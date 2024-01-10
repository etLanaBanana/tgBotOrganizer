package org.example.service;

import org.example.model.db.BDManager;
import org.example.model.entity.Event;
import org.example.model.repository.EventRepository;

import java.util.List;

public class EventService {
    public static Object saveAll;
    private final EventRepository eventRepository;
    public EventService() {
        this.eventRepository = BDManager.getInstance().getEventRepository();
    }

    public static void saveAll(List<Event> events) {
        //eventRepository.saveAll(events);
    }


    public static List<Event> getAll() {
        return null;//.eventRepository.getAll
    }

}
