package com.vaadin.tutorial.crm.ui.powerresources;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.ui.layout.PowerLayout;

/**
 * Класс реализующий показ статистику энергоресурсов
 */
@Route(value = "statistics", layout = PowerLayout.class)
@PageTitle("Статистика энергоресурсов | Система управления производством")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class PowerStatistic extends VerticalLayout {
}
