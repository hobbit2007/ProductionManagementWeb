package com.vaadin.tutorial.crm.repository.storage;

import com.vaadin.tutorial.crm.entity.storage.ChangePriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Класс репозиторий содержащий sql запросы к таблице price_change
 */
@Repository
public interface ChangePriceRepository extends JpaRepository<ChangePriceEntity, Long> {
    @Query("select cp from price_change cp where cp.delete = 0")
    List<ChangePriceEntity> getAll();

    //Поиск по id объекта хранения
    @Query("select cp from price_change cp where cp.delete = 0 and cp.idMaterial = :idMaterial")
    List<ChangePriceEntity> getAllByIdMaterial(@Param("idMaterial") Long idMaterial);
}
