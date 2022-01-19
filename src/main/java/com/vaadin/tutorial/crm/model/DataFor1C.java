package com.vaadin.tutorial.crm.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Класс модель для массива, хранящего данные из средних значений переменных ПЛК контроллеров и даты
 * для их отправки в 1С, с помощью API
 */
@Getter
@Setter
public class DataFor1C {
    private float variable;
    private Date dateTime;
}
