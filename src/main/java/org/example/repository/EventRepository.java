package org.example.repository;

import lombok.Getter;
import org.example.model.entity.Event;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EventRepository {
    private Connection connection;
    private Logger log;

    public EventRepository(Connection connection) {

        this.connection = connection;
    }

    public List<Event> findAll() throws SQLException {
        List<Event> events = new ArrayList<>();

        Statement statement = connection.createStatement();

        String sql = "select * from events";
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            Long id = resultSet.getLong("id");
            String title = resultSet.getString("title");
            var description = resultSet.getString("description");
            var metroStation = resultSet.getString("metrostation");
            var uri = resultSet.getString("uri");
            var city = resultSet.getString("city");
            var price = resultSet.getString("price");

            var event = new Event(id, title, description, metroStation, uri, city, price);
            events.add(event);
        }
        resultSet.close();
        statement.close();

        return events;
    }
    public List<Event> saveAll(List<Event> events) {
        //KOD
        return events;
    }
    public List<Event> getAll(List<Event> events) {
        //KOD
        return events;
    }
}


