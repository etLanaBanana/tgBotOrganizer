package org.example;

import org.example.handlers.TelegramRequestHandler;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) {
        System.out.println(Configuration.DB_URL);

        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            TelegramRequestHandler bot = new TelegramRequestHandler();
            telegramBotsApi.registerBot(bot);
            bot.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}