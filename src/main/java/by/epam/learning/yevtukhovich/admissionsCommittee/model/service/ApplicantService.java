package by.epam.learning.yevtukhovich.admissionsCommittee.model.service;

import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.exception.*;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.factory.DaoFactory;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.factory.DaoType;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.implementation.ApplicantDaoImpl;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.implementation.EnrollmentDaoImpl;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.implementation.FacultyDaoImpl;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.implementation.SubjectDaoImpl;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.*;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.state.ApplicantState;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.state.EnrollmentState;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.exception.ApplicantServiceException;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.exception.SubjectServiceException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class ApplicantService extends Service {

    public boolean addApplicant(Applicant newApplicant, String[] grades) throws ApplicantServiceException {
        Connection connection = pool.getConnection();
        try {
            connection.setAutoCommit(false);
            try {
                if (!isAlreadyApplied(connection, newApplicant)) {
                    List<Subject> requiredSubjects=subjectDao.getRequiredSubjects(connection,newApplicant.getFacultyId());
                    newApplicant.setId(applicantDao.insert(connection, newApplicant));
                    List<Grade> newApplicantGrades = convertStringsToGrades(grades, requiredSubjects, newApplicant);
                    subjectDao.insertGrades(connection, newApplicantGrades);
                    connection.commit();
                    return true;
                } else {
                    return false;
                }
            } catch (ApplicantDaoException | SubjectDaoException e) {
                connection.rollback();
                throw new DaoException(e.getMessage());
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (DaoException | SQLException ex) {
            logger.error(ex.getMessage());
            throw new ApplicantServiceException(ex.getMessage());
        } finally {
            pool.releaseConnection(connection);
        }
    }

    private List<Grade> convertStringsToGrades(String[] gradesStrings, List<Subject> requiredSubjects, Applicant newApplicant) {

        List<Grade> applicantGrades = new ArrayList<>();
        Grade grade;
        for (int i = 0; i < gradesStrings.length; i++) {
            grade = new Grade();
            grade.setGrade(gradesStrings[i]);
            grade.setApplicantId(newApplicant.getId());
            grade.setSubjectId(requiredSubjects.get(i).getSubjectId());
            applicantGrades.add(grade);
        }
        return applicantGrades;
    }

    private boolean isAlreadyApplied(Connection connection, Applicant newApplicant) throws ApplicantServiceException {
        try {
            List<Applicant> applicants = applicantDao.getApplicantsByUserId(connection, newApplicant.getUserId());
            for (Applicant applicant : applicants) {
                if (applicant.getApplicantState() == ApplicantState.APPLIED) {
                    return true;
                }
            }
        } catch (ApplicantDaoException e) {
            logger.error(e.getMessage());
            throw new ApplicantServiceException(e.getMessage());
        }
        return false;
    }

    public boolean cancelApplication(int applicantId, User currentUser) throws ApplicantServiceException {
        Connection connection = pool.getConnection();
        try {
            connection.setAutoCommit(false);
            try {
                Enrollment currentEnrollment = enrollmentDao.getLatestEnrollment(connection);
                Applicant applicant = applicantDao.getApplicantById(connection, applicantId);
                if (currentEnrollment.getState() == EnrollmentState.OPENED && applicant.getEnrollmentId() == currentEnrollment.getEnrollmentId()
                        && applicant.getUserId() == currentUser.getUserId()) {
                    subjectDao.deleteGradesByApplicantId(connection, applicantId);
                    applicantDao.deleteApplicantById(connection, applicantId);
                    connection.commit();
                    return true;
                } else {
                    return false;
                }
            } catch (ApplicantDaoException | EnrollmentDaoException | SubjectDaoException e) {
                connection.rollback();
                throw new DaoException(e.getMessage());
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (DaoException | SQLException ex) {
            logger.error(ex.getMessage());
            throw new ApplicantServiceException(ex.getMessage());
        } finally {
            pool.releaseConnection(connection);
        }
    }

    public List<Applicant> getApplicantsByUserId(int userId) throws ApplicantServiceException {
        try {
            return applicantDao.getApplicantsByUserId(userId);
        } catch (ApplicantDaoException e) {
            logger.error(e.getMessage());
            throw new ApplicantServiceException(e.getMessage());
        }
    }

    public Applicant getApplicantById(int applicantId) throws ApplicantServiceException, SubjectServiceException {
        Connection connection = pool.getConnection();
        Applicant applicant;
        try {
            applicant = applicantDao.getApplicantById(connection, applicantId);
            if (applicant != null) {
                applicant.setSubjects(subjectDao.getSubjectGradesByApplicantId(connection, applicantId));
            }
            return applicant;
        } catch (ApplicantDaoException e) {
            logger.error(e.getMessage());
            throw new ApplicantServiceException(e.getMessage());
        } catch (SubjectDaoException e) {
            logger.error(e.getMessage());
            throw new SubjectServiceException(e.getMessage());
        } finally {
            pool.releaseConnection(connection);
        }
    }


    public TreeSet<Applicant> getCurrentApplicants() throws ApplicantServiceException {
        try {
            return applicantDao.getApplicantsByEnrollment(Optional.empty());
        } catch (ApplicantDaoException e) {
            logger.error(e.getMessage());
            throw new ApplicantServiceException(e.getMessage());
        }
    }

    public TreeSet<Applicant> getApplicantsByEnrollment(int enrollmentId) throws ApplicantServiceException {
        try {
            return applicantDao.getApplicantsByEnrollment(Optional.of(enrollmentId));
        } catch (ApplicantDaoException e) {
            logger.error(e.getMessage());
            throw new ApplicantServiceException(e.getMessage());
        }
    }
}
