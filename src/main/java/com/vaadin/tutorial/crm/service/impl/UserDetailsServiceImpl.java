package com.vaadin.tutorial.crm.service.impl;

import com.vaadin.tutorial.crm.entity.User;
import com.vaadin.tutorial.crm.repository.UserRepository;
import com.vaadin.tutorial.crm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
}