package org.example.action;

import lombok.NonNull;
import org.example.handlers.TelegramRequestHandler;
import org.example.keyboards.StartKeyboard;
import org.example.model.entity.User;
import org.example.model.repository.UserRepository;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;

public class StartAction {
    public static void startAction(@NonNull String chatId, String tgUserId) {
        TelegramLongPollingBot bot = new TelegramRequestHandler();

        User newUser = new User();
        newUser.setTgUserId(tgUserId);

        newUser.setFirstEntryDate(LocalDateTime.now());

        UserRepository userRepository = new UserRepository();
        userRepository.saveUser(newUser);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        var keyboard = new StartKeyboard();
        sendMessage.setText("Выберите действие:");
        sendMessage.setReplyMarkup(keyboard.getKeyboard());

        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
