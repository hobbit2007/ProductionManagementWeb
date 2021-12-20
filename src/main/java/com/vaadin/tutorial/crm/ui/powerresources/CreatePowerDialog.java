package com.vaadin.tutorial.crm.ui.powerresources;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.tutorial.crm.entity.powerresources.PowerResourceDict;
import com.vaadin.tutorial.crm.entity.powerresources.PowerResources;
import com.vaadin.tutorial.crm.security.SecurityUtils;
import com.vaadin.tutorial.crm.service.powerresources.PowerResourceDictService;
import com.vaadin.tutorial.crm.service.powerresources.PowerResourcesService;
import com.vaadin.tutorial.crm.ui.component.AnyComponent;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Класс реализующий добавление показателей счетчиков энергоресурсов
 */
public class CreatePowerDialog extends Dialog {
    VerticalLayout vMain = new VerticalLayout();
    NumberField[] powerValue = new NumberField[100];
    FormLayout fContent = new FormLayout();
    HorizontalLayout hButton = new HorizontalLayout();
    List<PowerResourceDict> powerResourceDictList;
    List<NumberField> powerValueFieldLink = new ArrayList<>();
    /*HorizontalLayout hMain1 = new HorizontalLayout();
    HorizontalLayout hMain2 = new HorizontalLayout();
    HorizontalLayout hMain3 = new HorizontalLayout();
    HorizontalLayout hMain4 = new HorizontalLayout();
    HorizontalLayout hButton = new HorizontalLayout();
    NumberField waterWell = new NumberField("Арт, вода с скважины:");
    NumberField waterProduction = new NumberField("Арт вода на производство:");
    NumberField waterBoiler = new NumberField("Вода на котельную:");
    NumberField gas = new NumberField("Газ:");
    NumberField enter1 = new NumberField("Ввод 1(983):");
    NumberField enter2 = new NumberField("Ввод 2(740):");
    NumberField enter3 = new NumberField("Ввод 3(434):");
    NumberField enter4 = new NumberField("Ввод 4(457):");
    NumberField drains = new NumberField("Стоки:");*/
    double totalElectricity = 0.00;
    Button save, cancel;
    PowerResourceDictService powerResourceDictService;
    PowerResourcesService powerResourcesService;

    @Autowired
    public CreatePowerDialog(PowerResourceDictService powerResourceDictService, PowerResourcesService powerResourcesService) {
        this.powerResourceDictService = powerResourceDictService;
        this.powerResourcesService = powerResourcesService;
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
        setDraggable(true);
        /*waterWell.setRequiredIndicatorVisible(true);
        waterWell.setWidth("401px");
        waterProduction.setRequiredIndicatorVisible(true);
        waterProduction.setWidth("401px");
        waterBoiler.setRequiredIndicatorVisible(true);
        waterBoiler.setWidth("401px");
        gas.setRequiredIndicatorVisible(true);
        enter1.setRequiredIndicatorVisible(true);
        enter2.setRequiredIndicatorVisible(true);
        enter3.setRequiredIndicatorVisible(true);
        enter4.setRequiredIndicatorVisible(true);
        drains.setRequiredIndicatorVisible(true);

        hMain1.add(waterWell, waterProduction, waterBoiler);
        hMain2.add(gas);
        hMain3.add(enter1, enter2, enter3, enter4);
        hMain4.add(drains);
        hButton.add(save, cancel);
        vMain.add(new AnyComponent().labelTitle("Добавить показания"), hMain1, hMain2, hMain3, hMain4, hButton);*/
        save = new Button("Сохранить");
        cancel = new Button("Отмена");
        hButton.add(save, cancel);


        vMain.add(new AnyComponent().labelTitle("Добавить показания"), initPower(), hButton);
        vMain.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        add(vMain);

        Icon icon1 = new Icon(VaadinIcon.DISC);
        save.setIcon(icon1);
        save.getStyle().set("background-color", "#d3b342");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Icon icon2 = new Icon(VaadinIcon.CLOSE);
        cancel.setIcon(icon2);
        cancel.getStyle().set("background-color", "#d3b342");
        cancel.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        cancel.addClickListener(e -> {
           close();
        });
        save.addClickListener(e -> {
            powerResourceDictList = powerResourceDictService.getAll();
            for (int i = 0; i < powerValueFieldLink.size(); i++) {
                if (powerValueFieldLink.get(i).isEmpty()) {
                    Notification.show("Не все поля заполнены!", 5000, Notification.Position.MIDDLE);
                    return;
                }
            }
            if (powerResourceDictList.size() != 0) {
                for (int i = 0; i < powerResourceDictList.size(); i++) {
                    if (powerResourceDictList.get(i).getCategory() == 1) {
                        totalElectricity += powerValueFieldLink.get(i).getValue();
                    }
                }
            }
            for (int i = 0; i < powerValueFieldLink.size(); i++) {
                try {
                    PowerResources powerResources = new PowerResources();

                    powerResources.setIdPowerResource(powerResourceDictList.get(i).getId());
                    powerResources.setValue(powerValueFieldLink.get(i).getValue());
                    powerResources.setIdUser(SecurityUtils.getAuthentication().getDetails().getId());
                    powerResources.setDateCreate(new Date(Calendar.getInstance().getTime().getTime()));
                    powerResources.setDelete(0L);

                    powerResourcesService.saveAll(powerResources);
                    close();
                }
                catch (Exception ex) {
                    Notification.show("Не могу сохранить показания в БД! " + ex.getMessage(), 5000, Notification.Position.MIDDLE);
                }
            }
        });
    }

    private Component initPower() {
        fContent = new FormLayout();
        fContent.setWidth("651px");

        powerResourceDictList = powerResourceDictService.getAll();
        if (powerResourceDictList.size() != 0) {
            for (int i = 0; i < powerResourceDictList.size(); i++) {

                powerValue[i] = new NumberField(powerResourceDictList.get(i).getResourceName());
                powerValue[i].setWidth("121px");
                fContent.add(powerValue[i]);
                fContent.setResponsiveSteps(new FormLayout.ResponsiveStep("121px", 3));
                powerValueFieldLink.add(powerValue[i]);
            }
        }
        return fContent;
    }
}
