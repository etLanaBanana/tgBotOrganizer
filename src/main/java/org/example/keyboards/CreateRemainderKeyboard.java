package org.example.keyboards;

import org.example.handlers.TelegramRequestHandler;
import org.example.model.repository.ReminderRepository;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Connection;
import java.sql.SQLException;

public class CreateRemainderKeyboard {
    private static Connection connection;

    public static void createReminder(String selectedDate, String chatId, Update update, ReminderRepository reminderRepository) throws SQLException {
        TelegramLongPollingBot bot = new TelegramRequestHandler();

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Введите текст напоминания для даты " + selectedDate + ":");

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        String reminderText = update.getMessage().getText();
        reminderRepository.saveReminder(selectedDate, reminderText, update.getMessage().getText());

        try {
            reminderRepository.saveReminder(selectedDate, reminderText, reminderText);
            SendMessage confirmationMessage = new SendMessage();
            confirmationMessage.setChatId(update.getMessage().getChatId());
            confirmationMessage.setText("Напоминание: " + reminderText + " добавлено для даты " + selectedDate);
            bot.execute(confirmationMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
