package by.epam.learning.yevtukhovich.admissionsCommittee.model.service;

import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.exception.UserDaoException;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.User;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.factory.DaoFactory;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.factory.DaoType;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.implementation.UserDaoImpl;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.exception.UserServiceException;

public class UserService extends Service {

    private UserDaoImpl userDao;

    {
        userDao = (UserDaoImpl) DaoFactory.getDao(DaoType.USER_DAO);
        dao = userDao;
    }

    public User login(String login) throws UserServiceException {

        try {
            return userDao.getByLogin(login);
        } catch (UserDaoException e) {
            logger.error(e.getMessage());
            throw new UserServiceException(e.getMessage());
        }


    }

    public int addUser(User user) throws UserServiceException {

        try {
            return userDao.insert(user);
        } catch (UserDaoException e) {
            logger.error(e.getMessage());
            throw new UserServiceException(e.getMessage());
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

    public boolean checkLoginUniqueness(String login) throws UserServiceException {

        try {
            User user = userDao.getByLogin(login);
            return user == null ? true : false;

        } catch (UserDaoException e) {
            logger.error(e.getMessage());
            throw new UserServiceException(e.getMessage());
        }

    }


}
