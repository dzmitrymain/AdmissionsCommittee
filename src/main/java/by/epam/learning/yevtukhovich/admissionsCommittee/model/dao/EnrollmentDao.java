package by.epam.learning.yevtukhovich.admissionsCommittee.model.dao;

import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.exception.EnrollmentDaoException;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.Enrollment;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;

public interface EnrollmentDao {

    List<Enrollment> getAllClosedEnrollments() throws EnrollmentDaoException;
    Enrollment getLatestEnrollment() throws EnrollmentDaoException;
    Enrollment getLatestEnrollment(Connection connection) throws EnrollmentDaoException;

    void openNewEnrollment(Timestamp timestamp) throws EnrollmentDaoException;
    void closeCurrentEnrollment(Connection connection, Timestamp timestamp) throws EnrollmentDaoException;


}
