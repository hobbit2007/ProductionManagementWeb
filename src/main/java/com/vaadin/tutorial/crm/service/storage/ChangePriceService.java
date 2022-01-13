package com.vaadin.tutorial.crm.service.storage;

import com.vaadin.tutorial.crm.entity.storage.ChangePriceEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс интерфейс содержащий методы для доступа к данным таблицы price_change
 */
@Service
public interface ChangePriceService {
    List<ChangePriceEntity> getAll();

    /**
     * Метод выбирает историю изменения цены по id объекта хранения
     * @param idMaterial - id объекта хранения
     * @return - возвращает массив с историей изменения цены
     */
    List<ChangePriceEntity> getAllByIdMaterial(Long idMaterial);

    void saveAll(ChangePriceEntity changePriceEntity);
}
