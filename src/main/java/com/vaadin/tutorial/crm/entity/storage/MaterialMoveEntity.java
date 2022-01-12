package com.vaadin.tutorial.crm.entity.storage;

import com.vaadin.tutorial.crm.entity.Department;
import com.vaadin.tutorial.crm.entity.Shop;
import com.vaadin.tutorial.crm.entity.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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

    @ManyToOne
    @JoinColumn(name = "idStorage", referencedColumnName = "id", insertable = false, updatable = false)
    private StorageEntity storage;

    @ManyToOne
    @JoinColumn(name = "idCell", referencedColumnName = "id", insertable = false, updatable = false)
    private CellEntity cell;

    @ManyToOne
    @JoinColumn(name = "idUser", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "idMaterial", referencedColumnName = "id", insertable = false, updatable = false)
    private MaterialInfoEntity material;

    @ManyToOne
    @JoinColumn(name = "idShop", referencedColumnName = "id", insertable = false, updatable = false)
    private Shop shop;

    @ManyToOne
    @JoinColumn(name = "idDepartment", referencedColumnName = "id", insertable = false, updatable = false)
    private Department department;
}
