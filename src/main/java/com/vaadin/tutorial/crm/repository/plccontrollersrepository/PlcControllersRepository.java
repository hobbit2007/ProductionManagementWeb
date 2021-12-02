package com.vaadin.tutorial.crm.repository.plccontrollersrepository;

import com.vaadin.tutorial.crm.entity.plccontrollersentity.PlcControllers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Класс репозиторий содержащий sql запросы к таблице plccontrollers
 */
@Repository
public interface PlcControllersRepository extends JpaRepository<PlcControllers, Long> {
    @Query("select pc from plccontrollers pc where pc.delete = 0 order by pc.id asc")
    List<PlcControllers> getAll();
}
