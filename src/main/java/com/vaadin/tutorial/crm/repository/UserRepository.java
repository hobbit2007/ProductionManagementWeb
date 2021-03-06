package com.vaadin.tutorial.crm.repository;

import com.vaadin.tutorial.crm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Класс интерфейс содержащий sql запросы к таблице userslist
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String userName);

    @Query("select u from userslist u where u.delete = 0")
    List<User> getAll();

    //Поиск пользователя по id отдела
    @Query("select u from userslist u where u.idDepartment = :idDepartment and u.delete = 0")
    List<User> getAllByIdDepartment(@Param("idDepartment") Long idDepartment);

    //Обновляем в БД дату захода пользователя в систему
    @Modifying
    @Transactional
    @Query("update userslist set lastDateActive = :dateActive where id = :userId")
    void updateUserActive(@Param("dateActive") Date dateActive, @Param("userId") Long userId);

    //Обновляем данные по пользователю
    @Modifying
    @Transactional
    @Query("update userslist set fio = :userFIO, email = :userEmail, role = :userRole where id = :userID")
    void updateUserInfo(@Param("userID") Long userID, @Param("userFIO") String userFIO, @Param("userEmail") String userEmail, @Param("userRole") String userRole);
}
