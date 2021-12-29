package com.vaadin.tutorial.crm.ui.powerresources;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.service.writetodb.WriteToDBService;
import com.vaadin.tutorial.crm.ui.component.AnyComponent;
import com.vaadin.tutorial.crm.ui.layout.PowerLayout;

/**
 * Класс позволяющий обновлять коэффициенты трансформации
 */
@Route(value = "changecoeff", layout = PowerLayout.class)
@PageTitle("Редактирование коэффициента трансформации | Система управления производством")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class ChangeCoefficient extends Dialog {
    VerticalLayout vMain = new VerticalLayout();
    HorizontalLayout hMain1 = new HorizontalLayout();
    Button save = new Button("Сохранить");
    Button cancel = new Button("Закрыть");
    NumberField enter1 = new NumberField("Коэфф. Ввод №1");
    NumberField enter2 = new NumberField("Коэфф. Ввод №2");
    private final WriteToDBService writeToDBService;

    public ChangeCoefficient(WriteToDBService writeToDBService) {

        this.writeToDBService = writeToDBService;

        enter1.setValue((double) writeToDBService.getAll().get(6).getRepeatTime());
        enter2.setValue((double) writeToDBService.getAll().get(7).getRepeatTime());

        save.getStyle().set("background-color", "#d3b342");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancel.getStyle().set("background-color", "#d3b342");
        cancel.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        hMain1.add(save, cancel);

        vMain.add(new AnyComponent().labelTitle("Редактировать коэфф. трансформации"), enter1, enter2, hMain1);
        vMain.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        add(vMain);

        cancel.addClickListener(e -> {
            close();
        });
        save.addClickListener(e -> {
           if (enter1.isEmpty() && enter2.isEmpty()) {
               try {
                   writeToDBService.updateCoefficient1(enter1.getValue());
                   writeToDBService.updateCoefficient2(enter2.getValue());
               }
               catch (Exception ex) {
                   Notification.show("Не могу обновить коэфф. трансформации!", 5000, Notification.Position.MIDDLE);
                   return;
               }
           }
           else {
               Notification.show("Не все поля заполнены!", 5000, Notification.Position.MIDDLE);
               return;
           }
        });
    }
}
