package com.vaadin.tutorial.crm.repository.storage;

import com.vaadin.tutorial.crm.entity.storage.StorageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Класс репозиторий содержащий sql запросы к таблице storage
 */
@Repository
public interface StorageRepository extends JpaRepository<StorageEntity, Long> {
    @Query("select s from storage s where s.delete = 0 order by s.storageName asc")
    List<StorageEntity> getAll();

    //Запрос проверяет наличие склада в БД
    @Query("select s from storage s where s.storageName = :storeName and s.delete = 0")
    List<StorageEntity> getCheckStorage(@Param("storeName") String storeName);

    //Запрос проверяет наличие склада в выбранной локации
    //@Query("select s from storage s where s.idLocation = :locationID and s.delete = 0 order by s.storageName asc")
    //List<StorageEntity> getStorageByLocationID(@Param("locationID") Long locationID);

    //Поиск склада по ID
    @Query("select s from storage s where s.id = :storeID and s.delete = 0")
    List<StorageEntity> getFindStorageByID(@Param("storeID") Long storeID);

    //Обновляем имя склада
    @Modifying
    @Transactional
    @Query("update storage s set s.storageName = :storageName, s.shortName = :shortName where s.id = :id")
    void updateStorageName(@Param("storageName") String storageName, @Param("id") Long id, @Param("shortName") String shortName);
}
