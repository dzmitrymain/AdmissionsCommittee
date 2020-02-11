package by.epam.learning.yevtukhovich.admissionsCommittee.model.service;

import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.exception.FacultyDaoException;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.Faculty;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.factory.DaoFactory;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.factory.DaoType;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.implementation.FacultyDaoImpl;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.exception.FacultyServiceException;
import java.util.List;

public class FacultyService extends Service {

    private FacultyDaoImpl facultyDao;

    {
        facultyDao= (FacultyDaoImpl) DaoFactory.getDao(DaoType.FACULTY_DAO);
        dao=facultyDao;
    }

    public List<Faculty> retrieveAllFaculties() throws FacultyServiceException {
        try {
            return facultyDao.listFaculties();
        } catch (FacultyDaoException e) {
            logger.error(e.getMessage());
            throw new FacultyServiceException(e.getMessage());
        }
    }

    public Faculty getById(int id) throws FacultyServiceException {
        try {
            return facultyDao.getById(id);
        } catch (FacultyDaoException e) {
            logger.error(e.getMessage());
            throw new FacultyServiceException(e.getMessage());
        }
    }

    public int addFaculty(Faculty faculty) throws FacultyServiceException {
        try{
            return facultyDao.insert(faculty);
        } catch (FacultyDaoException e) {
            logger.error(e.getMessage());
            throw new FacultyServiceException(e.getMessage());
        }
    }

    public boolean deleteFaculty(int facultyId) throws FacultyDaoException {
        try{
            return facultyDao.delete(facultyId);
        } catch (FacultyDaoException e) {
            logger.error(e.getMessage());
            throw new FacultyDaoException(e.getMessage());
        }
    }




}
