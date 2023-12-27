package org.example;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.HashMap;
import java.util.Map;

public class ChatRouter {
    private final Map<String, TransmittedData> chats;
    private ServiceManager serviceManager;

    public ChatRouter() {
        chats = new HashMap<>();
        serviceManager = new ServiceManager();
    }

    public SendMessage route(String chatId, String text, String command) {
        if (!chats.containsKey(chatId)) {
            chats.put(chatId, new TransmittedData(text, command));
        }

        var data = chats.get(chatId);

        return serviceManager.proccessUpdate(chatId, data);
    }
}
