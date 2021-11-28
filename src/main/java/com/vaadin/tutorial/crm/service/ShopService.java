package com.vaadin.tutorial.crm.service;

import com.vaadin.tutorial.crm.entity.Shop;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс интерфейс содержащий методы для работы с таблицей shop
 */
@Service
public interface ShopService {
    List<Shop> getAll();
}
