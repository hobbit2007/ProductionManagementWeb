package com.vaadin.tutorial.crm.service.impl;

import com.vaadin.tutorial.crm.entity.Shop;
import com.vaadin.tutorial.crm.repository.ShopRepository;
import com.vaadin.tutorial.crm.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс реализующий методы интерфейса ShopService
 */
@Service
public class ShopServiceImpl implements ShopService {
    private ShopRepository shopRepository;

    @Autowired
    public ShopServiceImpl(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    @Override
    public List<Shop> getAll() {
        return shopRepository.getAll();
    }
}
