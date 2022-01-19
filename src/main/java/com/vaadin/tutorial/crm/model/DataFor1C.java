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
    private String variableName;
    private String variableDescription;
    private float value;
    private Date dateTime;

    public DataFor1C (String variableName, String variableDescription, float value, Date dateTime) {
        this.variableName = variableName;
        this.variableDescription = variableDescription;
        this.value = value;
        this.dateTime = dateTime;
    }
}
