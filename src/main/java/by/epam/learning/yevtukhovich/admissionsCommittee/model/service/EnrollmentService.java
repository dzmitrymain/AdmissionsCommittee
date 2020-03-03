package by.epam.learning.yevtukhovich.admissionsCommittee.model.service;

import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.exception.ApplicantDaoException;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.exception.DaoException;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.exception.EnrollmentDaoException;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.Applicant;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.Enrollment;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.Faculty;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.exception.EnrollmentServiceException;
import com.google.common.annotations.VisibleForTesting;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

public class EnrollmentService extends Service {

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

    public void openNewEnrollment(Timestamp timestamp) throws EnrollmentServiceException {
        try {
            enrollmentDao.openNewEnrollment(timestamp);
        } catch (EnrollmentDaoException e) {
            logger.error(e.getMessage());
            throw new EnrollmentServiceException(e.getMessage());
        }
    }

    public void closeCurrentEnrollment(Timestamp timestamp) throws EnrollmentServiceException {
        Connection connection = pool.getConnection();
        try {
            connection.setAutoCommit(false);
            try {
                enrollmentDao.closeCurrentEnrollment(connection, timestamp);
                Map<Faculty, TreeSet<Applicant>> currentApplicants = applicantDao.getCurrentEnrollmentApplicants(connection);
                if (!currentApplicants.isEmpty()) {
                    List<Integer> enrolledApplicantsIdList = calculateEnrolledApplicantsId(currentApplicants);
                    applicantDao.updateEnrolledApplicantsState(connection, enrolledApplicantsIdList);
                    applicantDao.updateNotEnrolledApplicantsState(connection);
                }
                connection.commit();
            } catch (EnrollmentDaoException | ApplicantDaoException e) {
                throw new DaoException(e.getMessage());
            } finally {
                connection.rollback();
                connection.setAutoCommit(true);
            }
        } catch (DaoException | SQLException ex) {
            logger.error(ex.getMessage());
            throw new EnrollmentServiceException(ex.getMessage());
        } finally {
            pool.releaseConnection(connection);
        }
    }

    @VisibleForTesting
    List<Integer> calculateEnrolledApplicantsId(Map<Faculty, TreeSet<Applicant>> applicants) {
        List<Integer> enrolledIdList = new ArrayList<>();
        Set<Faculty> faculties = applicants.keySet();
        int count;

        for (Faculty faculty : faculties) {
            count = 0;

            Set<Applicant> currentFacultyApplicants = applicants.get(faculty);
            Iterator<Applicant> iterator = currentFacultyApplicants.iterator();

            while (iterator.hasNext()) {
                enrolledIdList.add(iterator.next().getId());
                if (++count == faculty.getCapacity()) {
                    break;
                }
            }
        }
        return enrolledIdList;
    }
}
