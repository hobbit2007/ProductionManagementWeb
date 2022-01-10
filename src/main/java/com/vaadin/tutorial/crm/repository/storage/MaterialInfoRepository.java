package com.vaadin.tutorial.crm.repository.storage;

import com.vaadin.tutorial.crm.entity.storage.MaterialInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Класс репозиторий содержащий sql запросы к таблице materialinfo
 */
@Repository
public interface MaterialInfoRepository extends JpaRepository<MaterialInfoEntity, Long> {
    @Query("select mi from materialinfo mi where mi.delete = 0 and mi.writeoff = 0 order by mi.dateCreate asc")
    List<MaterialInfoEntity> getAll();

    //Проверяем наличие объекта хранения по артикулу в БД
    @Query("select mi from materialinfo mi where mi.article = :article and mi.delete = 0")
    List<MaterialInfoEntity> getCheckArticle(@Param("article") String article);

    //Обновляем некоторые значения в таблице materialinfo
    @Modifying
    @Transactional //Добавляем эту аннотацию, чтоб насильно сделать апдейт, даже если он запрещен
    @Query("update materialinfo mi set mi.materialName = :materialName, mi.article = :article, mi.quantity = :qty, " +
            "mi.costPrice = :costPrice, mi.marketPrice = :marketPrice " +
            "where mi.id = :id")
    void updateValue(@Param("materialName") String materialName, @Param("article") String article, @Param("qty") double qty,
                        @Param("costPrice") double costPrice, @Param("marketPrice") double marketPrice, @Param("id") long id);
}
