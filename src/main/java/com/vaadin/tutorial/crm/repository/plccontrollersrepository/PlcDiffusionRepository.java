package com.vaadin.tutorial.crm.repository.plccontrollersrepository;

import com.vaadin.tutorial.crm.entity.plccontrollersentity.PlcDiffusionValue;
import com.vaadin.tutorial.crm.entity.plccontrollersentity.PlcValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Класс репозиторий содержащий sql запросы к таблице plccontrollervaluedifusion
 */
@Repository
public interface PlcDiffusionRepository extends JpaRepository<PlcDiffusionValue, Long> {
    @Query("select pcvd, sl from plccontrollervaluedifusion pcvd " +
            "join signallist sl on sl.id = pcvd.idSignal " +
            "where sl.delete = 0 and pcvd.delete = 0 order by pcvd.dateCreate asc")
    List<PlcDiffusionValue> getAllSignal();
}
