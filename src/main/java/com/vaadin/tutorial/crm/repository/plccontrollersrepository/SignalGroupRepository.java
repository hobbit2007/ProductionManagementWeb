package com.vaadin.tutorial.crm.repository.plccontrollersrepository;

import com.vaadin.tutorial.crm.entity.plccontrollersentity.SignalGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Класс репозиторий содержащий sql запросы к таблице groups
 */
@Repository
public interface SignalGroupRepository extends JpaRepository<SignalGroup, Long> {
    @Query("select g from groups g where g.delete = 0")
    List<SignalGroup> getAll();
}
