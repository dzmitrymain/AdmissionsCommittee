package by.epam.learning.yevtukhovich.admissionsCommittee.model.dao;

import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.connection.ConnectionPool;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.exception.CommitException;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.exception.MySqlTransactionException;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.exception.RollbackException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.sql.Connection;
import java.sql.SQLException;

public abstract class AbstractDao {

    private ConnectionPool pool;
    protected Connection connection;
    protected Logger logger;

    {
        pool=ConnectionPool.getInstance();
        logger= LogManager.getRootLogger();
    }

    public void setConnection(Connection connection){
        this.connection=connection;
    }

    public void takeConnection(){
        connection=pool.getConnection();
    }

    public Connection getConnection(){
        return connection;
    }

    public void releaseConnection(){
        pool.releaseConnection(connection);
    }

    public void setAutoCommit(boolean autoCommit) throws MySqlTransactionException {
        try{
            if(connection!=null){
                connection.setAutoCommit(autoCommit);
                logger.info("auto commmit set to "+autoCommit);
            }
        } catch (SQLException e) {
            logger.error("exception while setting autocommit: "+e.getMessage());
            throw new MySqlTransactionException(e.getMessage());
        }
    }

    public void commit() throws CommitException {
        try{
            if(connection!=null && !connection.getAutoCommit()){
            connection.commit();
            }
        } catch (SQLException e) {
            logger.error("exception while commit: "+e.getMessage());
            throw new CommitException(e.getMessage());
        }
    }

    public void rollback() throws RollbackException {

        try{
            if(connection!=null&& !connection.getAutoCommit()){
                connection.rollback();
            }
        } catch (SQLException e) {
            logger.error("exception while rollback: "+e.getMessage());
            throw new RollbackException(e.getMessage());
        }
    }
}
