package org.example.model.repository;

import org.example.model.db.DBConnection;
import org.example.model.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserRepository {

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


