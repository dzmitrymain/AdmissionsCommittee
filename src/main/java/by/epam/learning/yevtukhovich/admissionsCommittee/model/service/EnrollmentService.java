package by.epam.learning.yevtukhovich.admissionsCommittee.model.service;

import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.exception.EnrollmentDaoException;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.Enrollment;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.factory.DaoFactory;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.factory.DaoType;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.implementation.EnrollmentDaoImpl;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.exception.EnrollmentServiceException;

import java.sql.Timestamp;
import java.util.List;

public class EnrollmentService extends Service {

    private EnrollmentDaoImpl enrollmentDao;

    {
        enrollmentDao = (EnrollmentDaoImpl) DaoFactory.getDao(DaoType.ENROLLMENT_DAO);
        dao = enrollmentDao;
    }

    public List<Enrollment> getAllClosedEnrollments() throws EnrollmentServiceException {
        try {
            return enrollmentDao.getAllClosedEnrollments();
        } catch (EnrollmentDaoException e) {
            logger.error(e.getMessage());
            throw new EnrollmentServiceException(e.getMessage());
        }
    }


    public Enrollment getLatestEnrollment() throws EnrollmentServiceException {
        try {
            return enrollmentDao.getLatestEnrollment();
        } catch (EnrollmentDaoException e) {
            logger.error(e.getMessage());
            throw new EnrollmentServiceException(e.getMessage());
        }
    }

    public boolean openNewEnrollment(Timestamp timestamp) throws EnrollmentServiceException {
        try{
            return enrollmentDao.openNewEnrollment(timestamp);
        } catch (EnrollmentDaoException e) {
            logger.error(e.getMessage());
            throw new EnrollmentServiceException(e.getMessage());
        }
    }

    public boolean closeCurrentEnrollment(Timestamp timestamp) throws EnrollmentDaoException {

       try{
           return enrollmentDao.closeCurrentEnrollment(timestamp);
       } catch (EnrollmentDaoException e) {
           logger.error(e.getMessage());
           throw new EnrollmentDaoException(e.getMessage());
       }
    }


}
