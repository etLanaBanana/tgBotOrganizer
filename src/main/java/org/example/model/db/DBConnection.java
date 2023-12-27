package org.example.model.db;

import lombok.Getter;
import lombok.Setter;
import org.example.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    @Setter
    @Getter
    private Connection connection;
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

    private static DBConnection instance = null;
    public static DBConnection getInstance() {
        if(instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }
}
