package com.vaadin.tutorial.crm.ui.for1c;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.ui.layout.For1CLayout;

/**
 * Класс предоставляющий АПИ для получения значений переменных ПЛК контроллеров
 */
@Route(value = "api1c", layout = For1CLayout.class)
@PageTitle("API к 1С | Система управления производством")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class For1CAPI extends VerticalLayout {
    public For1CAPI() {
        UI.getCurrent().navigate(IntoToDB1C.class);
        UI.getCurrent().getPage().executeJavaScript("window.open(\"http://178.234.44.2:8080/apigateway/swagger-ui.html\", \"_blank\");");
        UI.getCurrent().getPage().reload();
    }
}
