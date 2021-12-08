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
import com.vaadin.tutorial.crm.ui.plccontrollersui.*;

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
    HorizontalLayout hMenu5 = new HorizontalLayout();
    HorizontalLayout hMenu6 = new HorizontalLayout();
    HorizontalLayout hMenu7 = new HorizontalLayout();

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

        RouterLink plcValueDiffusion = new RouterLink("ПЛК Диффузия", PlcValueDiffusion.class);
        plcValueDiffusion.setHighlightCondition(HighlightConditions.sameLocation());
        Icon icon2 = new Icon(VaadinIcon.CONTROLLER);
        hMenu3.add(icon2, plcValueDiffusion);

        RouterLink plcValueFermentation = new RouterLink("ПЛК Ферментация", PlcValueFermentation.class);
        plcValueFermentation.setHighlightCondition(HighlightConditions.sameLocation());
        Icon icon3 = new Icon(VaadinIcon.CONTROLLER);
        hMenu4.add(icon3, plcValueFermentation);

        RouterLink plcValue = new RouterLink("ПЛК Выпарка", PlcValueController.class);
        plcValue.setHighlightCondition(HighlightConditions.sameLocation());
        Icon icon7 = new Icon(VaadinIcon.CONTROLLER);
        hMenu7.add(icon7, plcValue);

        RouterLink plcValueBottling = new RouterLink("ПЛК Розлив", PlcValueBottling.class);
        plcValueBottling.setHighlightCondition(HighlightConditions.sameLocation());
        Icon icon4 = new Icon(VaadinIcon.CONTROLLER);
        hMenu5.add(icon4, plcValueBottling);

        RouterLink plcValueDrying = new RouterLink("ПЛК Сушка", PlcValueDrying.class);
        plcValueDrying.setHighlightCondition(HighlightConditions.sameLocation());
        Icon icon5 = new Icon(VaadinIcon.CONTROLLER);
        hMenu6.add(icon5, plcValueDrying);

        addToDrawer(new VerticalLayout(hMenu1, hMenu2, hMenu3, hMenu4, hMenu7, hMenu5, hMenu6));

        //Закрываем меню на стороне клиента
        //т.к. при первом запуске меню показывается автоматически
        //drawerToggle.clickInClient();
    }
}
