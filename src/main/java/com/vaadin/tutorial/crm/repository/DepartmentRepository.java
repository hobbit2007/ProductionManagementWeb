package com.vaadin.tutorial.crm.repository;

import com.vaadin.tutorial.crm.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Класс репозиторий содержащий sql запросы для работы с таблицей department
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    @Query("select d from department d where d.idShop = :shopID and d.delete = 0 order by d.id asc")
    List<Department> getAll(@Param("shopID") Long shopID);
}
