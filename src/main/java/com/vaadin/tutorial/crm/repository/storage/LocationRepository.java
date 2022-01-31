package com.vaadin.tutorial.crm.repository.storage;

import com.vaadin.tutorial.crm.entity.storage.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}