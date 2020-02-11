package by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.implementation;

import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.AbstractDao;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.UserDao;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.exception.UserDaoException;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.User;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.role.UserRole;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Fields;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDaoImpl extends AbstractDao implements UserDao {

   // private static final Logger LOGGER =  LogManager.getLogger(UserDaoImpl.class.getName());

    private static final String FIND_BY_LOGIN = "SELECT user.id, login, password, first_name, last_name, patronymic, role_type FROM admissions_committee.user JOIN admissions_committee.user_role ON user.user_role_id=user_role.id WHERE login=?";
    private static final String INSERT_USER = "INSERT INTO user(user_role_id, login, first_name, last_name, patronymic, password) VALUES (?, ?, ?, ?, ?, ?)";
private static final String UPDATE_USER="UPDATE admissions_committee.user SET last_name=?,first_name=?,patronymic=? WHERE user.id=?";

    public User getByLogin(String login) throws UserDaoException {

            if (login != null) {

                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_LOGIN);
                    preparedStatement.setString(1, login);

                    ResultSet resultSet = preparedStatement.executeQuery();
                    User user=null;
                    while (resultSet.next()) {
                        user = new User();
                        user.setUserId(resultSet.getInt(Fields.ID));
                        user.setLogin(resultSet.getString(Fields.LOGIN));
                        user.setFirstName(resultSet.getString(Fields.FIRST_NAME));
                        user.setLastName(resultSet.getString(Fields.LAST_NAME));
                        user.setPatronymic(resultSet.getString(Fields.PATRONYMIC));
                        user.setPassword(resultSet.getInt(Fields.PASSWORD));
                        user.setRole(UserRole.valueOf(resultSet.getString(Fields.ROLE_TYPE).toUpperCase()));
                    }
                    return user;

                } catch (SQLException e) {
                    throw new UserDaoException("could not get user by login: "+e.getMessage());
                }
            } else {
                logger.info("login was null");
            }


        return null;
    }

    public int insert(User user) throws UserDaoException {
        int generatedId=0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, user.getRole().getOrdinalNumber());
            preparedStatement.setString(2, user.getLogin());
            preparedStatement.setString(3, user.getFirstName());
            preparedStatement.setString(4, user.getLastName());
            preparedStatement.setString(5, user.getPatronymic());
            preparedStatement.setInt(6, user.getPassword());
            preparedStatement.execute();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if(generatedKeys.next()){
               generatedId = generatedKeys.getInt(1);
            }
        } catch (SQLException e) {
            throw new UserDaoException("could not insert user: " + e.getMessage());
        }
        return generatedId;
    }

    public boolean update(User user) throws UserDaoException {

        try{
            PreparedStatement preparedStatement=connection.prepareStatement(UPDATE_USER);
            preparedStatement.setString(1,user.getLastName());
            preparedStatement.setString(2,user.getFirstName());
            preparedStatement.setString(3,user.getPatronymic());
            preparedStatement.setInt(4,user.getUserId());
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            throw new UserDaoException("could not update user: "+e.getMessage());
        }
    }
}