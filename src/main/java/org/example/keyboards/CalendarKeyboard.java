package org.example.keyboards;

import org.example.handlers.TelegramRequestHandler;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CalendarKeyboard {
    public static void sendCalendar(String chatId) {
        TelegramLongPollingBot bot = new TelegramRequestHandler();

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Выберите нужную дату:");

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> daysOfWeekRow = new ArrayList<>();
        DayOfWeek[] daysOfWeek = DayOfWeek.values();
        DayOfWeek currentDayOfWeek = LocalDate.now().getDayOfWeek();

        DayOfWeek[] daysOfWeekFromCurrent = Arrays.copyOfRange(daysOfWeek, currentDayOfWeek.getValue() - 1, daysOfWeek.length);

        for (DayOfWeek dayOfWeek : daysOfWeekFromCurrent) {
            InlineKeyboardButton dayOfWeekButton = new InlineKeyboardButton();
            dayOfWeekButton.setText(dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()));
            dayOfWeekButton.setCallbackData("day_" + dayOfWeek.getValue());
            daysOfWeekRow.add(dayOfWeekButton);
        }

        if (currentDayOfWeek.getValue() > 1) {
            DayOfWeek[] daysOfWeekBeforeCurrent = Arrays.copyOfRange(daysOfWeek, 0, currentDayOfWeek.getValue() - 1);

            for (DayOfWeek dayOfWeek : daysOfWeekBeforeCurrent) {
                InlineKeyboardButton dayOfWeekButton = new InlineKeyboardButton();
                dayOfWeekButton.setText(dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()));
                dayOfWeekButton.setCallbackData("day_" + dayOfWeek.getValue());
                daysOfWeekRow.add(dayOfWeekButton);
            }
        }

        rows.add(0, daysOfWeekRow);

        LocalDate currentDate = LocalDate.now();
        String currentMonthYear = currentDate.format(DateTimeFormatter.ofPattern("yyyy MMMM"));

        InlineKeyboardButton monthYearButton = new InlineKeyboardButton();
        monthYearButton.setText(currentMonthYear);
        monthYearButton.setCallbackData("current_month_year");

        List<InlineKeyboardButton> monthYearRow = new ArrayList<>();
        monthYearRow.add(monthYearButton);

        rows.add(0, monthYearRow);

        for (int i = 0; i < 5; i++) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            for (int j = 0; j < 7; j++) {
                LocalDate date = currentDate.plusDays(i * 7 + j);
                String formattedDate = date.format(DateTimeFormatter.ofPattern("dd"));
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(formattedDate);
                button.setCallbackData("date_" + formattedDate);
                row.add(button);
            }
            rows.add(row);
        }

        inlineKeyboardMarkup.setKeyboard(rows);
        message.setReplyMarkup(inlineKeyboardMarkup);

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
