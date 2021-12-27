package com.vaadin.tutorial.crm.repository.powerresources;

import com.vaadin.tutorial.crm.entity.powerresources.PowerResources;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

/**
 * Класс репозиторий содержащий sql запросы к таблице power_resources
 */
@Repository
public interface PowerResourcesRepository extends JpaRepository<PowerResources, Long> {
    @Query("select pr from power_resources pr where pr.delete = 0")
    List<PowerResources> getAll();

    //Выбираем список показаний по конкретному ресурсу
    @Query("select pr from power_resources pr where pr.idPowerResource = :resourceID and pr.delete = 0")
    List<PowerResources> getAllByResourceId(@Param("resourceID") Long resourceID);

    //Выбирает список показаний за указанные даты
    @Query("select pr from power_resources pr where pr.dateCreate between :dateBegin and :dateEnd and pr.delete = 0")
    List<PowerResources> getResourceBySearch(@Param("dateBegin") Date dateBegin, @Param("dateEnd") Date dateEnd);

    //Обновление значения показания
    @Modifying
    @Transactional
    @Query("update power_resources pr set pr.value = :value where pr.id = :ID")
    void updateValue(@Param("ID") Long ID, @Param("value") Double value);

    //Обновление ежедневной разницы показаний
    @Modifying
    @Transactional
    @Query("update power_resources pr set pr.valueDaily = :value where pr.id = :ID")
    void updateValueDaily(@Param("ID") Long ID, @Param("value") Double value);

    //Обновление еженедельной суммы показаний
    @Modifying
    @Transactional
    @Query("update power_resources pr set pr.valueWeekly = :value where pr.id = :ID")
    void updateValueWeekly(@Param("ID") Long ID, @Param("value") Double value);

    //Обновление итоговой еженедельной разницы показаний
    @Modifying
    @Transactional
    @Query("update power_resources pr set pr.totalValueWeekly = :value where pr.id = :ID")
    void updateTotalValueWeekly(@Param("ID") Long ID, @Param("value") Double value);
}
