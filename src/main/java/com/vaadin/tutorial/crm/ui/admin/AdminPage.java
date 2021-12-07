package com.vaadin.tutorial.crm.ui.admin;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.ui.layout.AdminLayout;

/**
 * Класс реализующий страницу администрирования
 */
@Route(value = "adminpage", layout = AdminLayout.class)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class AdminPage extends VerticalLayout {
    VerticalLayout vContent = new VerticalLayout();

    public AdminPage() {
        init();
    }

    private void init() {
        add(vContent);
        vContent.setHorizontalComponentAlignment(Alignment.CENTER);
        vContent.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        vContent.setSizeFull();

        //PLCConnect.stopThread = true;
    }
}
