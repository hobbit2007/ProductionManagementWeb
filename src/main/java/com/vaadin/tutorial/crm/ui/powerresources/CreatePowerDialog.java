package com.vaadin.tutorial.crm.ui.powerresources;

import com.vaadin.flow.component.Component;
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
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.tutorial.crm.entity.powerresources.PowerResourceDict;
import com.vaadin.tutorial.crm.entity.powerresources.PowerResources;
import com.vaadin.tutorial.crm.security.SecurityUtils;
import com.vaadin.tutorial.crm.service.powerresources.PowerResourceDictService;
import com.vaadin.tutorial.crm.service.powerresources.PowerResourcesService;
import com.vaadin.tutorial.crm.service.writetodb.WriteToDBService;
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

        vMain.add(new AnyComponent().labelTitle("Добавить показания"), initPower(), hDateTime, hButton);
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
            save.setEnabled(false);
            cancel.setEnabled(false);
            powerResourceDictList = powerResourceDictService.getAll();

            if (powerResourceDictList.size() != 0) {
                for (int i = 0; i < powerResourceDictList.size(); i++) {
                    if (powerResourceDictList.get(i).getCategory() == 1 && !powerValueFieldLink.get(i).isEmpty()) {
                        count = count + 1;
                        if (count == 1)
                            totalElectricity = totalElectricity + powerValueFieldLink.get(i).getValue() * coefficient1;
                        if (count == 2)
                            totalElectricity = totalElectricity + powerValueFieldLink.get(i).getValue() * coefficient1;
                    }
                }
                powerValue[4].setValue(totalElectricity);
            }
            for (int i = 0; i < powerValueFieldLink.size(); i++) {
                try {
                    if (!powerValueFieldLink.get(i).isEmpty() && powerValueFieldLink.get(i).getValue() != 0) {
                        PowerResources powerResources = new PowerResources();
                        Date date = Date.valueOf(datePicker.getValue());
                        OffsetTime offsettime = OffsetTime.of(timePicker.getValue(), ZoneOffset.UTC);
                        powerResources.setIdPowerResource(powerResourceDictList.get(i).getId());
                        powerResources.setValue(powerValueFieldLink.get(i).getValue());
                        powerResources.setIdUser(SecurityUtils.getAuthentication().getDetails().getId());
                        powerResources.setDateCreate(date); //new Date(Calendar.getInstance().getTime().getTime())
                        powerResources.setDelete(0L);
                        powerResources.setTimeCreate(offsettime);

                        powerResourcesService.saveAll(powerResources);
                    }
                }
                catch (Exception ex) {
                    Notification.show("Не могу сохранить показания в БД! " + ex.getMessage(), 5000, Notification.Position.MIDDLE);
                }
            }
            //Получаем ежедневную разницу показаний между текущими и предыдущими для воды
            List<PowerResources> waterList = powerResourcesService.getAllByResourceId(1L);
            double varWater = 0;
            if (waterList.size() != 0 && waterList.size() >= 2) {
                for (int j = waterList.size() - 1; j >= waterList.size() - 2; j--) {
                    varWater = waterList.get(j).getValue() - varWater;
                }
                powerResourcesService.updateValueWeekly(waterList.get(waterList.size() - 1).getId(), varWater * -1);
            }
            //Получаем ежедневную разницу показаний между текущими и предыдущими для газа
            //List<PowerResources> gasList = powerResourcesService.getAllByResourceId(4L);
            //double varGas = 0;
            //if (gasList.size() != 0 && gasList.size() >= 2) {
            //    for (int j = gasList.size() - 1; j >= gasList.size() - 2; j--) {
            //        varGas = gasList.get(j).getValue() - varGas;
            //    }
            //    powerResourcesService.updateValueDaily(gasList.get(gasList.size() - 1).getId(), varGas * -1);
            //}
            //Получаем еженедельную сумму показаний для газа
            List<PowerResources> gasListWeekly = powerResourcesService.getAllByResourceId(4L);
            double varGasWeekly = 0;
            if (gasListWeekly.size() != 0 && gasListWeekly.size() % 7 == 0) {
                for (int j = gasListWeekly.size() - 1; j >= gasListWeekly.size() - 7; j--) {
                    varGasWeekly += gasListWeekly.get(j).getValue();
                }
                powerResourcesService.updateValueWeekly(gasListWeekly.get(gasListWeekly.size() - 1).getId(), varGasWeekly);
            }
            //Получаем разницу показаний текущей недели и предыдущей для газа
            List<PowerResources> gasListTotalWeekly = powerResourcesService.getAllByResourceId(4L);
            double varGasTotalWeekly = 0;
            if (gasListTotalWeekly.size() != 0 && gasListTotalWeekly.size() % 14 == 0) {
                for (int j = gasListTotalWeekly.size() - 1; j >= gasListTotalWeekly.size() - 14; j--) {
                    varGasTotalWeekly = gasListTotalWeekly.get(j).getValueWeekly() - varGasTotalWeekly;
                }
                powerResourcesService.updateTotalValueWeekly(gasListTotalWeekly.get(gasListTotalWeekly.size() - 1).getId(), varGasTotalWeekly * -1);
            }
            //Получаем разницу показаний между текущими и предыдущими для стоков
            List<PowerResources> listStock = powerResourcesService.getAllByResourceId(10L);
            double varStock = 0;
            if (listStock.size() != 0 && listStock.size() >= 2) {
                for (int j = listStock.size() - 1; j >= listStock.size() - 2; j--) {
                    varStock = listStock.get(j).getValue() - varStock;
                }
                powerResourcesService.updateValueWeekly(listStock.get(listStock.size() - 1).getId(), varStock * -1);
            }
            //Получаем разницу показаний между текущими и предыдущими для ввода №1
            List<PowerResources> listEnter1 = powerResourcesService.getAllByResourceId(5L);
            double varEnter1 = 0;
            if (listEnter1.size() != 0 && listEnter1.size() >= 2) {
                for (int j = listEnter1.size() - 1; j >= listEnter1.size() - 2; j--) {
                    varEnter1 = listEnter1.get(j).getValue() - varEnter1;
                }
                powerResourcesService.updateValueWeekly(listEnter1.get(listEnter1.size() - 1).getId(), varEnter1 * coefficient1 * -1);
            }
            //Получаем разницу показаний между текущими и предыдущими для ввода №2
            List<PowerResources> listEnter2 = powerResourcesService.getAllByResourceId(6L);
            double varEnter2 = 0;
            if (listEnter2.size() != 0 && listEnter2.size() >= 2) {
                for (int j = listEnter2.size() - 1; j >= listEnter2.size() - 2; j--) {
                    varEnter2 = listEnter2.get(j).getValue() - varEnter2;
                }
                powerResourcesService.updateValueWeekly(listEnter2.get(listEnter2.size() - 1).getId(), varEnter2 * coefficient2 * -1);
            }
            //Получаем разницу показаний между текущими и предыдущими для суммарной электроэнергии
            List<PowerResources> listTotalElectric = powerResourcesService.getAllByResourceId(9L);
            double varTotalElectric = 0;
            if (listTotalElectric.size() != 0 && listTotalElectric.size() >= 2) {
                for (int j = listTotalElectric.size() - 1; j >= listTotalElectric.size() - 2; j--) {
                    varTotalElectric = listTotalElectric.get(j).getValue() - varTotalElectric;
                }
                powerResourcesService.updateTotalValueWeekly(listTotalElectric.get(listTotalElectric.size() - 1).getId(), varTotalElectric * -1);
                powerResourcesService.updateValueWeekly(listTotalElectric.get(listTotalElectric.size() - 1).getId(), varTotalElectric * -1);
            }

            close();
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
