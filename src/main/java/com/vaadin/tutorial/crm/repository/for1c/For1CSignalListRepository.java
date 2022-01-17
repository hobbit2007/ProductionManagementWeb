package com.vaadin.tutorial.crm.repository.for1c;

import com.vaadin.tutorial.crm.entity.for1c.For1CSignalListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Класс репозиторий содержащий sql запросы к таблице for1csignallist
 */
@Repository
public interface For1CSignalListRepository extends JpaRepository<For1CSignalListEntity, Long> {
    @Query("select sl from for1csignallist sl where sl.delete = 0 order by sl.id asc ")
    List<For1CSignalListEntity> getAll();

    //Обновление поля delete - удаление записи
    @Modifying
    @Transactional
    @Query("update for1csignallist sl set sl.delete = 1 where sl.id = :id")
    void updateDeleteRecord(@Param("id") Long id);
}
