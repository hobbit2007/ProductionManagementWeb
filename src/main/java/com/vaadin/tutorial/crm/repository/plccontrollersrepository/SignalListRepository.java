package com.vaadin.tutorial.crm.repository.plccontrollersrepository;

import com.vaadin.tutorial.crm.entity.plccontrollersentity.SignalList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

    //Обновляем некоторые значения в таблице signallist
    @Modifying
    @Transactional //Добавляем эту аннотацию, чтоб насильно сделать апдейт, даже если он запрещен
    @Query("update signallist sl set sl.signalName = :signalName, sl.signalDescription = :signalDescription, " +
            "sl.dbValue = :dbValue, sl.position = :position, sl.offset = :offSet, sl.idGroup = :idGroup " +
            "where sl.id = :id")
    void updateValue(@Param("signalName") String signalName, @Param("signalDescription") String signalDescription,
                     @Param("dbValue") int dbValue, @Param("position") int position,
                     @Param("offSet") int offSet, @Param("idGroup") long idGroup, @Param("id") long id);
}
