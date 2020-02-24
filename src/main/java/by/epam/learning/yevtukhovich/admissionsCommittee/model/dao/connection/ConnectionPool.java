package by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.connection;

import by.epam.learning.yevtukhovich.admissionsCommittee.util.configuration.ConfigurationData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class ConnectionPool {

    private static final int CONNECTIONS_INIT_NUMBER = 10;
    private static final int QUEUE_WAITING_DELAY = 1;
    private static ConnectionPool instance;

    private BlockingQueue<Connection> connections;

    public void initPool() {
        try {
            Class.forName(ConfigurationData.getString(ConfigurationData.DRIVER_PATH));

            connections = new ArrayBlockingQueue<>(CONNECTIONS_INIT_NUMBER);

            for (int i = 0; i < CONNECTIONS_INIT_NUMBER; i++) {

                try {
                    connections.add(DriverManager.getConnection(ConfigurationData.getString(ConfigurationData.DATABASE_CONNECTION_PATH), ConfigurationData.getString(ConfigurationData.DATABASE_LOGIN), ConfigurationData.getString(ConfigurationData.DATABASE_PASSWORD)));
                } catch (SQLException e) {
                    //logger
                    //e.printStackTrace();
                }
            }
        } catch (ClassNotFoundException e) {
            //logger
            e.printStackTrace();
        }
    }


    public static ConnectionPool getInstance() {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    public Connection getConnection() {
        Connection connection = null;
//        try {
//            connection = connections.poll(QUEUE_WAITING_DELAY, TimeUnit.SECONDS);
//        } catch (InterruptedException e) {
//            //logger
//            //  e.printStackTrace();
//        }

        while (connection == null) {
            try {
                connection = connections.poll(QUEUE_WAITING_DELAY, TimeUnit.MILLISECONDS);
                if (connection == null) {
                    connections.add(DriverManager.getConnection(ConfigurationData.getString(ConfigurationData.DATABASE_CONNECTION_PATH), ConfigurationData.getString(ConfigurationData.DATABASE_LOGIN), ConfigurationData.getString(ConfigurationData.DATABASE_PASSWORD)));
                }
            } catch (SQLException | InterruptedException e) {
                //logger
                e.printStackTrace();
            }
        }
        return connection;
    }

    public void releaseConnection(Connection connection) {

        if (connection != null) {

            connections.add(connection);
        }
    }

    public void closeAll() {
        for (Connection connection : connections) {
            try {
                connection.close();
            } catch (SQLException e) {
                //logger
                e.printStackTrace();
            }
        }
    }

}
