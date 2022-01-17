package com.vaadin.tutorial.crm.repository.for1c;

import com.vaadin.tutorial.crm.entity.for1c.For1CEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Класс репозиторий содержащий sql запросы для таблицы for1cerp
 */
@Repository
public interface For1CRepository extends JpaRepository<For1CEntity, Long> {
    @Query("select erp from for1cerp erp where erp.delete = 0")
    List<For1CEntity> getAll();

    //Обновление поля delete после того как данные были переданы в 1С считаем их удаленными
    @Modifying
    @Transactional
    @Query("update for1cerp erp set erp.delete = 1 where erp.delete = 0")
    void updateAfterLoadTo1C();
}
