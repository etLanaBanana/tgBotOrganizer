package org.example.keyboards;

import org.example.handlers.TelegramRequestHandler;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static java.awt.SystemColor.text;

public class SaveRemainderKeyboard {
    private void saveReminder(String reminderText, String selectedDate, String chatId) throws TelegramApiException {
        TelegramLongPollingBot bot = new TelegramRequestHandler();

        SendMessage message = new SendMessage();
        message.setChatId(chatId);


        SendMessage confirmationMessage = new SendMessage();
        confirmationMessage.setChatId(chatId);
        confirmationMessage.setText("Напоминание: " + reminderText + " добавлено для даты " + selectedDate);
        bot.execute(confirmationMessage);


    }
}
