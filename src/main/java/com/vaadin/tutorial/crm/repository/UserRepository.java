package com.vaadin.tutorial.crm.repository;

import com.vaadin.tutorial.crm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String userName);
}
