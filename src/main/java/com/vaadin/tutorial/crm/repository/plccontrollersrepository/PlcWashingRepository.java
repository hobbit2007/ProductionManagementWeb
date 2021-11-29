package com.vaadin.tutorial.crm.repository.plccontrollersrepository;

import com.vaadin.tutorial.crm.entity.plccontrollersentity.PlcWashing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Класс репозиторий содержащий sql запросы к таблице plcwashing
 */
@Repository
public interface PlcWashingRepository extends JpaRepository<PlcWashing, Long> {
    @Query("select pw from plcwashing pw where pw.delete = 0")
    List<PlcWashing> getAll();
}
