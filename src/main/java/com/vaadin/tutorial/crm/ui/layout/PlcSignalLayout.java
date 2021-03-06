package com.vaadin.tutorial.crm.ui.layout;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.tutorial.crm.security.SecurityConfiguration;
import com.vaadin.tutorial.crm.service.HistoryService;
import com.vaadin.tutorial.crm.service.plccontrollersservice.PlcControllersService;
import com.vaadin.tutorial.crm.service.plccontrollersservice.SignalGroupsService;
import com.vaadin.tutorial.crm.service.plccontrollersservice.SignalListService;
import com.vaadin.tutorial.crm.ui.admin.AdminPage;
import com.vaadin.tutorial.crm.ui.component.LabelComponent;
import com.vaadin.tutorial.crm.ui.plccontrollersui.CreateSignal;
import com.vaadin.tutorial.crm.ui.plccontrollersui.EditSignal;

/**
* Класс реализующий шапку и боковое меню для меню Список сигналов
*/
@CssImport("./styles/shared-styles.css")
public class PlcSignalLayout extends AppLayout {
    private final SecurityConfiguration securityConfiguration;
    private final PlcControllersService plcControllersService;
    private final SignalGroupsService signalGroupsService;
    private final SignalListService signalListService;
    private final HistoryService historyService;
    LabelComponent labelComponent;
    HorizontalLayout hMenu1 = new HorizontalLayout();
    HorizontalLayout hMenu2 = new HorizontalLayout();
    Button createSignal;

    public PlcSignalLayout(SecurityConfiguration securityConfiguration, PlcControllersService plcControllersService,
                           SignalGroupsService signalGroupsService, SignalListService signalListService, HistoryService historyService) {
        this.securityConfiguration = securityConfiguration;
        this.plcControllersService = plcControllersService;
        this.signalGroupsService = signalGroupsService;
        this.signalListService = signalListService;
        this.historyService = historyService;

        createHeader();
        createDrawer();
    }
    private void createHeader() {
        labelComponent = new LabelComponent(securityConfiguration);
        addToNavbar(labelComponent.labelHead());
    }
    private void createDrawer() {
        RouterLink back = new RouterLink("Назад", AdminPage.class);
        back.setHighlightCondition(HighlightConditions.sameLocation());
        Icon icon = new Icon(VaadinIcon.ARROW_BACKWARD);
        hMenu1.add(icon, back);

        createSignal = new Button("Новая переменная");
        Icon icon1 = new Icon(VaadinIcon.FILE_ADD);
        createSignal.setIcon(icon1);
        createSignal.getStyle().set("background-color", "#d3b342");
        createSignal.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        createSignal.addClickListener(e -> {
           new CreateSignal(plcControllersService, signalGroupsService, signalListService, historyService).open();
        });

        RouterLink editSignal = new RouterLink("Редактировать переменную", EditSignal.class);
        editSignal.setHighlightCondition(HighlightConditions.sameLocation());
        Icon icon2 = new Icon(VaadinIcon.FILE_PROCESS);
        hMenu2.add(icon2, editSignal);

        addToDrawer(new VerticalLayout(hMenu1, createSignal, hMenu2));
        //Закрываем меню на стороне клиента
        //т.к. при первом запуске меню показывается автоматически
        //drawerToggle.clickInClient();
    }
}
