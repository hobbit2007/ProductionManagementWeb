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
 * Класс модель описывающий поля таблицы material_move
 */
@Entity(name = "material_move")
@Getter
@Setter
public class MaterialMoveEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long idMaterial;
    private long idStorageOld;
    private long idStorageNew;
    private long idCellOld;
    private long idCellNew;
    private double expense;
    private long idShop;
    private long idDepartment;
    private long idUser;
    private String description;
    private long writeoff;
    private String action;
    private String forWhom;
    private long idUserCreate;
    private Date dateCreate;
    private long delete;
}
