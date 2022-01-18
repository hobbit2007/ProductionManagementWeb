package com.vaadin.tutorial.crm.ui.layout;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.tutorial.crm.security.SecurityConfiguration;
import com.vaadin.tutorial.crm.ui.MainView;
import com.vaadin.tutorial.crm.ui.component.LabelComponent;
import com.vaadin.tutorial.crm.ui.for1c.AddVariable;
import com.vaadin.tutorial.crm.ui.for1c.IntoToDB1C;
import com.vaadin.tutorial.crm.ui.powerresources.TableView;

/**
 * Класс реализующий шапку и боковое меню для меню Интеграция 1С
 */
public class For1CLayout extends AppLayout {
    private final SecurityConfiguration securityConfiguration;
    LabelComponent labelComponent;
    HorizontalLayout hMenu1 = new HorizontalLayout();
    HorizontalLayout hMenu2 = new HorizontalLayout();
    HorizontalLayout hMenu3 = new HorizontalLayout();

    public For1CLayout(SecurityConfiguration securityConfiguration) {
        this.securityConfiguration = securityConfiguration;

        createHeader();
        createDrawer();
    }
    private void createHeader() {
        labelComponent = new LabelComponent(securityConfiguration);
        addToNavbar(labelComponent.labelHead());
    }
    private void createDrawer() {
        RouterLink back = new RouterLink("Назад", MainView.class);
        back.setHighlightCondition(HighlightConditions.sameLocation());
        Icon icon1 = new Icon(VaadinIcon.ARROW_BACKWARD);
        hMenu1.add(icon1, back);

        RouterLink for1cView = new RouterLink("Проверка интеграции", IntoToDB1C.class);
        for1cView.setHighlightCondition(HighlightConditions.sameLocation());
        Icon icon2 = new Icon(VaadinIcon.EXTERNAL_LINK);
        hMenu2.add(icon2, for1cView);

        RouterLink addNewView = new RouterLink("Добавить переменную", AddVariable.class);
        addNewView.setHighlightCondition(HighlightConditions.sameLocation());
        Icon icon3 = new Icon(VaadinIcon.ADD_DOCK);
        hMenu3.add(icon3, addNewView);

        addToDrawer(new VerticalLayout(hMenu1, hMenu3, hMenu2));
    }

}
