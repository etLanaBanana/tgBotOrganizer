package org.example.handlers;

import lombok.SneakyThrows;
import org.example.Configuration;
import org.example.action.*;
import org.example.commands.StartCommands;
import org.example.keyboards.CalendarCallBack;
import org.example.model.repository.ReminderRepository;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

import static org.example.action.StartAction.startAction;
import static org.example.keyboards.CalendarKeyboard.sendCalendar;

public class TelegramRequestHandler extends TelegramLongPollingBot {
    private Map<String, List<String>> historyOfMessages = new HashMap<>();
    private ReminderRepository remainderRepository;
    private String tgUserId;

    public void init() throws TelegramApiException {
        this.execute(new SetMyCommands(StartCommands.init(), new BotCommandScopeDefault(), null));
    }

    @SneakyThrows
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
                    startAction(chatId, tgUserId);

                    if (text.startsWith("/task")) {
                        TaskAction.tackAction(chatId);
                    }
                    if (text.startsWith("/checklist")) {
                        ChecklistAction.checklistAction(chatId);
                    }
                    if (text.startsWith("/reminder")) {
                        ReminderAction.remainderAction(chatId);
                    }
                    if (text.startsWith("/plans")) {
                        PlansAction.plansAction(chatId);
                    }
                    if (text.startsWith("/events")) {
                        EventsAction.eventsAction(chatId);
                    }
                } else if (text.startsWith("/calendar")) {
                    sendCalendar(chatId);
                } else if (text.startsWith("/history")) {
                    sendHistory(chatId);
                }
            }
        } else if (update.hasCallbackQuery()) {
            CalendarCallBack.calendarCallBack(update, remainderRepository);
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

    @Override
    public String getBotToken() {
        return Configuration.BOT_TOKEN;
    }

    @Override
    public String getBotUsername() {
        return "tg_organizer_bot";
    }
}