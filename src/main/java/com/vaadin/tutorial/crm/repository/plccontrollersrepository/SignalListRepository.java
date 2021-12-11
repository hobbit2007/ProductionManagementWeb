package com.vaadin.tutorial.crm.repository.plccontrollersrepository;

import com.vaadin.tutorial.crm.entity.plccontrollersentity.SignalList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Класс репозиторий содержащий sql запросы к таблице signallist
 */
@Repository
public interface SignalListRepository extends JpaRepository<SignalList, Long> {
    @Query("select sl from signallist sl where sl.idController = :controllerId and sl.delete = 0")
    List<SignalList> findSignalList(@Param("controllerId") Long controllerId);

    //Подсчитываем количество типов сигналов
    @Query("select idGroup from signallist where idController = :controllerID group by idGroup")
    Long countGroups(@Param("controllerID") Long controllerID);
}
