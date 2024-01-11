package org.example.model.db;

import lombok.Setter;
import org.example.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnection {
    @Setter
    private static Connection connection;
    private Logger logger = LoggerFactory.getLogger(DBConnection.class);

    private DBConnection(){
        try{
            connection = DriverManager.getConnection(Configuration.DB_URL,
                    Configuration.DB_USER, Configuration.DB_PASSWORD);
        } catch (SQLException exception) {
            logger.error("Произошла ошибка при попытке подключиться к базе данных", exception);
            //System.out.println("Произошла ошибка при попытке подключиться к базе данных");
        }
    }

    public static Connection getConnection() {
        return connection;
    }
    private static DBConnection instance = null;
    public static DBConnection getInstance() {
        if(instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }
    public void executeQuery() {
        ResultSet resultSet = null;
        try {
            resultSet = connection.prepareStatement(" ").executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(resultSet);
    }
}
