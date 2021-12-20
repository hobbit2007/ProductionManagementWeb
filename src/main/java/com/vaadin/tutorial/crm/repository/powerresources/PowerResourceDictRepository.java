package com.vaadin.tutorial.crm.repository.powerresources;

import com.vaadin.tutorial.crm.entity.PowerResourceDict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Класс репозиторий содержащий sql запросы к таблице power_resource_dict
 */
@Repository
public interface PowerResourceDictRepository extends JpaRepository<PowerResourceDict, Long> {
    @Query("select prd from power_resource_dict prd where prd.delete = 0")
    List<PowerResourceDict> getAll();
}
