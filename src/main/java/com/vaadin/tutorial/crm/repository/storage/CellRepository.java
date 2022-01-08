package com.vaadin.tutorial.crm.repository.storage;

import com.vaadin.tutorial.crm.entity.storage.CellEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Класс репозиторий содержащий sql запросы к таблице cells
 */
@Repository
public interface CellRepository extends JpaRepository<CellEntity, Long> {
    @Query("select c from cells c where c.delete = 0 order by c.cellName asc")
    List<CellEntity> getAll();
}