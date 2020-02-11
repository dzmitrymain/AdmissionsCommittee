package by.epam.learning.yevtukhovich.admissionsCommittee.model.service;

import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.exception.ApplicantDaoException;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.Applicant;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.Faculty;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.factory.DaoFactory;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.factory.DaoType;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.implementation.ApplicantDaoImpl;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.state.ApplicantState;

import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.exception.ApplicantServiceException;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class ApplicantService extends Service {

    private ApplicantDaoImpl applicantDao;

    {
        applicantDao = (ApplicantDaoImpl) DaoFactory.getDao(DaoType.APPLICANT_DAO);
        dao = applicantDao;
    }

    public int addApplicant(Applicant newApplicant) throws ApplicantServiceException {
        try {
            return applicantDao.insert(newApplicant);
        } catch (ApplicantDaoException e) {
            logger.error(e.getMessage());
            throw new ApplicantServiceException(e.getMessage());
        }
    }

    public boolean isAlreadyApplied(Applicant newApplicant) throws ApplicantServiceException {
        try {
            int userId = newApplicant.getUserId();
            List<Applicant> applicants = applicantDao.getApplicantsByUserId(userId);
            for (Applicant applicant : applicants) {
                if (applicant.getApplicantState() == ApplicantState.APPLIED) {
                    return false;
                }
            }
        } catch (ApplicantDaoException e) {
            logger.error(e.getMessage());
            throw new ApplicantServiceException(e.getMessage());
        }
        return true;
    }

    public boolean cancelApplication(int applicantId) throws ApplicantServiceException {
        try {
            return applicantDao.deleteApplicantById(applicantId);
        } catch (ApplicantDaoException e) {
            logger.error(e.getMessage());
            throw new ApplicantServiceException(e.getMessage());
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

    public Applicant getApplicantById(int applicantId) throws ApplicantServiceException {
        try{
            return applicantDao.getApplicantById(applicantId);
        } catch (ApplicantDaoException e) {
            logger.error(e.getMessage());
            throw new ApplicantServiceException(e.getMessage());
        }
    }

    public Map<Faculty, TreeSet<Applicant>> getLatestEnrollmentApplicants() throws ApplicantServiceException {
        try {
            return applicantDao.getCurrentEnrollmentApplicants();
        } catch (ApplicantDaoException e) {
            logger.error(e.getMessage());
            throw new ApplicantServiceException(e.getMessage());
        }
    }

    public boolean enrollApplicants(List<Integer> idList) throws ApplicantServiceException {

        try {
            applicantDao.updateEnrolledApplicantsState(idList);
            applicantDao.updateNotEnrolledApplicantsState();
            return true;
        } catch (ApplicantDaoException e) {
            logger.error(e.getMessage());
            throw new ApplicantServiceException(e.getMessage());
        }


    }

    public TreeSet<Applicant> getCurrentApplicants() throws ApplicantServiceException {
        try {
            return applicantDao.getApplicantsByEnrollment(0);
        } catch (ApplicantDaoException e) {
            logger.error(e.getMessage());
            throw new ApplicantServiceException(e.getMessage());
        }
    }

    public TreeSet<Applicant> getApplicantsByEnrollment(int enrollmentId) throws ApplicantServiceException {
        try {
            return applicantDao.getApplicantsByEnrollment(enrollmentId);
        } catch (ApplicantDaoException e) {
            logger.error(e.getMessage());
            throw new ApplicantServiceException(e.getMessage());
        }
    }

    public List<Applicant> getApplicantsIdByFacultyId(int facultyId) throws ApplicantServiceException {

        try{
            return applicantDao.getApplicantsIdByFacultyId(facultyId);
        } catch (ApplicantDaoException e) {
            logger.error(e.getMessage());
            throw new ApplicantServiceException(e.getMessage());
        }
    }


}
