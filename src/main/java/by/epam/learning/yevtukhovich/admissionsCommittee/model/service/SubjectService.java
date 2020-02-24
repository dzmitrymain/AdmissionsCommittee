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


    public List<Subject> getAllSubjects() throws SubjectServiceException {
        try {
            return subjectDao.getAllSubjects();
        } catch (SubjectDaoException e) {
            logger.error(e.getMessage());
            throw new SubjectServiceException(e.getMessage());
        }
    }

    public boolean insert(Subject subject) throws SubjectServiceException {
        try {
            return subjectDao.insert(subject);
        } catch (SubjectDaoException e) {
            logger.error(e.getMessage());
            throw new SubjectServiceException(e.getMessage());
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
}
