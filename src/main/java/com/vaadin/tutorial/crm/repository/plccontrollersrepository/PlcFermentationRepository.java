package com.vaadin.tutorial.crm.repository.plccontrollersrepository;

import com.vaadin.tutorial.crm.entity.plccontrollersentity.PlcFermentationValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Класс репозиторий содержащий sql запросы к таблице plccontrollervaluefermentation
 */
@Repository
public interface PlcFermentationRepository extends JpaRepository<PlcFermentationValue, Long> {
    @Query("select pcvf, sl from plccontrollervaluefermentation pcvf " +
            "join signallist sl on sl.id = pcvf.idSignal " +
            "where sl.delete = 0 and pcvf.delete = 0 order by pcvf.dateCreate asc")
    List<PlcFermentationValue> getAllSignal();
}
