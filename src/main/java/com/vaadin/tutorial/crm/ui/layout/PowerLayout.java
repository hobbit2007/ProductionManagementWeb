package com.vaadin.tutorial.crm.ui.layout;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.tutorial.crm.security.SecurityConfiguration;
import com.vaadin.tutorial.crm.security.SecurityUtils;
import com.vaadin.tutorial.crm.service.powerresources.PowerResourceDictService;
import com.vaadin.tutorial.crm.service.powerresources.PowerResourcesService;
import com.vaadin.tutorial.crm.service.writetodb.WriteToDBService;
import com.vaadin.tutorial.crm.ui.MainView;
import com.vaadin.tutorial.crm.ui.component.LabelComponent;
import com.vaadin.tutorial.crm.ui.powerresources.CreatePowerDialog;
import com.vaadin.tutorial.crm.ui.powerresources.EditPower;
import com.vaadin.tutorial.crm.ui.powerresources.PowerStatistic;
import com.vaadin.tutorial.crm.ui.powerresources.TableView;
import com.vaadin.tutorial.crm.ui.users.RuleOut;
import com.vaadin.tutorial.crm.ui.users.RuleOutEnergo;

/**
 * Класс реализующий шапку и боковое меню для меню Энергоресурсы
 */
public class PowerLayout extends AppLayout {
    private final SecurityConfiguration securityConfiguration;
    Button createPower = new Button("Добавить показания");

    LabelComponent labelComponent;
    HorizontalLayout hMenu1 = new HorizontalLayout();
    HorizontalLayout hMenu2 = new HorizontalLayout();
    HorizontalLayout hMenu3 = new HorizontalLayout();
    HorizontalLayout hMenu4 = new HorizontalLayout();
    private final PowerResourceDictService powerResourceDictService;
    private final PowerResourcesService powerResourcesService;
    private final WriteToDBService writeToDBService;
    private final String ROLE = "ADMIN";
    private final String ROLE1 = "USER";
    RouterLink editLink= new RouterLink("Редактирование показаний", RuleOutEnergo.class);

    public PowerLayout(SecurityConfiguration securityConfiguration, PowerResourceDictService powerResourceDictService, PowerResourcesService powerResourcesService, WriteToDBService writeToDBService) {
        this.securityConfiguration = securityConfiguration;
        this.powerResourceDictService = powerResourceDictService;
        this.powerResourcesService = powerResourcesService;
        this.writeToDBService = writeToDBService;
        if (SecurityUtils.getAuthentication().getDetails().getRole().equals(ROLE1)) {
            Icon icon5 = new Icon(VaadinIcon.EDIT);
            editLink.setHighlightCondition(HighlightConditions.sameLocation());
            hMenu4.add(icon5, editLink);
        }

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

        Icon icon4 = new Icon(VaadinIcon.FILE_ADD);
        createPower.setIcon(icon4);
        createPower.getStyle().set("background-color", "#d3b342");
        createPower.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        createPower.addClickListener(e -> {
           new CreatePowerDialog(powerResourceDictService, powerResourcesService, writeToDBService).open();
        });

        RouterLink powerView = new RouterLink("Таблица показаний", TableView.class);
        powerView.setHighlightCondition(HighlightConditions.sameLocation());
        Icon icon2 = new Icon(VaadinIcon.TABLE);
        hMenu2.add(icon2, powerView);

        RouterLink statistics = new RouterLink("Статистика", PowerStatistic.class);
        statistics.setHighlightCondition(HighlightConditions.sameLocation());
        Icon icon3 = new Icon(VaadinIcon.CHART);
        hMenu3.add(icon3, statistics);

        if (SecurityUtils.getAuthentication().getDetails().getRole().equals(ROLE)) {
            editLink= new RouterLink("Редактирование показаний", EditPower.class);
            editLink.setHighlightCondition(HighlightConditions.sameLocation());
            Icon icon5 = new Icon(VaadinIcon.EDIT);
            hMenu4.add(icon5, editLink);
        }

        addToDrawer(new VerticalLayout(hMenu1, createPower, hMenu2, hMenu3, hMenu4));

        //Закрываем меню на стороне клиента
        //т.к. при первом запуске меню показывается автоматически
        //drawerToggle.clickInClient();
    }
}
