package com.vaadin.tutorial.crm.repository.storage;

import com.vaadin.tutorial.crm.entity.storage.SupplierEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Класс репозиторий содержащий sql запросы к таблице supplier
 */
@Repository
public interface SupplierRepository extends JpaRepository<SupplierEntity, Long> {
    @Query("select s from supplier s where s.delete = 0 order by s.supplierName asc")
    List<SupplierEntity> getAll();
}
