package org.example.model.repository;

import org.example.model.db.DBConnection;
import org.example.model.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class UserRepository {



    public User getUserById(String tgUserId) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String sql = "SELECT * FROM users WHERE tg_user_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, tgUserId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                User user = new User();
                user.setTgUserId(resultSet.getString("tg_user_id"));
                user.setFirstEntryDate(resultSet.getObject("first_entry_date", LocalDateTime.class));
                return user;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось получить пользователя", e);
        }
    }

    public void saveUser(User user) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String sql = "INSERT INTO users (tg_user_id, first_entry_date) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, user.getTgUserId());
            statement.setObject(2, user.getFirstEntryDate());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось добавить пользователя", e);
        }
    }
}

