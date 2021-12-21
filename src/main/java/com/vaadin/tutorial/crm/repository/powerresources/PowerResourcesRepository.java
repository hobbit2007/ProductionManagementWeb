package com.vaadin.tutorial.crm.repository.powerresources;

import com.vaadin.tutorial.crm.entity.powerresources.PowerResources;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Класс репозиторий содержащий sql запросы к таблице power_resources
 */
@Repository
public interface PowerResourcesRepository extends JpaRepository<PowerResources, Long> {
    @Query("select pr from power_resources pr where pr.delete = 0")
    List<PowerResources> getAll();

    //Выбираем список показаний по конкретному ресурсу
    @Query("select pr from power_resources pr where pr.idPowerResource = :resourceID and pr.delete = 0")
    List<PowerResources> getAllByResourceId(@Param("resourceID") String resourceID);
}
