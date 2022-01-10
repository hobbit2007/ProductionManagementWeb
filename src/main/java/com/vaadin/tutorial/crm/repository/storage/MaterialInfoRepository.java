package com.vaadin.tutorial.crm.repository.storage;

import com.vaadin.tutorial.crm.entity.storage.MaterialInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Класс репозиторий содержащий sql запросы к таблице materialinfo
 */
@Repository
public interface MaterialInfoRepository extends JpaRepository<MaterialInfoEntity, Long> {
    @Query("select mi from materialinfo mi where mi.delete = 0 order by mi.dateCreate asc")
    List<MaterialInfoEntity> getAll();

    //Проверяем наличие объекта хранения по артикулу в БД
    @Query("select mi from materialinfo mi where mi.article = :article and mi.delete = 0")
    List<MaterialInfoEntity> getCheckArticle(@Param("article") String article);
}
