package com.vaadin.tutorial.crm.ui.layout;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.tutorial.crm.security.SecurityConfiguration;
import com.vaadin.tutorial.crm.security.SecurityUtils;
import com.vaadin.tutorial.crm.ui.MainView;
import com.vaadin.tutorial.crm.ui.admin.AdminPage;
import com.vaadin.tutorial.crm.ui.component.LabelComponent;
import com.vaadin.tutorial.crm.ui.for1c.IntoToDB1C;
import com.vaadin.tutorial.crm.ui.plccontrollersui.PlcValueController;
import com.vaadin.tutorial.crm.ui.plccontrollersui.PlcValueWashing;
import com.vaadin.tutorial.crm.ui.powerresources.PowerStatistic;
import com.vaadin.tutorial.crm.ui.report.PowerReportEmpty;
import com.vaadin.tutorial.crm.ui.report.PowerResourceReport;
import com.vaadin.tutorial.crm.ui.storage.StorageSearch;
import com.vaadin.tutorial.crm.ui.users.RuleOut;

/**
 * Класс создающий титульный заголовок вверху страницы
 */

@CssImport("./styles/shared-styles.css")
public class MainLayout extends AppLayout {
    private final SecurityConfiguration securityConfiguration;
    LabelComponent labelComponent;
    HorizontalLayout hMenu1 = new HorizontalLayout();
    HorizontalLayout hMenu2 = new HorizontalLayout();
    HorizontalLayout hMenu3 = new HorizontalLayout();
    HorizontalLayout hMenu4 = new HorizontalLayout();
    HorizontalLayout hMenu5 = new HorizontalLayout();
    HorizontalLayout hMenu6 = new HorizontalLayout();
    HorizontalLayout hMenu7 = new HorizontalLayout();
    RouterLink for1cLink = new RouterLink("Интеграция 1С", RuleOut.class);
    private final String ROLE = "ADMIN";
    private final String ROLE1 = "USER";

    public MainLayout(SecurityConfiguration securityConfiguration) {
        this.securityConfiguration = securityConfiguration;
        if (SecurityUtils.getAuthentication().getDetails().getRole().equals(ROLE1)) {
            for1cLink.setHighlightCondition(HighlightConditions.sameLocation());
            Icon icon6 = new Icon(VaadinIcon.CLUSTER);
            hMenu7.add(icon6, for1cLink);
        }

        createHeader();
        createDrawer();
    }
    private void createHeader() {
        labelComponent = new LabelComponent(securityConfiguration);
        addToNavbar(labelComponent.labelHead());
    }
    private void createDrawer() {
        RouterLink dashboardLink = new RouterLink("Dashboard", MainView.class);
        dashboardLink.setHighlightCondition(HighlightConditions.sameLocation());
        Icon icon = new Icon(VaadinIcon.DASHBOARD);
        hMenu1.add(icon, dashboardLink);

        RouterLink plcLink = new RouterLink("Визуализация", PlcValueWashing.class);
        plcLink.setHighlightCondition(HighlightConditions.sameLocation());
        Icon icon1 = new Icon(VaadinIcon.EYE);
        hMenu2.add(icon1, plcLink);

        RouterLink adminLink = new RouterLink("Администрирование", AdminPage.class);
        adminLink.setHighlightCondition(HighlightConditions.sameLocation());
        Icon icon2 = new Icon(VaadinIcon.TOOLS);
        hMenu3.add(icon2, adminLink);

        RouterLink powerLink = new RouterLink("Энергоресурсы", PowerStatistic.class);
        powerLink.setHighlightCondition(HighlightConditions.sameLocation());
        Icon icon3 = new Icon(VaadinIcon.POWER_OFF);
        hMenu4.add(icon3, powerLink);

        RouterLink storageLink = new RouterLink("Склад", StorageSearch.class);
        storageLink.setHighlightCondition(HighlightConditions.sameLocation());
        Icon icon5 = new Icon(VaadinIcon.STORAGE);
        hMenu6.add(icon5, storageLink);

        RouterLink reportLink = new RouterLink("Отчеты", PowerReportEmpty.class);
        reportLink.setHighlightCondition(HighlightConditions.sameLocation());
        Icon icon4 = new Icon(VaadinIcon.MODAL_LIST);
        hMenu5.add(icon4, reportLink);

        if (SecurityUtils.getAuthentication().getDetails().getRole().equals(ROLE)) {
            for1cLink = new RouterLink("Интеграция 1С", IntoToDB1C.class);
            for1cLink.setHighlightCondition(HighlightConditions.sameLocation());
            Icon icon6 = new Icon(VaadinIcon.CLUSTER);
            hMenu7.add(icon6, for1cLink);
        }

        addToDrawer(new VerticalLayout(hMenu1, hMenu2, hMenu4, hMenu3, hMenu6, hMenu5, hMenu7));

        //Закрываем меню на стороне клиента
        //т.к. при первом запуске меню показывается автоматически
        //drawerToggle.clickInClient();
    }
}