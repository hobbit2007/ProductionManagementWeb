package com.vaadin.tutorial.crm.repository;

import com.vaadin.tutorial.crm.entity.PowerResources;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Класс репозиторий содержащий sql запросы к таблице power_resources
 */
@Repository
public interface PowerResourcesRepository extends JpaRepository<PowerResources, Long> {
    @Query("select pr from power_resources pr where pr.delete = 0")
    List<PowerResources> getAll();
}
