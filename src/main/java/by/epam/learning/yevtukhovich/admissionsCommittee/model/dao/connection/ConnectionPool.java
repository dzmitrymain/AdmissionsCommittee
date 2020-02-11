package by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.connection;

import by.epam.learning.yevtukhovich.admissionsCommittee.util.configuration.ConfigurationData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ConnectionPool {

    private static final int CONNECTIONS_NUMBER=10;

    private static ConnectionPool instance;
    private static BlockingQueue<Connection> connections;

    static {
        instance = new ConnectionPool();

        try{
            Class.forName(ConfigurationData.getString(ConfigurationData.DRIVER_PATH));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        connections=new ArrayBlockingQueue<>(CONNECTIONS_NUMBER);

        for(int i=0; i<CONNECTIONS_NUMBER;i++){

            try {
                connections.add(DriverManager.getConnection(ConfigurationData.getString(ConfigurationData.DATABASE_CONNECTION_PATH),ConfigurationData.getString(ConfigurationData.DATABASE_LOGIN),ConfigurationData.getString(ConfigurationData.DATABASE_PASSWORD)));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static ConnectionPool getInstance(){
        return instance;
    }

    public Connection getConnection(){
        Connection connection=null;
        try{
            connection=connections.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(connection==null){
            try{
                connections.add(DriverManager.getConnection(ConfigurationData.getString(ConfigurationData.DATABASE_CONNECTION_PATH),ConfigurationData.getString(ConfigurationData.DATABASE_LOGIN),ConfigurationData.getString(ConfigurationData.DATABASE_PASSWORD)));
                connection=connections.poll();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public void releaseConnection(Connection connection){

        if(connection!=null){

            connections.add(connection);
        }
    }

    public void closeAll(){
        for(Connection connection:connections){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
