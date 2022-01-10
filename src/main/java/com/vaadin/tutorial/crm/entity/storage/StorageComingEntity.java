package com.vaadin.tutorial.crm.entity.storage;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Класс модель описывающий поля таблицы storage_coming
 */
@Entity(name = "storage_coming")
@Getter
@Setter
public class StorageComingEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long idMaterial;
    private double qtyCome;
    private long idMeas;
    private long idUser;
    private Date dateCreate;
    private long delete;
    private double qtyOldCome;
    private double balanceOld;
    private double balanceNew;

    @ManyToOne
    @JoinColumn(name = "idMeas", referencedColumnName = "id", insertable = false, updatable = false)
    private MeasEntity meas;

    @ManyToOne
    @JoinColumn(name = "idMaterial", referencedColumnName = "id", insertable = false, updatable = false)
    private MaterialInfoEntity materialInfo;
}
