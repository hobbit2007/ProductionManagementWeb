package com.vaadin.tutorial.crm.service.powerresources;


import com.vaadin.tutorial.crm.entity.PowerResourceDict;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс интерфейс содержащий методы для работы с таблицей power_resource_dict
 */
@Service
public interface PowerResourceDictService {
    List<PowerResourceDict> getAll();

    void saveAll(PowerResourceDict powerResourceDict);
}
