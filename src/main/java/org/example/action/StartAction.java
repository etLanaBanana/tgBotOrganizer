package org.example.action;

import lombok.NonNull;
import org.example.handlers.TelegramRequestHandler;
import org.example.keyboards.StartKeyboard;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class StartAction {
    public static void startAction(@NonNull String chatId) {
        TelegramLongPollingBot bot = new TelegramRequestHandler();

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
