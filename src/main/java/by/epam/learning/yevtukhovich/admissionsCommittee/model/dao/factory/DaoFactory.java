package by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.factory;

import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.AbstractDao;

public class DaoFactory {

    public static AbstractDao getDao(DaoType type){
        return type.getDao();
    }
}
