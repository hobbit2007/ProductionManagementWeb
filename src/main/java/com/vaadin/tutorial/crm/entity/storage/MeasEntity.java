package com.vaadin.tutorial.crm.entity.storage;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * Класс модель описывающий поля таблицы measurement
 */
@Entity(name = "measurement")
@Getter
@Setter
public class MeasEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String measName;
    private long idUser;
    private Date dateCreate;
    private long delete;
}
