package org.example.model.repository;

import org.example.model.db.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;


public class ReminderRepository {
    private Logger log = LoggerFactory.getLogger(ReminderRepository.class);
    private Connection connection;

    public ReminderRepository(Connection connection) {
        this.connection = connection;
    }
    public void saveReminder(String date, String text, String userId) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String sql = "INSERT INTO reminders (date, text, userId) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, date);
            statement.setString(2, text);
            statement.setString(3, userId);
            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            log.error("Не удалось сохранить напоминание", e);
            throw new RuntimeException(e);
        }
    }
}