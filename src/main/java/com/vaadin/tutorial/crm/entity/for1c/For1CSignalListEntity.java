package com.vaadin.tutorial.crm.entity.for1c;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.util.Date;

/**
 * Класс модель описывающий структуру таблицы for1csignallist
 */
@Entity(name = "for1csignallist")
@Getter
@Setter
public class For1CSignalListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private long idSignalName;
    private long idUser;
    private Date dateCreate;
    private long delete;
}