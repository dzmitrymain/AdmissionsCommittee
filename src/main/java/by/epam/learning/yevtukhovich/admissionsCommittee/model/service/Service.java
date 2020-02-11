package by.epam.learning.yevtukhovich.admissionsCommittee.model.service;

import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.AbstractDao;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.exception.MySqlTransactionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;

public abstract class Service {

    protected Logger logger;
    protected AbstractDao dao;

    {
        logger= LogManager.getRootLogger();
    }

    public void setConnection(Connection connection){
        dao.setConnection(connection);
    }

    public void takeConnection(){
        dao.takeConnection();
    }

    public Connection getConnection(){
        return dao.getConnection();
    }

    public void releaseConnection(){
        dao.releaseConnection();
    }

    public boolean setAutoCommit(boolean autoCommit){

        boolean result=false;
        try{
            dao.setAutoCommit(autoCommit);
            result=true;
        } catch (MySqlTransactionException e) {
            logger.error(e.getMessage());
        }
        return result;
    }

    public boolean commit(){
        boolean result=false;
        try{
            dao.commit();
            result=true;
        } catch (MySqlTransactionException e) {
            logger.error(e.getMessage());
        }
        return result;
    }

    public boolean rollback(){
        boolean result=false;
        try{
            dao.rollback();
            result=true;
        } catch (MySqlTransactionException e) {
            logger.error(e.getMessage());
        }
        return result;
    }

}
