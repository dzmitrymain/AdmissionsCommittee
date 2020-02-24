package by.epam.learning.yevtukhovich.admissionsCommittee.model.dao;

import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.exception.UserDaoException;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.User;

import java.sql.Connection;

public interface UserDao {

    User getByLogin(String login) throws UserDaoException;
    User getByLogin(Connection connection, String login) throws UserDaoException;

    int insert(Connection connection, User user) throws UserDaoException;
    boolean update(User user) throws UserDaoException;
}
