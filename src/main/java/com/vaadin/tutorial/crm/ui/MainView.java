package com.vaadin.tutorial.crm.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import com.vaadin.tutorial.crm.security.SecurityUtils;

@Route(value = "", layout = MainLayout.class)
@PWA(name = "Автоматизированная система управления производством",
        shortName = "АСУП",
        description = "Сбор и аналитика данных из PLC контроллеров")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class MainView extends AppLayout {

    Label labelUser;

    public MainView() {
        labelUser = new Label("Пользователь: " + SecurityUtils.getAuthentication().getDetails().getFio() + "(" + SecurityUtils.getAuthentication().getId() + ") " + "РОЛЬ: " + SecurityUtils.getAuthentication().getRole());
        labelUser.getStyle().set("color", "green");
        labelUser.getStyle().set("font-weight", "bold");
        labelUser.getStyle().set("font-size", "11pt");

        setContent(labelUser);

    }

}