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
    private long idLocationOld;
    private long idLocationNew;

    @ManyToOne
    @JoinColumn(name = "idLocationOld", referencedColumnName = "id", insertable = false, updatable = false)
    private LocationEntity locationOld;
    @ManyToOne
    @JoinColumn(name = "idStorageOld", referencedColumnName = "id", insertable = false, updatable = false)
    private StorageEntity storageOld;
    @ManyToOne
    @JoinColumn(name = "idLocationNew", referencedColumnName = "id", insertable = false, updatable = false)
    private LocationEntity locationNew;
    @ManyToOne
    @JoinColumn(name = "idStorageNew", referencedColumnName = "id", insertable = false, updatable = false)
    private StorageEntity storageNew;

    @ManyToOne
    @JoinColumn(name = "idCellOld", referencedColumnName = "id", insertable = false, updatable = false)
    private CellEntity cellOld;
    @ManyToOne
    @JoinColumn(name = "idCellNew", referencedColumnName = "id", insertable = false, updatable = false)
    private CellEntity cellNew;

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
