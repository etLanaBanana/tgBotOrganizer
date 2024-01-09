package org.example.model;

import lombok.Getter;
import org.example.model.db.DBConnection;
import org.example.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

public class BDManager {
    private Logger logger = LoggerFactory.getLogger(BDManager.class);

    @Getter
    private final EventRepository eventRepository;

    private BDManager() {
        logger.info("DBManager created");

        Connection connection = DBConnection.getInstance().getConnection();

        eventRepository = new EventRepository(connection);
    }

    public EventRepository getEventRepository() {
        return eventRepository;
    }

    private static BDManager instance = null;

    public  static  BDManager getInstance() {
        if(instance == null) {
            instance = new BDManager();
        }
        return instance;
    }
}
