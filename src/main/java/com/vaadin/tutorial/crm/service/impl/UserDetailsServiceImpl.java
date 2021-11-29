package com.vaadin.tutorial.crm.service.impl;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.tutorial.crm.entity.User;
import com.vaadin.tutorial.crm.repository.UserRepository;
import com.vaadin.tutorial.crm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Класс реализующий методы из интерфейса UserService
 */
@Service
public class UserDetailsServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public Optional<User> getAll(String username) {
        return userRepository.findByLogin(username);
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public void saveAll(User user) {
        if (user != null)
            userRepository.saveAndFlush(user);
        else
            Notification.show("Нет данных для записи", 5000, Notification.Position.MIDDLE);

    }
    //метод обновляющей поле даты захода пользователя в систему
    @Override
    public void updateDateActive(User user) {
        if (user != null)
            userRepository.updateUserActive(user.getLastDateActive(), user.getId());
    }
}