package com.vaadin.tutorial.crm.entity.for1c;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Класс модель описывающий структуру таблицы for1cerp
 */
@Entity(name = "for1cerp")
@Getter
@Setter
public class For1CEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long idOrderNum;
    private long delete;
    private Float value;
    private String units;
    private Date datetime;
}