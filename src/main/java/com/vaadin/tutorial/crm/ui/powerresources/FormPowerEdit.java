package com.vaadin.tutorial.crm.ui.powerresources;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToDateConverter;
import com.vaadin.flow.data.converter.StringToDoubleConverter;
import com.vaadin.tutorial.crm.entity.powerresources.PowerResources;

/**
 * Класс форма реализующий редактирование показаний
 */
public class FormPowerEdit extends FormLayout {
    TextField value = new TextField("Показания:");
    TextField dateCreate = new TextField("Дата снятия:");
    TextField timeCreate = new TextField("Время снятия:");
    Binder<PowerResources> binder = new Binder<>(PowerResources.class);
    PowerResources powerResources = new PowerResources();

    public FormPowerEdit() {

        addClassName("contact-form");
        binder.forField(dateCreate).withConverter(new StringToDateConverter()).bind(PowerResources::getDateCreate, null);
        binder.forField(value).withConverter(new StringToDoubleConverter("")).bind(PowerResources::getValue, null);
        binder.bindInstanceFields(this);

        dateCreate.setReadOnly(true);

        add(value, dateCreate, timeCreate);
    }
    public void setPowerRes(PowerResources powerResources) {
        this.powerResources = powerResources;
        binder.readBean(powerResources);
    }
}
