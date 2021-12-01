package com.vaadin.tutorial.crm.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Класс модель для массива, хранящего данные из ПЛК контроллера
 * для их визуализации в реальном времени
 */
@Getter
@Setter
public class DataFromPlc {
    private String signalName;
    private float value;
}
