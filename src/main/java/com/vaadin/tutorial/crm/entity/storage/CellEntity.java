package com.vaadin.tutorial.crm.entity.storage;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * Класс модель описывающий поля таблицы cells
 */
@Entity(name = "cells")
@Getter
@Setter
public class CellEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String cellName;
    private long idStorage;
    private long idUser;
    private Date dateCreate;
    private long delete;
    private long idLocation;
}
