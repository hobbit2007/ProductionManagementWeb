package com.vaadin.tutorial.crm.ui.for1c;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.service.writetodb.WriteToDBService;
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
    HorizontalLayout hMain1 = new HorizontalLayout();
    Label alarmWashing = new Label();
    Label header1Washing = new Label();
    Label timeLabel = new Label();
    NumberField timeWashing = new NumberField();
    RadioButtonGroup<String> radioButtonWashing = new RadioButtonGroup<>();
    private final WriteToDBService writeToDBService;

    public IntoToDB1C(WriteToDBService writeToDBService) {
        this.writeToDBService = writeToDBService;
        radioButtonWashing.setItems("Нет", "Да");
        radioButtonWashing.getStyle().set("color", "#d3b342");
        radioButtonWashing.setValue(writeToDBService.getAll().get(8).getWriteOff());

        header1Washing.setText(writeToDBService.getAll().get(8).getDescription());
        header1Washing.getStyle().set("color", "#d3b342");
        header1Washing.getStyle().set("font-weight", "bold");
        header1Washing.getStyle().set("font-size", "13pt");
        header1Washing.getStyle().set("margin-left", "30px");

        timeLabel.setText("Время повтора, мин.:");
        timeLabel.getStyle().set("color", "#d3b342");
        timeLabel.getStyle().set("font-weight", "bold");
        timeLabel.getStyle().set("font-size", "13pt");

        timeWashing.setWidth("45px");
        timeWashing.setValue((double) writeToDBService.getAll().get(8).getRepeatTime());

        if (radioButtonWashing.getValue().equals("Нет")) {
            alarmDisable(alarmWashing);
        }
        else {
            alarmEnabled(alarmWashing);
        }

        radioButtonWashing.addValueChangeListener(e -> {
            if (e.getValue().equals("Да")) {
                alarmEnabled(alarmWashing);
            }
            if (e.getValue().equals("Нет")) {
                alarmDisable(alarmWashing);
            }
        });

        hMain1.add(header1Washing, radioButtonWashing, timeLabel, timeWashing, alarmWashing);
        hMain1.setVerticalComponentAlignment(Alignment.BASELINE);
        vMain.add(new AnyComponent().labelTitle("Управление записью в БД для 1С", "#d3b342", "15pt"), hMain1);
        vMain.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        add(vMain);
    }
    private void alarmDisable(Label label) {
        label.setText("Запись отключена");
        label.getStyle().set("color", "red");
        label.getStyle().set("font-weight", "bold");
        label.getStyle().set("font-size", "13pt");
    }
    private void alarmEnabled(Label label) {
        label.setText("Запись включена");
        label.getStyle().set("color", "green");
        label.getStyle().set("font-weight", "bold");
        label.getStyle().set("font-size", "13pt");
    }
}
