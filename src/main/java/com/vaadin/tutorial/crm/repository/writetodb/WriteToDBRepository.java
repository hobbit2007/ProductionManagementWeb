package com.vaadin.tutorial.crm.repository.writetodb;

import com.vaadin.tutorial.crm.entity.writetodb.WriteToDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Класс репозиторий содержащий sql запросы к таблице settings
 */
@Repository
public interface WriteToDBRepository extends JpaRepository<WriteToDB, Long> {
    @Query("select s from settings s where s.delete = 0 order by s.id asc")
    List<WriteToDB> getAll();

    //Обновление коэффициентов трансформации для ввода1
    @Modifying
    @Transactional
    @Query("update settings s set s.repeatTime = :value where s.id = 7")
    void updateCoefficient1(@Param("value") Double value);

    //Обновление коэффициентов трансформации для ввода2
    @Modifying
    @Transactional
    @Query("update settings s set s.repeatTime = :value where s.id = 8")
    void updateCoefficient2(@Param("value") Double value);
}
