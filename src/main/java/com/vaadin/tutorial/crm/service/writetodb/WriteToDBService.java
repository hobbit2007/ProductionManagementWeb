package com.vaadin.tutorial.crm.service.writetodb;

import com.vaadin.tutorial.crm.entity.writetodb.WriteToDB;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс интерфейс содержащий методы для работы с таблицей settings
 */
@Service
public interface WriteToDBService {
    List<WriteToDB> getAll();

    //Обновления коэффициентов трансформации ввод1
    void updateCoefficient1(Double value);

    //Обновления коэффициентов трансформации ввод2
    void updateCoefficient2(Double value);

    /**
     * Метод для обновления, времени повтора, записи данных в БД для 1С
     * @param value - значение времени повтора в минутах
     */
    void updateRTFor1C(Long value);

    /**
     * Метод для обновления режима включения/выключения записи в БД
     * @param value - Запись включена, значение: Да, запись выключена, значение: Нет
     */
    void updateWriteFor1C(String value);
}
