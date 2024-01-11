package org.example.action;

import lombok.NonNull;
import org.example.handlers.TelegramRequestHandler;
import org.example.model.entity.User;
import org.example.model.repository.UserRepository;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.time.LocalDateTime;

public class StartAction {
    public static void startAction(@NonNull String chatId, String tgUserId) {
        TelegramLongPollingBot bot = new TelegramRequestHandler();


        User newUser = new User();
        newUser.setTgUserId(chatId);

        newUser.setFirstEntryDate(LocalDateTime.now());

        UserRepository userRepository = new UserRepository();
        userRepository.saveUser(newUser);


    }
}
