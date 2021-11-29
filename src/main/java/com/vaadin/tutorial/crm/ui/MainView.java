package com.vaadin.tutorial.crm.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.ui.layout.MainLayout;

/**
 * Главный класс приложения
 */
@Route(value = "", layout = MainLayout.class)
@PageTitle("Dashboard | Система управления производством")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class MainView extends AppLayout {

    Label labelUser;
    VerticalLayout vContent = new VerticalLayout();
    HorizontalLayout hContent = new HorizontalLayout();

    public MainView() {
        labelUser = new Label("ЗДЕСЬ БУДЕТ КОНТЕНТ ПРИЛОЖЕНИЯ");
        labelUser.getStyle().set("color", "red");
        labelUser.getStyle().set("font-weight", "bold");
        labelUser.getStyle().set("font-size", "11pt");
        labelUser.getStyle().set("border", "1px inset blue");

        hContent.add(labelUser);

        vContent.add(hContent);
        vContent.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        vContent.setSizeFull();


        setContent(vContent);

    }

}