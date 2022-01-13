package com.vaadin.tutorial.crm.entity.storage;

import com.vaadin.tutorial.crm.entity.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Класс модель описывающий структуру таблицы price_change
 */
@Entity(name = "price_change")
@Getter
@Setter
public class ChangePriceEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long idMaterial;
    private double costPriceOld;
    private double costPriceNew;
    private double marketPriceOld;
    private double marketPriceNew;
    private long idUserCreate;
    private Date dateCreate;
    private long delete;

    @ManyToOne
    @JoinColumn(name = "idUserCreate", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "idMaterial", referencedColumnName = "id", insertable = false, updatable = false)
    private MaterialInfoEntity material;
}
