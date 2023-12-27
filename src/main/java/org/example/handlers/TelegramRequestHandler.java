package org.example.handlers;

import org.example.Configuration;
import org.example.TransmittedData;
import org.example.commands.StartCommands;
import org.example.keyboards.Keyboard;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
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
                    sendMessage.setChatId(update.getMessage().getChatId().toString());

                    var keyboard = new Keyboard();
                    sendMessage.setText("Выберите действие:");
                    sendMessage.setReplyMarkup(keyboard.getKeyboard());

                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (update.hasMessage() && update.getMessage().hasText()) {

                    String messageText = update.getMessage().getText();
                    if ("/calendar".equals(messageText)) {
                        sendCalendar(update.getMessage().getChatId());
                    }
                }
                if (update.hasCallbackQuery()) {

                    var query = update.getCallbackQuery();
                    String callData = query.getData();
                    Long chatID = query.getMessage().getChatId();

                    if (callData.startsWith("date_")) {
                        String selectedDate = callData.substring(5);
                        //добавить логику для создания напоминания на выбранную дату
                        createReminder(selectedDate, chatID);
                    }
                } else if (text.startsWith("/history")) {
                    String chatid = message.getChat().toString();
                    List<String> chatHistiry = historyOfMessages.getOrDefault(chatId, new ArrayList<>());
                    System.out.println("История запросов " + chatId + ":");
                    for (String request : chatHistory) {
                        System.out.println(request);
                    }
                    sendHistory(Long.valueOf(chatId));
                }
            }
        }
    }
    private void sendHistory(Long chatId) {
        List<String> chatHistory = historyOfMessages.getOrDefault(chatId.toString(), new ArrayList<>());

        StringBuilder historyMessage = new StringBuilder();
        historyMessage.append("История запросов:\n");

        for (String request : chatHistory) {
            historyMessage.append(request).append("\n");
        }

        sendMessage(historyMessage.toString(), chatId.toString());
    }
    private void createReminder(String date, Long chatId) {
        // логика для создания напоминания на выбранную дату
        // сохранить информацию о напоминании в баз
    }

    private void sendCalendar(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("Выберите нужную дату:");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setOneTimeKeyboard(true);
        keyboardMarkup.setResizeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();


        KeyboardRow daysOfWeekRow = new KeyboardRow();
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            daysOfWeekRow.add(dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()));
        }
        keyboard.add(daysOfWeekRow);

        for (int i = 0; i < 5; i++) {
            KeyboardRow row = new KeyboardRow();
            for (int j = 0; j < 7; j++) {
                LocalDate date = currentDate.plusDays(i * 7 + j);
                row.add(date.format(DateTimeFormatter.ofPattern("dd MMM")));
            }
            keyboard.add(row);
        }

        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }



    private void sendMessage(String text, String chatId) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println("чет пошло не так при отправке сообщения");
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
