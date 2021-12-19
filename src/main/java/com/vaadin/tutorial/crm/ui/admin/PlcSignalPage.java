package com.vaadin.tutorial.crm.ui.admin;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.ui.layout.PlcSignalLayout;

/**
 * Класс реализующий страницу с действиями над переменными ПЛК контроллеров
 */
@Route(value = "plcsignalpage", layout = PlcSignalLayout.class)
@PageTitle("Переменные ПЛК контроллеров | Система управления производством")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class PlcSignalPage extends VerticalLayout{
    VerticalLayout vContent = new VerticalLayout();
    public PlcSignalPage() {
        init();
    }
    private void init() {
        add(vContent);
        vContent.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        vContent.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        vContent.setSizeFull();
    }
}
