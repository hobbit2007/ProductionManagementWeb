package com.vaadin.tutorial.crm.ui.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.tutorial.crm.security.SecurityConfiguration;
import com.vaadin.tutorial.crm.security.SecurityUtils;

/**
 * Класс содержит реализации компонентов, которые чаще всего используются в приложении
 */
public class LabelComponent extends VerticalLayout {
    private Label labelTitle, logo, labelHead;
    private HorizontalLayout userSeparator, header;
    private final SecurityConfiguration securityConfiguration;
    private DrawerToggle drawerToggle = new DrawerToggle();

    public LabelComponent(SecurityConfiguration securityConfiguration) {

        this.securityConfiguration = securityConfiguration;
    }

    /**
     * Метод содержит визуальные компоненты для построения титульной шапки приложения
     * @return - возвращает объект класс HorizontalLayout
     */
    public Component labelHead() {
        labelHead = new Label("Управление производством");
        labelHead.getStyle().set("color", "#d3b342");
        labelHead.getStyle().set("font-weight", "bold");
        labelHead.getStyle().set("font-size", "15pt");
        labelHead.getStyle().set("margin-left", "30px");

        logo = new Label("Пользователь: " + SecurityUtils.getAuthentication().getDetails().getFio() + " РОЛЬ: " + SecurityUtils.getAuthentication().getRole());
        logo.getStyle().set("color", "green");
        logo.getStyle().set("font-weight", "bold");
        logo.getStyle().set("font-size", "15pt");
        logo.addClassName("logo");

        userSeparator = new HorizontalLayout();
        userSeparator.setWidth("12em");

        Button logout = new Button("Выйти", e -> securityConfiguration.logout());
        logout.getStyle().set("color", "red");
        logout.getStyle().set("font-weight", "bold");
        logout.getStyle().set("font-size", "11pt");

        header = new HorizontalLayout(drawerToggle, labelTitle, userSeparator, logo, logout);
        header.expand(logo);
        header.setDefaultVerticalComponentAlignment(
                FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.addClassName("header");

        return header;
    }

    /**
     * Метод реализует создание новой метки класса Label
     * @param labelText - текст метки
     * @return - возвращает объект класса Label
     */
    public Component labelTitle(String labelText) {
        labelTitle = new Label(labelText);
        labelTitle.getStyle().set("color", "#d3b342");
        labelTitle.getStyle().set("font-weight", "bold");
        labelTitle.getStyle().set("font-size", "16pt");
        labelTitle.getStyle().set("margin-left", "30px");

        return labelTitle;
    }
}
