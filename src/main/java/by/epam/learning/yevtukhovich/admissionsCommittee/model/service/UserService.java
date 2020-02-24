package by.epam.learning.yevtukhovich.admissionsCommittee.model.service;

import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.exception.UserDaoException;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.factory.DaoFactory;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.factory.DaoType;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.implementation.UserDaoImpl;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.User;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.exception.UserServiceException;

import java.sql.Connection;
import java.util.Optional;

public class UserService extends Service {

    public User login(String login) throws UserServiceException {
        try {
            return userDao.getByLogin(login);
        } catch (UserDaoException e) {
            logger.error(e.getMessage());
            throw new UserServiceException(e.getMessage());
        }


    }

    public Optional<Integer> addUser(User user) throws UserServiceException {
        Connection connection = pool.getConnection();
        try {
            if (checkLoginUniqueness(connection, user.getLogin())) {
                return Optional.of(userDao.insert(connection, user));
            } else {
                return Optional.empty();
            }
        } catch (UserDaoException e) {
            logger.error(e.getMessage());
            throw new UserServiceException(e.getMessage());
        } finally {
            pool.releaseConnection(connection);
        }
    }

    public boolean editUser(User user) throws UserServiceException {
        try {
            return userDao.update(user);
        } catch (UserDaoException e) {
            logger.error(e.getMessage());
            throw new UserServiceException(e.getMessage());
        }
    }

    private boolean checkLoginUniqueness(Connection connection, String login) throws UserServiceException {

        try {
            User user = userDao.getByLogin(connection, login);
            return user == null;

        } catch (UserDaoException e) {
            logger.error(e.getMessage());
            throw new UserServiceException(e.getMessage());
        }
    }
}
