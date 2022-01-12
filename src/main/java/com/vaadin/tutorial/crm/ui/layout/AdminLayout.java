package com.vaadin.tutorial.crm.ui.layout;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.tutorial.crm.security.SecurityConfiguration;
import com.vaadin.tutorial.crm.security.SecurityUtils;
import com.vaadin.tutorial.crm.service.writetodb.WriteToDBService;
import com.vaadin.tutorial.crm.ui.MainView;
import com.vaadin.tutorial.crm.ui.admin.PlcSignalPage;
import com.vaadin.tutorial.crm.ui.component.LabelComponent;
import com.vaadin.tutorial.crm.ui.powerresources.ChangeCoefficient;
import com.vaadin.tutorial.crm.ui.users.RuleOut;
import com.vaadin.tutorial.crm.ui.users.UsersCreate;
import com.vaadin.tutorial.crm.ui.writetodb.WriteToDBValue;

/**
 * Класс реализующий шапку и боковое меню для меню Администрирование
 */
@CssImport("./styles/shared-styles.css")
public class AdminLayout extends AppLayout {

    private final SecurityConfiguration securityConfiguration;

    LabelComponent labelComponent;
    HorizontalLayout hMenu1 = new HorizontalLayout();
    HorizontalLayout hMenu2 = new HorizontalLayout();
    HorizontalLayout hMenu3 = new HorizontalLayout();
    HorizontalLayout hMenu4 = new HorizontalLayout();
    HorizontalLayout hMenu5 = new HorizontalLayout();
    HorizontalLayout hMenu6 = new HorizontalLayout();
    Button coefficient = new Button("Коэфф. трансформации");
    private final String ROLE = "ADMIN";
    private final String ROLE1 = "USER";
    private final WriteToDBService writeToDBService;
    RouterLink usersAction = new RouterLink("Пользователи", RuleOut.class);
    RouterLink orgStructure = new RouterLink("Орг. структура", RuleOut.class);
    RouterLink plcControllersList = new RouterLink("Список контроллеров", RuleOut.class);
    RouterLink plcSignalsList = new RouterLink("Список сигналов", RuleOut.class);
    RouterLink writeToDBList = new RouterLink("Управление записью в БД", RuleOut.class);

    public AdminLayout(SecurityConfiguration securityConfiguration, WriteToDBService writeToDBService) {
        this.securityConfiguration = securityConfiguration;
        this.writeToDBService = writeToDBService;
        if (SecurityUtils.getAuthentication().getDetails().getRole().equals(ROLE1)) {
            Icon icon = new Icon(VaadinIcon.USER);
            usersAction.setHighlightCondition(HighlightConditions.sameLocation());
            hMenu1.add(icon, usersAction);
            Icon icon1 = new Icon(VaadinIcon.FACTORY);
            orgStructure.setHighlightCondition(HighlightConditions.sameLocation());
            hMenu2.add(icon1, orgStructure);
            Icon icon4 = new Icon(VaadinIcon.LINES);
            plcControllersList.setHighlightCondition(HighlightConditions.sameLocation());
            hMenu4.add(icon4, plcControllersList);
            Icon icon5 = new Icon(VaadinIcon.LINES_LIST);
            plcSignalsList.setHighlightCondition(HighlightConditions.sameLocation());
            hMenu5.add(icon5, plcSignalsList);
            Icon icon6 = new Icon(VaadinIcon.DATABASE);
            writeToDBList.setHighlightCondition(HighlightConditions.sameLocation());
            hMenu6.add(icon6, writeToDBList);
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
        Icon icon2 = new Icon(VaadinIcon.ARROW_BACKWARD);
        hMenu3.add(icon2, back);

        if (SecurityUtils.getAuthentication().getDetails().getRole().equals(ROLE)) {

            usersAction = new RouterLink("Пользователи", UsersCreate.class);
            usersAction.setHighlightCondition(HighlightConditions.sameLocation());
            Icon icon = new Icon(VaadinIcon.USER);
            hMenu1.add(icon, usersAction);

            orgStructure = new RouterLink("Орг. структура", MainView.class);
            orgStructure.setHighlightCondition(HighlightConditions.sameLocation());
            Icon icon1 = new Icon(VaadinIcon.FACTORY);
            hMenu2.add(icon1, orgStructure);

            plcControllersList = new RouterLink("Список контроллеров", MainView.class);
            plcControllersList.setHighlightCondition(HighlightConditions.sameLocation());
            Icon icon4 = new Icon(VaadinIcon.LINES);
            hMenu4.add(icon4, plcControllersList);

            plcSignalsList = new RouterLink("Список сигналов", PlcSignalPage.class);
            plcSignalsList.setHighlightCondition(HighlightConditions.sameLocation());
            Icon icon5 = new Icon(VaadinIcon.LINES_LIST);
            hMenu5.add(icon5, plcSignalsList);

            writeToDBList = new RouterLink("Управление записью в БД", WriteToDBValue.class);
            writeToDBList.setHighlightCondition(HighlightConditions.sameLocation());
            Icon icon6 = new Icon(VaadinIcon.DATABASE);
            hMenu6.add(icon6, writeToDBList);

            Icon icon7 = new Icon(VaadinIcon.EXCHANGE);
            coefficient.setIcon(icon7);
            coefficient.getStyle().set("background-color", "#d3b342");
            coefficient.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            coefficient.addClickListener(e -> {
                new ChangeCoefficient(writeToDBService).open();
            });
        }

        addToDrawer(new VerticalLayout(hMenu3, hMenu1, hMenu2, hMenu4, hMenu5, hMenu6, coefficient));

        //Закрываем меню на стороне клиента
        //т.к. при первом запуске меню показывается автоматически
        //drawerToggle.clickInClient();
    }
}
