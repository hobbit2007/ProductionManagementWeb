package com.vaadin.tutorial.crm.repository.storage;

import com.vaadin.tutorial.crm.entity.storage.MeasEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Класс репозиторий содержащий sql запросы для работы с таблицей measurement
 */
@Repository
public interface MeasRepository extends JpaRepository<MeasEntity, Long> {
    @Query("select m from measurement m where m.delete = 0")
    List<MeasEntity> getAll();
}
