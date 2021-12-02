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
    @Query("select pcv, sl from plccontrollersvalue pcv " +
            "join signallist sl on sl.id = pcv.idSignalName " +
            "where pcv.idController = :controllerId and sl.delete = 0 order by sl.id asc ")
    List<PlcValue> getSignalOnController(@Param("controllerId") Long controllerId);
}
