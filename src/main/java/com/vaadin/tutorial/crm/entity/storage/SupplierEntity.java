package com.vaadin.tutorial.crm.entity.storage;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Класс модель содержит описание полей таблицы supplier
 */
@Entity(name = "supplier")
@Getter
@Setter
public class SupplierEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String supplierName;
    private String contract;
    private long idUser;
    private Date dateCreate;
    private long delete;
}
