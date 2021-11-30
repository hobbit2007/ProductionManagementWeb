package com.vaadin.tutorial.crm.ui.layout;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.tutorial.crm.security.SecurityConfiguration;
import com.vaadin.tutorial.crm.ui.component.LabelComponent;
import com.vaadin.tutorial.crm.ui.plccontrollersui.PlcValueController;

/**
 * Класс шаблон создающий боковое меню в разделе контроллеры
 */
@CssImport("./styles/shared-styles.css")
public class PlcLayout extends AppLayout {
    private final SecurityConfiguration securityConfiguration;
    LabelComponent labelComponent;

    public PlcLayout(SecurityConfiguration securityConfiguration) {
        this.securityConfiguration = securityConfiguration;
        createHeader();
        createDrawer();
    }
    private void createHeader() {
        labelComponent = new LabelComponent(securityConfiguration);
        addToNavbar(labelComponent.labelHead());
    }
    private void createDrawer() {
        RouterLink plcValue = new RouterLink("Визуализация)", PlcValueController.class);
        plcValue.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink plcControllersList = new RouterLink("Список контроллеров)", null);
        plcControllersList.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink plcSignalsList = new RouterLink("Список сигналов)", null);
        plcSignalsList.setHighlightCondition(HighlightConditions.sameLocation());

        addToDrawer(new VerticalLayout(plcValue, plcControllersList, plcSignalsList));

        //Закрываем меню на стороне клиента
        //т.к. при первом запуске меню показывается автоматически
        //drawerToggle.clickInClient();
    }
}
