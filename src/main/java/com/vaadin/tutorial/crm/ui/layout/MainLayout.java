package com.vaadin.tutorial.crm.ui.layout;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.tutorial.crm.security.SecurityConfiguration;
import com.vaadin.tutorial.crm.ui.MainView;
import com.vaadin.tutorial.crm.ui.admin.AdminPage;
import com.vaadin.tutorial.crm.ui.component.LabelComponent;
import com.vaadin.tutorial.crm.ui.plccontrollersui.PlcValueController;

/**
 * Класс создающий титульный заголовок вверху страницы
 */

@CssImport("./styles/shared-styles.css")
public class MainLayout extends AppLayout {
    private final SecurityConfiguration securityConfiguration;
    LabelComponent labelComponent;

    public MainLayout(SecurityConfiguration securityConfiguration) {
        this.securityConfiguration = securityConfiguration;
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
        RouterLink plcLink = new RouterLink("PLC контроллеры", PlcValueController.class);
        plcLink.setHighlightCondition(HighlightConditions.sameLocation());
        RouterLink adminLink = new RouterLink("Администрирование", AdminPage.class);
        adminLink.setHighlightCondition(HighlightConditions.sameLocation());

        addToDrawer(new VerticalLayout(dashboardLink, plcLink, adminLink));

        //Закрываем меню на стороне клиента
        //т.к. при первом запуске меню показывается автоматически
        //drawerToggle.clickInClient();
    }
}