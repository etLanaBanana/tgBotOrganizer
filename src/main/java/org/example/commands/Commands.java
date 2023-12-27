package org.example.commands;

public enum Commands {
    START("/start", "запуск бота"),
    CALENDAR("/calendar", "календарь"),
    HISTORY("/history", "Получить историю сообщений");

    private final String command;
    private final String description;

    Commands(String command, String description) {
        this.command = command;
        this.description = description;
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }
}
