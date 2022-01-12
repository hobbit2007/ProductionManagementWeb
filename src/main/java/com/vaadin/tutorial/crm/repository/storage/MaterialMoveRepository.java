package com.vaadin.tutorial.crm.repository.storage;

import com.vaadin.tutorial.crm.entity.storage.MaterialMoveEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Класс репозиторий содержащий sql запросы к таблице material_move
 */
@Repository
public interface MaterialMoveRepository extends JpaRepository<MaterialMoveEntity, Long> {
    @Query("select mv from material_move mv where mv.delete = 0")
    List<MaterialMoveEntity> getAll();
}