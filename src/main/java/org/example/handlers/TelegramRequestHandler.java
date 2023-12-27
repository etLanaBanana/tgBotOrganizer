package org.example.handlers;

import org.example.Configuration;
import org.example.commands.StartCommands;
import org.example.keyboards.Keyboard;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

public class TelegramRequestHandler extends TelegramLongPollingBot {
    private Map<String, List<String>> historyOfMessages = new HashMap<>();

    public void init() throws TelegramApiException {
        this.execute(new SetMyCommands(StartCommands.init(), new BotCommandScopeDefault(), null));
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            var message = update.getMessage();

            if (message.hasText()) {
                String text = message.getText();
                String chatId = message.getChatId().toString();

                List<String> chatHistory = historyOfMessages.getOrDefault(chatId, new ArrayList<>());
                chatHistory.add(text);
                historyOfMessages.put(chatId, chatHistory);

                if (text.startsWith("/start")) {
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(chatId);

                    var keyboard = new Keyboard();
                    sendMessage.setText("Выберите действие:");
                    sendMessage.setReplyMarkup(keyboard.getKeyboard());

                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                } else if (text.startsWith("/calendar")) {
                    sendCalendar(chatId);
                } else if (text.startsWith("/history")) {
                    sendHistory(chatId);
                }
            }
        } else if (update.hasCallbackQuery()) {
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
                    execute(deleteInlineKeyboard);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (callData.startsWith("action_reminder_")) {
                String selectedDate = callData.substring(15);
                createReminder(selectedDate, String.valueOf(chatId));
            } else if (callData.startsWith("action_task_")) {
                String selectedDate = callData.substring(11);
                createTask(selectedDate, String.valueOf(chatId));
            }
        }
    }

    private void sendHistory(String chatId) {
        List<String> chatHistory = historyOfMessages.getOrDefault(chatId, new ArrayList<>());

        StringBuilder historyMessage = new StringBuilder();
        historyMessage.append("История запросов:\n");

        for (String request : chatHistory) {
            historyMessage.append(request).append("\n");
        }

        sendMessage(historyMessage.toString(), chatId);
    }

    private void sendMessage(String string, String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(string);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendCalendar(String chatId) {
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
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void createReminder(String selectedDate, String chatId) {
        //
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Введите текст напоминания для даты " + selectedDate + ":");
        //
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void createTask(String selectedDate, String chatId) {
        //
        SendMessage message = new SendMessage();
        message.setChatId(chatId);

        message.setText("Введите описание задачи для даты " + selectedDate + ":");
        //
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    private void createAction(String selectedDate, String chatId) {
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
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotToken() {
        return Configuration.BOT_TOKEN;
    }

    @Override
    public String getBotUsername() {
        return "tg_organizer_bot";
    }
}