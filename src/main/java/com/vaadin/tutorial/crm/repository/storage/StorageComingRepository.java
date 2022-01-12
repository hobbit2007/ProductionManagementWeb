package com.vaadin.tutorial.crm.repository.storage;

import com.vaadin.tutorial.crm.entity.storage.StorageComingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Класс репозиторий содержащий sql запросы к таблице storage_coming
 */
@Repository
public interface StorageComingRepository extends JpaRepository<StorageComingEntity, Long> {
    @Query("select sce from storage_coming sce where sce.delete = 0 order by sce.dateCreate asc")
    List<StorageComingEntity> getAll();

    //Поиск всех записей по id объекта хранения
    @Query("select sce from storage_coming sce where sce.delete = 0 and sce.idMaterial = :idMaterial order by sce.dateCreate asc")
    List<StorageComingEntity> getAllByIdMaterial(@Param("idMaterial") Long idMaterial);
}
