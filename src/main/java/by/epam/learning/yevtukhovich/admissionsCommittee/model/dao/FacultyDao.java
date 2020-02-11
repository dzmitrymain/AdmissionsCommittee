package by.epam.learning.yevtukhovich.admissionsCommittee.model.dao;

import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.exception.FacultyDaoException;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.Faculty;

import java.util.List;

public interface FacultyDao {

    List<Faculty> listFaculties() throws FacultyDaoException;

}
