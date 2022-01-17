package com.vaadin.tutorial.crm.service.for1c;

import com.vaadin.tutorial.crm.entity.for1c.For1CSignalListEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс интерфейс содержащий методы для доступа к таблице for1csignallist
 */
@Service
public interface For1CSignalListService {

    List<For1CSignalListEntity> getAll();

    void saveAll(For1CSignalListEntity for1CSignalListEntity);

    /**
     * Метод помечающий запись как удаленную
     * @param id - id записи на удаление
     */
    void updateDeleteRecord(Long id);
}
