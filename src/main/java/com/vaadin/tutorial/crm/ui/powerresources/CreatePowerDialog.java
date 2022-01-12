package com.vaadin.tutorial.crm.ui.powerresources;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.tutorial.crm.entity.powerresources.PowerResourceDict;
import com.vaadin.tutorial.crm.entity.powerresources.PowerResources;
import com.vaadin.tutorial.crm.security.SecurityUtils;
import com.vaadin.tutorial.crm.service.powerresources.PowerResourceDictService;
import com.vaadin.tutorial.crm.service.powerresources.PowerResourcesService;
import com.vaadin.tutorial.crm.service.writetodb.WriteToDBService;
import com.vaadin.tutorial.crm.threads.UpdatePowerResource;
import com.vaadin.tutorial.crm.ui.component.AnyComponent;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Класс реализующий добавление показателей счетчиков энергоресурсов
 */
public class CreatePowerDialog extends Dialog {
    VerticalLayout vMain = new VerticalLayout();
    NumberField[] powerValue = new NumberField[100];
    FormLayout fContent = new FormLayout();
    HorizontalLayout hButton = new HorizontalLayout();
    HorizontalLayout hDateTime = new HorizontalLayout();
    List<PowerResourceDict> powerResourceDictList;
    List<NumberField> powerValueFieldLink = new ArrayList<>();
    DatePicker datePicker = new DatePicker(LocalDate.now());
    TimePicker timePicker = new TimePicker(LocalTime.now());
    ProgressBar progressBar = new ProgressBar();
    Thread updateValues = new Thread();

    double totalElectricity = 0.00;
    long coefficient1, coefficient2;
    int count = 0;//Счетчик для суммы вводов, т.к. ввода два и коэффициента трансформации два, то при count = 1 умножаем на coefficient1, а при count = 2 на coefficient2
    Button save, cancel;
    PowerResourceDictService powerResourceDictService;
    PowerResourcesService powerResourcesService;
    WriteToDBService writeToDBService;

    @Autowired
    public CreatePowerDialog(PowerResourceDictService powerResourceDictService, PowerResourcesService powerResourcesService,
                             WriteToDBService writeToDBService) {
        this.powerResourceDictService = powerResourceDictService;
        this.powerResourcesService = powerResourcesService;
        this.writeToDBService = writeToDBService;
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
        setDraggable(true);

        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);

        save = new Button("Сохранить");
        save.setEnabled(true);
        cancel = new Button("Отмена");
        cancel.setEnabled(true);
        hButton.add(save, cancel);

        datePicker.setLabel("Выберите дату:");
        datePicker.setI18n(new AnyComponent().datePickerRus());

        timePicker.setLabel("Выберите время:");
        timePicker.setValue(LocalTime.now());
        Locale locale = Locale.UK;
        timePicker.setLocale(locale);

        hDateTime.add(datePicker, timePicker);
        hDateTime.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);

        vMain.add(new AnyComponent().labelTitle("Добавить показания"), initPower(), hDateTime, progressBar, hButton);
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

        coefficient1 = writeToDBService.getAll().get(6).getRepeatTime();
        coefficient2 = writeToDBService.getAll().get(7).getRepeatTime();

        cancel.addClickListener(e -> {
           close();
        });
        save.addClickListener(e -> {
            powerResourceDictList = powerResourceDictService.getAll();

            if ((!powerValueFieldLink.get(2).isEmpty() && powerValueFieldLink.get(3).isEmpty()) || (!powerValueFieldLink.get(3).isEmpty() && powerValueFieldLink.get(2).isEmpty())) {
                Notification.show("Внимание! Поля Ввод №1 и Ввод №2 должны быть заполнены оба!", 5000, Notification.Position.MIDDLE);
                return;
            }

            save.setEnabled(false);
            cancel.setEnabled(false);

            if (powerResourceDictList.size() != 0) {
                for (int i = 0; i < powerResourceDictList.size(); i++) {
                    if (powerResourceDictList.get(i).getCategory() == 1 && !powerValueFieldLink.get(i).isEmpty()) {
                        count = count + 1;
                        if (count == 1)
                            totalElectricity = totalElectricity + powerValueFieldLink.get(i).getValue() * coefficient1;
                        if (count == 2)
                            totalElectricity = totalElectricity + powerValueFieldLink.get(i).getValue() * coefficient2;
                    }
                }
                powerValue[4].setValue(totalElectricity);
            }
            for (int i = 0; i < powerValueFieldLink.size(); i++) {
                try {
                    if (!powerValueFieldLink.get(i).isEmpty() && powerValueFieldLink.get(i).getValue() != 0) {
                        PowerResources powerResources = new PowerResources();
                        Date date = Date.valueOf(datePicker.getValue());
                        if (powerResourcesService.getCheckDate(date, powerResourceDictList.get(i).getId()).size() == 0) {
                            OffsetTime offsettime = OffsetTime.of(timePicker.getValue(), ZoneOffset.UTC);
                            powerResources.setIdPowerResource(powerResourceDictList.get(i).getId());
                            powerResources.setValue(powerValueFieldLink.get(i).getValue());
                            powerResources.setIdUser(SecurityUtils.getAuthentication().getDetails().getId());
                            powerResources.setDateCreate(date); //new Date(Calendar.getInstance().getTime().getTime())
                            powerResources.setDelete(0L);
                            powerResources.setTimeCreate(offsettime);

                            powerResourcesService.saveAll(powerResources);
                        }
                        else {
                            save.setEnabled(true);
                            cancel.setEnabled(true);
                            Notification.show("Показание на " + date + " для " + powerResourceDictList.get(i).getResourceName() +  " уже были внесены!", 5000, Notification.Position.MIDDLE);
                            return;
                        }
                    }
                }
                catch (Exception ex) {
                    Notification.show("Не могу сохранить показания в БД! " + ex.getMessage(), 5000, Notification.Position.MIDDLE);
                }
            }
            updateValues = new UpdatePowerResource(UI.getCurrent().getUI().get(), this, coefficient1, coefficient2, powerResourcesService);
            updateValues.start();
            progressBar.setVisible(true);
        });
    }

    private Component initPower() {
        fContent = new FormLayout();
        fContent.setWidth("731px");

        powerResourceDictList = powerResourceDictService.getAll();
        if (powerResourceDictList.size() != 0) {
            for (int i = 0; i < powerResourceDictList.size(); i++) {
                powerValue[i] = new NumberField(powerResourceDictList.get(i).getResourceName());
                powerValue[i].setWidth("191px");
                fContent.add(powerValue[i]);
                fContent.setResponsiveSteps(new FormLayout.ResponsiveStep("191px", 3));
                powerValueFieldLink.add(powerValue[i]);
            }
            powerValue[4].setVisible(false);//Поле Итого с id = 5 не показываем в интерфейсе
            powerValue[4].setValue(0.00);
        }
        return fContent;
    }
}
