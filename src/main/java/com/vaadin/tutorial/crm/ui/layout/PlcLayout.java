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
import com.vaadin.tutorial.crm.ui.component.LabelComponent;
import com.vaadin.tutorial.crm.ui.plccontrollersui.PlcValueController;

/**
 * Класс шаблон создающий боковое меню в разделе контроллеры
 */
@CssImport("./styles/shared-styles.css")
public class PlcLayout extends AppLayout {
    private final SecurityConfiguration securityConfiguration;
    LabelComponent labelComponent;
    HorizontalLayout hMenu1 = new HorizontalLayout();
    HorizontalLayout hMenu2 = new HorizontalLayout();
    HorizontalLayout hMenu3 = new HorizontalLayout();
    HorizontalLayout hMenu4 = new HorizontalLayout();
    private final String ROLE = "ADMIN";

    public PlcLayout(SecurityConfiguration securityConfiguration) {
        this.securityConfiguration = securityConfiguration;
        hMenu3.setVisible(false);
        hMenu4.setVisible(false);
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
        Icon icon = new Icon(VaadinIcon.ARROW_BACKWARD);
        hMenu1.add(icon, back);

        RouterLink plcValue = new RouterLink("Визуализация", PlcValueController.class);
        plcValue.setHighlightCondition(HighlightConditions.sameLocation());
        Icon icon1 = new Icon(VaadinIcon.EYE);
        hMenu2.add(icon1, plcValue);

        if (SecurityUtils.getAuthentication().getDetails().getRole().equals(ROLE)) {
            hMenu3.setVisible(true);
            hMenu4.setVisible(true);
            RouterLink plcControllersList = new RouterLink("Список контроллеров", MainView.class);
            plcControllersList.setHighlightCondition(HighlightConditions.sameLocation());
            Icon icon2 = new Icon(VaadinIcon.LINES);
            hMenu3.add(icon2, plcControllersList);

            RouterLink plcSignalsList = new RouterLink("Список сигналов", MainView.class);
            plcSignalsList.setHighlightCondition(HighlightConditions.sameLocation());
            Icon icon3 = new Icon(VaadinIcon.LINES_LIST);
            hMenu4.add(icon3, plcSignalsList);
        }

        addToDrawer(new VerticalLayout(hMenu1, hMenu2, hMenu3, hMenu4));

        //Закрываем меню на стороне клиента
        //т.к. при первом запуске меню показывается автоматически
        //drawerToggle.clickInClient();
    }
}
