package com.vaadin.tutorial.crm.repository.plccontrollersrepository;

import com.vaadin.tutorial.crm.entity.plccontrollersentity.PlcResidueValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Класс репозиторий содержащий sql запросы к таблице plccontrollervalueresidue
 */
@Repository
public interface PlcResidueRepository extends JpaRepository<PlcResidueValue, Long> {
    @Query("select pcvr, sl from plccontrollervalueresidue pcvr " +
            "join signallist sl on sl.id = pcvr.idSignal " +
            "where sl.delete = 0 and pcvr.delete = 0 order by pcvr.dateCreate asc")
    List<PlcResidueValue> getAllSignal();
}
