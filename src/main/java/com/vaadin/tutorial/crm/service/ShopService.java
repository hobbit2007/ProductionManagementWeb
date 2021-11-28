package com.vaadin.tutorial.crm.service;

import com.vaadin.tutorial.crm.entity.Shop;

import java.util.List;

/**
 * Класс интерфейс содержащий методы для работы с таблицей shop
 */
public interface ShopService {
    List<Shop> getAll();
}
