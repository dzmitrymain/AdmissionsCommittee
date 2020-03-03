package by.epam.learning.yevtukhovich.admissionsCommittee.model.service;

import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.exception.UserDaoException;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.implementation.UserDaoImpl;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.User;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.exception.UserServiceException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.Connection;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserDaoImpl userDao;

    @Mock
    Connection connection;


    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheckLoginUniquenessNegative() throws UserDaoException, UserServiceException {

        when(userDao.getByLogin(any(Connection.class), anyString())).thenReturn(new User());
        boolean isLoginUnique = userService.checkLoginUniqueness(connection, "");

        verify(userDao, times(1)).getByLogin(any(Connection.class), anyString());
        assertFalse(isLoginUnique);
    }

    @Test
    public void testCheckLoginUniquenessPositive() throws UserDaoException, UserServiceException {

        when(userDao.getByLogin(any(Connection.class), anyString())).thenReturn(null);
        boolean isLoginUnique = userService.checkLoginUniqueness(connection, "");

        verify(userDao, times(1)).getByLogin(any(Connection.class), anyString());
        assertTrue(isLoginUnique);
    }
}