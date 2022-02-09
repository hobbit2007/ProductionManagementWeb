package com.vaadin.tutorial.crm.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Класс модель описывающий поля таблицы history
 */
@Entity(name = "history")
@Getter
@Setter
public class HistoryEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idUser;
    private String action;
    private Date createRecordDate;
    private Long delete;
    private String place;
}
