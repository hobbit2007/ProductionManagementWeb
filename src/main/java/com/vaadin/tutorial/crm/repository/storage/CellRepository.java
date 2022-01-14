package com.vaadin.tutorial.crm.repository.storage;

import com.vaadin.tutorial.crm.entity.storage.CellEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Класс репозиторий содержащий sql запросы к таблице cells
 */
@Repository
public interface CellRepository extends JpaRepository<CellEntity, Long> {
    @Query("select c from cells c where c.delete = 0 and c.idStorage = :idStorage order by c.cellName asc")
    List<CellEntity> getAll(@Param("idStorage") Long idStorage);

    //Запрос проверяет наличие ячейки в БД
    @Query("select c from cells c where c.cellName = :cellName and c.delete = 0 and c.idStorage = :idStorage")
    List<CellEntity> getCheckCell(@Param("cellName") String cellName, @Param("idStorage") Long idStorage);

    //Поиск ячейки по ID
    @Query("select c from cells c where c.id = :cellID and c.delete = 0 and c.idStorage = :idStorage")
    List<CellEntity> getFindCellByID(@Param("cellID") Long cellID, @Param("idStorage") Long idStorage);

    //Обновляем имя ячейки
    @Modifying
    @Transactional
    @Query("update cells c set c.cellName = :cellName where c.id = :id")
    void updateCellName(@Param("cellName") String cellName, @Param("id") Long id);
}
