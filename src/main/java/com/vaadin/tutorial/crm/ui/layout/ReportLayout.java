package com.vaadin.tutorial.crm.ui.layout;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.tutorial.crm.entity.powerresources.PowerReportModel;
import com.vaadin.tutorial.crm.security.SecurityConfiguration;
import com.vaadin.tutorial.crm.service.powerresources.PowerResourcesService;
import com.vaadin.tutorial.crm.ui.MainView;
import com.vaadin.tutorial.crm.ui.component.LabelComponent;
import com.vaadin.tutorial.crm.ui.report.PowerReportEmpty;
import com.vaadin.tutorial.crm.ui.report.PowerResourceReport;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Класс реализующий шапку и боковое меню для Отчетов
 */
public class ReportLayout extends AppLayout {
    private final SecurityConfiguration securityConfiguration;
    LabelComponent labelComponent;
    HorizontalLayout hMenu1 = new HorizontalLayout();

    private final PowerResourcesService powerResourcesService;
    private final PowerReportModel powerReportModel;
    @Autowired
    public ReportLayout(SecurityConfiguration securityConfiguration, PowerResourcesService powerResourcesService, PowerReportModel powerReportModel) {
        this.securityConfiguration = securityConfiguration;
        this.powerResourcesService = powerResourcesService;
        this.powerReportModel = powerReportModel;

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

        Button powerWaterValue = new Button("Отчет вода");
        Icon icon2 = new Icon(VaadinIcon.PIN_POST);
        powerWaterValue.setIcon(icon2);
        powerWaterValue.addClickListener(e -> {
            UI.getCurrent().navigate(PowerReportEmpty.class);
            powerReportModel.setMsg("Отчет по использованию воды ");
            powerReportModel.setId(1);
            UI.getCurrent().navigate(new PowerResourceReport(powerResourcesService, powerReportModel).getClass());
        });

        Button powerGasValue = new Button("Отчет газ  ");
        Icon icon3 = new Icon(VaadinIcon.PIN_POST);
        powerGasValue.setIcon(icon3);
        powerGasValue.addClickListener(e -> {
            UI.getCurrent().navigate(PowerReportEmpty.class);
            powerReportModel.setMsg("Отчет по использованию газа");
            powerReportModel.setId(4);
            UI.getCurrent().navigate(new PowerResourceReport(powerResourcesService, powerReportModel).getClass());
        });

        Button powerEnter1Value = new Button("Отчет Ввод1");
        Icon icon4 = new Icon(VaadinIcon.PIN_POST);
        powerEnter1Value.setIcon(icon4);
        powerEnter1Value.addClickListener(e -> {
            UI.getCurrent().navigate(PowerReportEmpty.class);
            powerReportModel.setMsg("Отчет по использованию электричества ввод1");
            powerReportModel.setId(5);
            UI.getCurrent().navigate(new PowerResourceReport(powerResourcesService, powerReportModel).getClass());
        });

        Button powerEnter2Value = new Button("Отчет Ввод2");
        Icon icon5 = new Icon(VaadinIcon.PIN_POST);
        powerEnter2Value.setIcon(icon5);
        powerEnter2Value.addClickListener(e -> {
            UI.getCurrent().navigate(PowerReportEmpty.class);
            powerReportModel.setMsg("Отчет по использованию электричества ввод2");
            powerReportModel.setId(6);
            UI.getCurrent().navigate(new PowerResourceReport(powerResourcesService, powerReportModel).getClass());
        });

        Button powerTotalEnterValue = new Button("Отчет электроэнергия");
        Icon icon6 = new Icon(VaadinIcon.PIN_POST);
        powerTotalEnterValue.setIcon(icon6);
        powerTotalEnterValue.addClickListener(e -> {
            UI.getCurrent().navigate(PowerReportEmpty.class);
            powerReportModel.setMsg("Отчет по использованию электроэнергия суммарно");
            powerReportModel.setId(9);
            UI.getCurrent().navigate(new PowerResourceReport(powerResourcesService, powerReportModel).getClass());
        });

        Button powerStockValue = new Button("Отчет стоки");
        Icon icon7 = new Icon(VaadinIcon.PIN_POST);
        powerStockValue.setIcon(icon7);
        powerStockValue.addClickListener(e -> {
            UI.getCurrent().navigate(PowerReportEmpty.class);
            powerReportModel.setMsg("Отчет по стокам");
            powerReportModel.setId(10);
            UI.getCurrent().navigate(new PowerResourceReport(powerResourcesService, powerReportModel).getClass());
        });

        addToDrawer(new VerticalLayout(hMenu1, powerWaterValue, powerGasValue, powerEnter1Value, powerEnter2Value, powerTotalEnterValue, powerStockValue));
    }
}
