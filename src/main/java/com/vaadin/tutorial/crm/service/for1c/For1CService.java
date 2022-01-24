package com.vaadin.tutorial.crm.service.for1c;

import com.vaadin.tutorial.crm.entity.for1c.For1CEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Класс интерфейс содержащий методы для работы с таблицей for1cerp
 */
@Service
public interface For1CService {
    List<For1CEntity> getAll();

    /**
     * Метод для записи данных ПЛК контроллеров для их последующей вкпкдачи в 1С
     * @param for1CEntity - объект класс For1CEntity
     */
    @Transactional
    void saveAll(For1CEntity for1CEntity);

    /**
     * Метод для обновления поля delete после того как данные были переданы в 1С считаем их удаленными
     */
    void updateAfterLoadTo1C();
}
