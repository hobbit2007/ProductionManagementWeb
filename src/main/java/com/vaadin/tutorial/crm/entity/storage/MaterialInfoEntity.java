package com.vaadin.tutorial.crm.entity.storage;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Класс модель описывающий поля таблицы materialinfo
 */
@Entity(name = "materialinfo")
@Getter
@Setter
public class MaterialInfoEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long idStorage;
    private long idCell;
    private String materialName;
    private String article;
    private double quantity;
    private long idMeas;
    private double expense;
    private double balance;
    private Date dateCreate;
    private long idUser;
    private long writeoff;
    private double costPrice;
    private double marketPrice;
    private double diffPrice;
    private long flagMove;
    private long delete;

    @ManyToOne
    @JoinColumn(name = "idStorage", referencedColumnName = "id", insertable = false, updatable = false)
    private StorageEntity storage;

    @ManyToOne
    @JoinColumn(name = "idCell", referencedColumnName = "id", insertable = false, updatable = false)
    private CellEntity cell;

    @ManyToOne
    @JoinColumn(name = "idMeas", referencedColumnName = "id", insertable = false, updatable = false)
    private MeasEntity meas;
}
