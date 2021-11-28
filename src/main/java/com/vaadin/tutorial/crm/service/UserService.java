package com.vaadin.tutorial.crm.service;

import com.vaadin.tutorial.crm.entity.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Класс интерфейс содержащий методы для работы с таблицей userslist
 */
@Service
public interface UserService {
    Optional<User> getAll(String username);

    void saveAll(User user);
}
