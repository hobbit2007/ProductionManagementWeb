package com.vaadin.tutorial.crm.ui.report;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.ui.layout.ReportLayout;

/**
 * Класс реализующий пустую страницу при переходе к отчетам
 */
@Route(value = "powerreportempty", layout = ReportLayout.class)
@PageTitle("Отчет по показаниям энергоресурсов | Система управления производством")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class PowerReportEmpty extends VerticalLayout {
    public PowerReportEmpty() {

    }
}
