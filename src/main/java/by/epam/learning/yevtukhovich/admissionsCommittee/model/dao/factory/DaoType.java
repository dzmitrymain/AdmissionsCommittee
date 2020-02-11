package by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.factory;

import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.AbstractDao;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.implementation.*;

public enum DaoType {

    FACULTY_DAO(new FacultyDaoImpl()),
    USER_DAO(new UserDaoImpl()),
    ENROLLMENT_DAO(new EnrollmentDaoImpl()),
    SUBJECT_DAO(new SubjectDaoImpl()),
    APPLICANT_DAO(new ApplicantDaoImpl());

    // private IDao dao;
    private AbstractDao dao;

    DaoType(AbstractDao dao) {
        this.dao = dao;
    }

    public AbstractDao getDao() {
        return dao;
    }

}
