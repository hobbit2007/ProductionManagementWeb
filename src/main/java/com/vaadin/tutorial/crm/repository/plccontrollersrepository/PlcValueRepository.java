package com.vaadin.tutorial.crm.repository.plccontrollersrepository;

import com.vaadin.tutorial.crm.entity.plccontrollersentity.PlcValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Класс репозиторий содержащий sql запросы к таблице plcwashing
 */
@Repository
public interface PlcValueRepository extends JpaRepository<PlcValue, Long> {
    @Query("select pw, sl from plcwashing pw " +
            "join signallist sl on sl.idController = pw.idController " +
            "where pw.idController = :controllerId and pw.delete = 0")
    List<PlcValue> getSignalOnController(@Param("controllerId") Long controllerId);
}
