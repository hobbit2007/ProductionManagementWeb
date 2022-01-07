package com.vaadin.tutorial.crm.repository.storage;

import com.vaadin.tutorial.crm.entity.storage.MaterialInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Класс репозиторий содержащий sql запросы к таблице materialinfo
 */
@Repository
public interface MaterialInfoRepository extends JpaRepository<MaterialInfoEntity, Long> {
    @Query("select mi from materialinfo mi where mi.delete = 0 order by mi.materialName asc")
    List<MaterialInfoEntity> getAll();
}
