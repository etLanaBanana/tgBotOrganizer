package org.example.keyboards;

import org.example.handlers.TelegramRequestHandler;
import org.example.model.repository.ReminderRepository;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;

import static org.example.keyboards.CreateActionKeyboard.createAction;

public class CalendarCallBack {
    public static void calendarCallBack(Update update, ReminderRepository reminderRepository) {
        TelegramLongPollingBot bot = new TelegramRequestHandler();

        var query = update.getCallbackQuery();
        String callData = query.getData();
        Long chatId = query.getMessage().getChatId();

        if (callData.startsWith("date_")) {
            String selectedDate = callData.substring(5);
            createAction(selectedDate, String.valueOf(chatId));
            SendMessage deleteInlineKeyboard = new SendMessage();
            deleteInlineKeyboard.setChatId(chatId);
            deleteInlineKeyboard.setText("Выберите действие:");
            deleteInlineKeyboard.setReplyMarkup(new ReplyKeyboardRemove());

            try {
                bot.execute(deleteInlineKeyboard);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (callData.startsWith("action_reminder_")) {
            String selectedDate = callData.substring(16);
            try {
                CreateRemainderKeyboard.createReminder(selectedDate, String.valueOf(chatId), update, reminderRepository);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if (callData.startsWith("reminder_text_")) {
            String reminderText = callData.substring(14);
            String selectedDate;// = получить выбранную дату
            //saveReminder(reminderText, selectedDate, String.valueOf(chatId));
        } else if (callData.startsWith("action_task_")) {
            String selectedDate = callData.substring(12);
            TaskKeyboard.createTask(selectedDate, String.valueOf(chatId));
        }
    }
}
