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
import com.vaadin.tutorial.crm.ui.plccontrollersui.PlcValueWashing;

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

        RouterLink plcValueWashing = new RouterLink("ПЛК Мойка", PlcValueWashing.class);
        plcValueWashing.setHighlightCondition(HighlightConditions.sameLocation());
        Icon icon1 = new Icon(VaadinIcon.CONTROLLER);
        hMenu2.add(icon1, plcValueWashing);

        RouterLink plcValue = new RouterLink("ПЛК Выпарка", PlcValueController.class);
        plcValue.setHighlightCondition(HighlightConditions.sameLocation());
        Icon icon2 = new Icon(VaadinIcon.CONTROLLER);
        hMenu3.add(icon2, plcValue);

        addToDrawer(new VerticalLayout(hMenu1, hMenu2, hMenu3));

        //Закрываем меню на стороне клиента
        //т.к. при первом запуске меню показывается автоматически
        //drawerToggle.clickInClient();
    }
}
