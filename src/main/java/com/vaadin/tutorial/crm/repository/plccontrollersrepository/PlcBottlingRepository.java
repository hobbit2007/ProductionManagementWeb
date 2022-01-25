package com.vaadin.tutorial.crm.repository.plccontrollersrepository;

import com.vaadin.tutorial.crm.entity.plccontrollersentity.PlcBottlingValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Класс репозиторий содержащий sql запросы к таблице plccontrollervaluebottling
 */
@Repository
public interface PlcBottlingRepository extends JpaRepository<PlcBottlingValue, Long> {
    @Query("select pcvb, sl from plccontrollervaluebottling pcvb " +
            "join signallist sl on sl.id = pcvb.idSignal " +
            "where sl.delete = 0 and pcvb.delete = 0 order by pcvb.dateCreate asc")
    List<PlcBottlingValue> getAllSignal();
}
