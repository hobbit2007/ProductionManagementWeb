package com.vaadin.tutorial.crm.repository.storage;

import com.vaadin.tutorial.crm.entity.storage.StorageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Класс репозиторий содержащий sql запросы к таблице storage
 */
@Repository
public interface StorageRepository extends JpaRepository<StorageEntity, Long> {
    @Query("select s from storage s where s.delete = 0")
    List<StorageEntity> getAll();
}
