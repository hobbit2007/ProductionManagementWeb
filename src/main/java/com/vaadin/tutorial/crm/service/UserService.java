package com.vaadin.tutorial.crm.service;

import com.vaadin.tutorial.crm.entity.User;

import java.util.Optional;

/**
 * Класс интерфейс содержащий методы для работы с таблицей userslist
 */
public interface UserService {
    Optional<User> getAll(String username);
}
