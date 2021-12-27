package com.vaadin.tutorial.crm.entity.writetodb;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * Класс модель описывающий поля таблицы settings
 */
@Entity(name = "settings")
@Getter
@Setter
public class WriteToDB implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String description;
    private long writeOff;
    private long repeatTime;
    private long idController;
    private long idUser;
    private Date dateChange;
    private long delete;
}
