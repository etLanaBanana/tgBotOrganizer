package org.example.keyboards;

import org.example.handlers.TelegramRequestHandler;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TaskKeyboard {
    public static void createTask(String selectedDate, String chatId) {
        TelegramLongPollingBot bot = new TelegramRequestHandler();
        //
        SendMessage message = new SendMessage();
        message.setChatId(chatId);

        message.setText("Введите описание задачи для даты " + selectedDate + ":");
        //
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
