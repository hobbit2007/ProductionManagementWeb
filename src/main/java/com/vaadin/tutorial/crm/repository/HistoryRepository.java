package com.vaadin.tutorial.crm.repository;

import com.vaadin.tutorial.crm.entity.HistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Класс репозиторий содержащий sql запросы к таблице history
 */
@Repository
public interface HistoryRepository extends JpaRepository<HistoryEntity, Long> {
    @Query("select h from history h where h.delete = 0")
    List<HistoryEntity> getAll();
}
