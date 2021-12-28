package com.vaadin.tutorial.crm.repository.writetodb;

import com.vaadin.tutorial.crm.entity.writetodb.WriteToDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Класс репозиторий содержащий sql запросы к таблице settings
 */
@Repository
public interface WriteToDBRepository extends JpaRepository<WriteToDB, Long> {
    @Query("select s from settings s where s.delete = 0 order by s.id asc")
    List<WriteToDB> getAll();
}
