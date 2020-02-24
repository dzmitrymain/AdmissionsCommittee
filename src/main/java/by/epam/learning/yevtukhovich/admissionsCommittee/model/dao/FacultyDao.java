package by.epam.learning.yevtukhovich.admissionsCommittee.model.dao;

import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.exception.FacultyDaoException;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.Faculty;

import java.sql.Connection;
import java.util.List;

public interface FacultyDao {

    List<Faculty> getAllFaculties() throws FacultyDaoException;
    Faculty getById(Connection connection, int facultyId) throws FacultyDaoException;

    int insert(Connection connection, Faculty faculty) throws FacultyDaoException;
    boolean delete(Connection connection, int facultyId) throws FacultyDaoException;
}
