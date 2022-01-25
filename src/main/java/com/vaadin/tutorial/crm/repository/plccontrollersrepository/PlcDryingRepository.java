package com.vaadin.tutorial.crm.repository.plccontrollersrepository;

import com.vaadin.tutorial.crm.entity.plccontrollersentity.PlcDryingValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Класс репозиторий содержащий sql запросы к таблице plccontrollervaluedrying
 */
@Repository
public interface PlcDryingRepository extends JpaRepository<PlcDryingValue, Long> {
    @Query("select pcvd, sl from plccontrollervaluedrying pcvd " +
            "join signallist sl on sl.id = pcvd.idSignal " +
            "where sl.delete = 0 and pcvd.delete = 0 order by pcvd.dateCreate asc")
    List<PlcDryingValue> getAllSignal();
}
