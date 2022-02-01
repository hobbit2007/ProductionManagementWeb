package com.vaadin.tutorial.crm.repository.storage;

import com.vaadin.tutorial.crm.entity.storage.LocationEntity;
import com.vaadin.tutorial.crm.entity.storage.StorageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Класс репозиторий содержащий sql запросы к таблице store_location
 */
@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, Long> {
    @Query("select sl from store_location sl where sl.delete = 0 order by sl.id asc")
    List<LocationEntity> getAll();

    //Поиск локации по ID
    @Query("select sl from store_location sl where sl.id = :locationID and sl.delete = 0")
    List<LocationEntity> getFindLocationByID(@Param("locationID") Long locationID);

    //Запрос проверяет наличие локации в БД
    @Query("select sl from store_location sl where sl.locationName = :locationName and sl.delete = 0")
    List<LocationEntity> getCheckLocation(@Param("locationName") String locationName);

    //Обновляем имя и описание локации
    @Modifying
    @Transactional
    @Query("update store_location sl set sl.locationName = :locationName, sl.locationDescription = :locationDescription where sl.id = :id")
    void updateLocation(@Param("locationName") String locationName, @Param("id") Long id, @Param("locationDescription") String locationDescription);
}
