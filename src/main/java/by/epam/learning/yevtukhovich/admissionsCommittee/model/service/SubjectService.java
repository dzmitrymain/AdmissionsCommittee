package by.epam.learning.yevtukhovich.admissionsCommittee.model.service;

import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.exception.SubjectDaoException;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.Grade;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.Subject;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.factory.DaoFactory;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.factory.DaoType;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.implementation.SubjectDaoImpl;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.exception.SubjectServiceException;

import java.util.List;

public class SubjectService extends Service {

    private SubjectDaoImpl subjectDao;

    {
        subjectDao = (SubjectDaoImpl) DaoFactory.getDao(DaoType.SUBJECT_DAO);
        dao = subjectDao;
    }

    public List<Subject> retrieveRequiredSubjects(int facultyId) throws SubjectServiceException {

        try {
            return subjectDao.getRequiredSubjects(facultyId);
        } catch (SubjectDaoException e) {
            logger.error(e.getMessage());
            throw new SubjectServiceException(e.getMessage());
        }


    }

    public boolean addGrades(List<Grade> grades) throws SubjectServiceException {

        try {
            return subjectDao.insertGrades(grades);
        } catch (SubjectDaoException e) {
            logger.error(e.getMessage());
            throw new SubjectServiceException(e.getMessage());
        }
    }

    public boolean deleteGrades(int applicantId) throws SubjectDaoException {
        try {
            return subjectDao.deleteGradesByApplicantId(applicantId);
        } catch (SubjectDaoException e) {
            logger.error(e.getMessage());
            throw new SubjectDaoException(e.getMessage());
        }
    }

    public List<Subject> getSubjectGrades(int applicantId) throws SubjectServiceException {
        try {
            return subjectDao.getSubjectGradesByApplicantId(applicantId);
        } catch (SubjectDaoException e) {
            logger.error(e.getMessage());
            throw new SubjectServiceException(e.getMessage());
        }
    }

    public List<Subject> getAllSubjects() throws SubjectDaoException {
        try {
            return subjectDao.getAllSubjects();
        } catch (SubjectDaoException e) {
            logger.error(e.getMessage());
            throw new SubjectDaoException(e.getMessage());
        }
    }

    public boolean insert(Subject subject) throws SubjectDaoException {
        try {
            return subjectDao.insert(subject);
        } catch (SubjectDaoException e) {
            logger.error(e.getMessage());
            throw new SubjectDaoException(e.getMessage());
        }
    }

    public boolean deleteSubject(int subjectId) throws SubjectServiceException {
        try {
            return subjectDao.delete(subjectId);
        } catch (SubjectDaoException e) {
            logger.error(e.getMessage());
            throw new SubjectServiceException(e.getMessage());
        }
    }

    public boolean addRequiredSubjects(int facultyId, int[] subjectId) throws SubjectDaoException {

        try {
            return subjectDao.insertRequiredSubjects(facultyId, subjectId);
        } catch (SubjectDaoException e) {
            logger.error(e.getMessage());
            throw new SubjectDaoException(e.getMessage());
        }
    }

    public boolean deleteRequiredSubjectsByFacultyId(int facultyId) throws SubjectDaoException {
        try {
            return subjectDao.deleteRequiredSubjects(facultyId);
        } catch (SubjectDaoException e) {
            logger.error(e.getMessage());
            throw new SubjectDaoException();
        }
    }
}
