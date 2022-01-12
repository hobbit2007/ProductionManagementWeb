package com.vaadin.tutorial.crm.service;

import com.vaadin.tutorial.crm.entity.User;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Класс интерфейс содержащий методы для работы с таблицей userslist
 */
@Scope("session")
@Service
public interface UserService {
    Optional<User> getAll(String username);

    List<User> getAll();

    void saveAll(User user);

    void updateDateActive(User user);

    void updateUserInfo(User user);

    /**
     * Метод выполняющий поиск пользователя по id отдела
     * @param idDepartment - id отдела
     * @return - возвращает список найденных пользователей
     */
    List<User> getAllByIdDepartment(Long idDepartment);
}
