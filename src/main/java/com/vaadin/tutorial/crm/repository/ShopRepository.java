package com.vaadin.tutorial.crm.repository;

import com.vaadin.tutorial.crm.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Класс репозиторий содержащий sql запросы к таблице shop
 */
@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    @Query("select s from shop s where s.delete = 0 order by s.id asc")
    List<Shop> getAll();
}
