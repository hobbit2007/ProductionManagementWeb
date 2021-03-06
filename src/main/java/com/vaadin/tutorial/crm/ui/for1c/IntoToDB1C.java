package com.vaadin.tutorial.crm.ui.for1c;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.service.for1c.For1CService;
import com.vaadin.tutorial.crm.service.for1c.For1CSignalListService;
import com.vaadin.tutorial.crm.service.plccontrollersservice.PLCConnect;
import com.vaadin.tutorial.crm.service.writetodb.WriteToDBService;
import com.vaadin.tutorial.crm.threads.StartRecordFor1C;
import com.vaadin.tutorial.crm.ui.component.AnyComponent;
import com.vaadin.tutorial.crm.ui.layout.For1CLayout;

/**
 * Класс реализующий запись данных в БД для экспорта в 1С, а также настройку времени записи
 */
@Route(value = "integration1c", layout = For1CLayout.class)
@PageTitle("Интеграция с 1С | Система управления производством")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class IntoToDB1C extends VerticalLayout {
    VerticalLayout vMain = new VerticalLayout();

    Label alarm1С = new Label();
    Label header1С = new Label();
    Label timeLabel = new Label();
    NumberField time1С = new NumberField();
    RadioButtonGroup<String> radioButton1С = new RadioButtonGroup<>();
    Thread recordToDB = new Thread();

    public IntoToDB1C(WriteToDBService writeToDBService, For1CSignalListService for1CSignalListService, For1CService for1CService) {
        radioButton1С.setItems("Нет", "Да");
        radioButton1С.getStyle().set("color", "#d3b342");
        radioButton1С.setValue(writeToDBService.getAll().get(8).getWriteOff());

        header1С.setText(writeToDBService.getAll().get(8).getDescription());
        header1С.getStyle().set("color", "#d3b342");
        header1С.getStyle().set("font-weight", "bold");
        header1С.getStyle().set("font-size", "13pt");
        header1С.getStyle().set("margin-left", "30px");

        timeLabel.setText("Время повтора, мин.:");
        timeLabel.getStyle().set("color", "#d3b342");
        timeLabel.getStyle().set("font-weight", "bold");
        timeLabel.getStyle().set("font-size", "13pt");

        time1С.setWidth("45px");
        time1С.setValue((double) writeToDBService.getAll().get(8).getRepeatTime());

        if (radioButton1С.getValue().equals("Нет")) {
            alarmDisable(alarm1С);
        }
        else {
            alarmEnabled(alarm1С);
        }

        radioButton1С.addValueChangeListener(e -> {
            if (e.getValue().equals("Да")) {
                alarmEnabled(alarm1С);
                writeToDBService.updateWriteFor1C("Да");

                recordToDB = new StartRecordFor1C(for1CSignalListService.getAll(), PLCConnect.clientForStatusWashing, PLCConnect.clientForStatusDiffusion,
                         PLCConnect.clientForStatusFermentation, PLCConnect.clientForStatus, PLCConnect.clientForStatusBottling,
                        PLCConnect.clientForStatusDrying, time1С.getValue().longValue() * 60 * 1000, for1CService);
                recordToDB.start();
            }
            if (e.getValue().equals("Нет")) {
                recordToDB.interrupt();
                recordToDB = null;
                alarmDisable(alarm1С);
                writeToDBService.updateWriteFor1C("Нет");
            }
        });

        time1С.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                if (!time1С.isEmpty())
                    writeToDBService.updateRTFor1C(time1С.getValue().longValue());
           }
        });

        FormLayout formLayout = new FormLayout();
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 5));
        formLayout.add(header1С, radioButton1С, timeLabel, time1С, alarm1С);

        vMain.add(new AnyComponent().labelTitle("Управление записью в БД для 1С", "#d3b342", "15pt"), formLayout);
        vMain.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        add(vMain);
    }
    private void alarmDisable(Label label) {
        label.setText(" - Запись в БД отключена");
        label.getStyle().set("color", "red");
        label.getStyle().set("font-weight", "bold");
        label.getStyle().set("font-size", "13pt");
    }
    private void alarmEnabled(Label label) {
        label.setText(" - Запись в БД включена");
        label.getStyle().set("color", "green");
        label.getStyle().set("font-weight", "bold");
        label.getStyle().set("font-size", "13pt");
    }
}
