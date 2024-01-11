package org.example.service;

import lombok.Getter;
import org.example.model.entity.User;
import org.example.model.repository.UserRepository;

import java.time.LocalDateTime;

public class SaveNewUserService {
    private static boolean startButtonActive = true;  // Статическая переменная для хранения состояния кнопки
    private final UserRepository userRepository;

    public SaveNewUserService() {
        userRepository = new UserRepository();
    }

    public boolean isStartButtonActive() {
        return startButtonActive;
    }

    public void addNewUser(Long chatId) {
        String tgUserId = chatId.toString();
        User existingUser = userRepository.getUserById(tgUserId);

        if (existingUser == null && startButtonActive) {
            // Создаем нового пользователя и сохраняем его
            User newUser = new User();
            newUser.setTgUserId(tgUserId);
            newUser.setFirstEntryDate(LocalDateTime.now());
            userRepository.saveUser(newUser);

            startButtonActive = false;
        }
    }
}