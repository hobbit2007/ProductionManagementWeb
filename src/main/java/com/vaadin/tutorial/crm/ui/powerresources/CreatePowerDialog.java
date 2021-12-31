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
            List<PowerResources> listWater = powerResourcesService.getAllByResourceId(1L);

            int indexWater = 0;
            if (listWater.size() != 0 && listWater.size() >= 2) {
                //if (listWater.size() <= 56) {
                for (int j = 0; j < listWater.size(); j++) {
                    indexWater += 1;
                    double varWater;
                    if (indexWater < listWater.size()) {
                        varWater = listWater.get(indexWater).getValue() - listWater.get(j).getValue();
                        //System.out.println("VALUE FOR UPDATE ID = " + listWater.get(indexWater).getId() + " VALUE = " + varWater);
                        powerResourcesService.updateValueWeekly(listWater.get(indexWater).getId(), varWater);
                    }
                }
                //}
                //if (listWater.size() > 56) {
                //    for (int j = listWater.size() - 1; j < 61; j--) {//Обрабатываем только часть массива содержащую 60 записей
                //        double varWater;
                //        if (j > 0) {
                //            varWater = listWater.get(j).getValue() - listWater.get(j - 1).getValue();
                //            System.out.println("VALUE FOR UPDATE2 ID = " + listWater.get(j).getId() + " VALUE = " + varWater);
                //        }
                //    }
                //}
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
            List<PowerResources> listGas = powerResourcesService.getAllByResourceId(4L);

            int indexGas = 0;
            double varGas = 0;
            if (listGas.size() != 0 && listGas.size() >= 2) {
                if (listGas.size() <= 56) {
                    for (int j = 0; j < listGas.size(); j++) {
                        indexGas += 1;
                        if (indexGas < listGas.size()) {
                            varGas += listGas.get(j).getValue();
                            if (indexGas % 7 == 0) {
                                //System.out.println("VALUE FOR UPDATE GAS ID = " + listGas.get(j).getId() + " VALUE GAS = " + varGas);
                                powerResourcesService.updateValueWeekly(listGas.get(j).getId(), varGas);
                                varGas = 0;
                            }
                        }
                    }
                }
                if (listGas.size() > 56 && listGas.size() % 7 == 0) {
                    int indexGas1 = listGas.size() - 56;
                    double varGas1 = 0;
                    for (int j = listGas.size() - 56; j < listGas.size(); j++) {//Обрабатываем только часть массива содержащую 60 записей
                        indexGas1 += 1;
                        if (indexGas1 < listGas.size()) {
                            varGas1 += listGas.get(indexGas1).getValue();//listGas.get(j).getValue() + listGas.get(j - 1).getValue();
                            if (j % 7 == 0) {
                                //System.out.println("VALUE FOR UPDATE2 GAS ID = " + listGas.get(j).getId() + " VALUE GAS = " + varGas1);
                                powerResourcesService.updateValueWeekly(listGas.get(j).getId(), varGas1);
                                varGas1 = 0;
                            }
                        }
                    }
                }
            }

            //Получаем разницу показаний текущей недели и предыдущей для газа
            List<PowerResources> listGasWeekly = powerResourcesService.getAllByResourceId(4L);

            int indexGasWeekly = 0;
            double varGasWeekly;
            if (listGasWeekly.size() != 0 && listGasWeekly.size() >= 2) {
                if (listGasWeekly.size() <= 56) {
                    for (int j = 0; j < listGasWeekly.size(); j++) {
                        indexGasWeekly += 1;
                        if (indexGasWeekly < listGasWeekly.size()) {
                            if (indexGasWeekly % 14 == 0) {
                                varGasWeekly = listGasWeekly.get(j).getValue() - listGasWeekly.get(j - 7).getValue();
                                //System.out.println("VALUE FOR UPDATE GAS WEEKLY ID = " + listGasWeekly.get(j).getId() + " VALUE GAS WEEKLY = " + varGasWeekly);
                                powerResourcesService.updateTotalValueWeekly(listGasWeekly.get(j).getId(), varGasWeekly);
                                varGasWeekly = 0;
                            }
                        }
                    }
                }
                if (listGasWeekly.size() > 56 && listGasWeekly.size() % 14 == 0) {
                    int indexGasWeekly1 = listGasWeekly.size() - 56;
                    double varGasWeekly1 = 0;
                    for (int j = listGasWeekly.size() - 56; j < listGasWeekly.size(); j++) {//Обрабатываем только часть массива содержащую 60 записей
                        indexGasWeekly1 += 1;
                        if (indexGasWeekly1 < listGasWeekly.size()) {
                            varGasWeekly1 += listGasWeekly.get(indexGasWeekly1).getValue();//listGas.get(j).getValue() + listGas.get(j - 1).getValue();
                            if (j % 14 == 0) {
                                //System.out.println("VALUE FOR UPDATE2 GAS ID = " + listGasWeekly.get(j).getId() + " VALUE GAS = " + varGasWeekly1);
                                powerResourcesService.updateTotalValueWeekly(listGasWeekly.get(j).getId(), varGasWeekly1);
                                varGasWeekly1 = 0;
                            }
                        }
                    }
                }
            }

            //Получаем разницу показаний между текущими и предыдущими для стоков
            List<PowerResources> listStock = powerResourcesService.getAllByResourceId(10L);

            int indexStock = 0;
            if (listStock.size() != 0 && listStock.size() >= 2) {
                //if (listWater.size() <= 56) {
                for (int j = 0; j < listStock.size(); j++) {
                    indexStock += 1;
                    double varStock;
                    if (indexStock < listStock.size()) {
                        varStock = listStock.get(indexStock).getValue() - listStock.get(j).getValue();
                        //System.out.println("VALUE FOR UPDATE ID = " + listWater.get(indexWater).getId() + " VALUE = " + varWater);
                        powerResourcesService.updateValueWeekly(listStock.get(indexStock).getId(), varStock);
                    }
                }
            }
            /*List<PowerResources> listStock = powerResourcesService.getAllByResourceId(10L);
            double varStock = 0;
            if (listStock.size() != 0 && listStock.size() >= 2) {
                for (int j = listStock.size() - 1; j >= listStock.size() - 2; j--) {
                    varStock = listStock.get(j).getValue() - varStock;
                }
                powerResourcesService.updateValueWeekly(listStock.get(listStock.size() - 1).getId(), varStock * -1);
            }*/

            //Получаем разницу показаний между текущими и предыдущими для ввода №1
            List<PowerResources> listEnter1 = powerResourcesService.getAllByResourceId(5L);

            int indexEnter1 = 0;
            if (listEnter1.size() != 0 && listEnter1.size() >= 2) {
                //if (listEnter1.size() < 61) {
                for (int j = 0; j < listEnter1.size(); j++) {
                    indexEnter1 += 1;
                    double varEnter1;
                    if (indexEnter1 < listEnter1.size()) {
                        varEnter1 = listEnter1.get(indexEnter1).getValue() - listEnter1.get(j).getValue();
                        //System.out.println("VALUE FOR UPDATE ENTER1 ID = " + listEnter1.get(indexEnter1).getId() + " VALUE ENTER1 = " + varEnter1 * coefficient1);
                        powerResourcesService.updateValueWeekly(listEnter1.get(indexEnter1).getId(), varEnter1 * coefficient1);
                    }
                }
                //}
                //if (listEnter1.size() > 61) {
                //    for (int j = listEnter1.size() - 1; j < 61; j--) {//Обрабатываем только часть массива содержащую 60 записей
                //        double varEnter1;
                //        if (j > 0) {
                //            varEnter1 = listEnter1.get(j).getValue() - listEnter1.get(j - 1).getValue();
                //            System.out.println("VALUE FOR UPDATE2 ID = " + listEnter1.get(j).getId() + " VALUE = " + varEnter1 * coefficient1);
                //        }
                //    }
                //}
                //for (int j = listEnter1.size() - 1; j >= listEnter1.size() - 2; j--) {
                //    varEnter1 = listEnter1.get(j).getValue() - varEnter1;
                //}
                //powerResourcesService.updateValueWeekly(listEnter1.get(listEnter1.size() - 1).getId(), varEnter1 * coefficient1 * -1);
            }

            //Получаем разницу показаний между текущими и предыдущими для ввода №2
            List<PowerResources> listEnter2 = powerResourcesService.getAllByResourceId(6L);

            int indexEnter2 = 0;
            if (listEnter2.size() != 0 && listEnter2.size() >= 2) {
                //if (listEnter1.size() < 61) {
                for (int j = 0; j < listEnter2.size(); j++) {
                    indexEnter2 += 1;
                    double varEnter2;
                    if (indexEnter2 < listEnter2.size()) {
                        varEnter2 = listEnter2.get(indexEnter2).getValue() - listEnter2.get(j).getValue();
                        //System.out.println("VALUE FOR UPDATE ENTER2 ID = " + listEnter2.get(indexEnter2).getId() + " VALUE ENTER2 = " + varEnter2 * coefficient2);
                        powerResourcesService.updateValueWeekly(listEnter2.get(indexEnter2).getId(), varEnter2 * coefficient2);
                    }
                }
            }

            //Получаем разницу показаний между текущими и предыдущими для суммарной электроэнергии
            List<PowerResources> listTotalElectric = powerResourcesService.getAllByResourceId(9L);

            int indexTotalElectric = 0;
            if (listTotalElectric.size() != 0 && listTotalElectric.size() >= 2) {
                //if (listEnter1.size() < 61) {
                for (int j = 0; j < listTotalElectric.size(); j++) {
                    indexTotalElectric += 1;
                    double varTotalElectric = 0;
                    if (indexTotalElectric < listTotalElectric.size()) {
                        varTotalElectric = listTotalElectric.get(indexTotalElectric).getValue() - listTotalElectric.get(j).getValue();
                        //System.out.println("VALUE FOR UPDATE TOTAL ELECTRIC ID = " + listTotalElectric.get(indexTotalElectric).getId() + " VALUE TOTAL = " + varTotalElectric);
                        powerResourcesService.updateTotalValueWeekly(listTotalElectric.get(indexTotalElectric).getId(), varTotalElectric);
                        powerResourcesService.updateValueWeekly(listTotalElectric.get(indexTotalElectric).getId(), varTotalElectric);
                    }
                }
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
