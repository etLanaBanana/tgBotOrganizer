package org.example.keyboards;

import org.example.handlers.TelegramRequestHandler;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class CreateActionKeyboard {
    public static void createAction(String selectedDate, String chatId) {
        TelegramLongPollingBot bot = new TelegramRequestHandler();

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Выберите действие для даты " + selectedDate + ":");

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        InlineKeyboardButton reminderButton = new InlineKeyboardButton();
        reminderButton.setText("Добавить напоминание");
        reminderButton.setCallbackData("action_reminder_" + selectedDate);

        InlineKeyboardButton taskButton = new InlineKeyboardButton();
        taskButton.setText("Добавить задачу");
        taskButton.setCallbackData("action_task_" + selectedDate);

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(reminderButton);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row2.add(taskButton);

        rows.add(row1);
        rows.add(row2);

        inlineKeyboardMarkup.setKeyboard(rows);
        message.setReplyMarkup(inlineKeyboardMarkup);

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
