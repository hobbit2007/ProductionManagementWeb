package com.vaadin.tutorial.crm.entity.for1c;

import com.vaadin.tutorial.crm.entity.powerresources.PowerResources;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.time.OffsetTime;
import java.util.Date;

/**
 * Класс модель описывающий структуру таблицы for1cerp
 */
@Entity(name = "for1cerp")
@Getter
@Setter
public class For1CEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long idOrderNum;
    private long delete;
    private Float value;
    private String units;
    private Date datetime;
    private long idSignal;

    @Transient
    String description;

    @NotFound(action = NotFoundAction.IGNORE)
    @ManyToOne
    @JoinColumn(name = "idSignal", referencedColumnName = "id", insertable = false, updatable = false)
    private For1CEntity parent;

    public For1CEntity() {}

    public For1CEntity(String description, For1CEntity parent) {
        this.description = description;
        this.parent = parent;
    }

    public For1CEntity(float value, Date datetime, long id, For1CEntity parent) {
        this.value = value;
        this.datetime = datetime;
        this.id = id;
        this.parent = parent;
    }
}