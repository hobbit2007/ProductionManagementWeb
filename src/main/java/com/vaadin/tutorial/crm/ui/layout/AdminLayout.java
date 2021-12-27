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
import com.vaadin.tutorial.crm.ui.admin.PlcSignalPage;
import com.vaadin.tutorial.crm.ui.component.LabelComponent;
import com.vaadin.tutorial.crm.ui.users.UsersCreate;
import com.vaadin.tutorial.crm.ui.writetodb.WriteToDB;

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
    private final String ROLE = "ADMIN";

    public AdminLayout(SecurityConfiguration securityConfiguration) {
        this.securityConfiguration = securityConfiguration;
        hMenu1.setVisible(false);
        hMenu2.setVisible(false);
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
            hMenu1.setVisible(true);
            hMenu2.setVisible(true);
            hMenu4.setVisible(true);
            hMenu5.setVisible(true);

            RouterLink usersAction = new RouterLink("Пользователи", UsersCreate.class);
            usersAction.setHighlightCondition(HighlightConditions.sameLocation());
            Icon icon = new Icon(VaadinIcon.USER);
            hMenu1.add(icon, usersAction);

            RouterLink orgStructure = new RouterLink("Орг. структура", MainView.class);
            orgStructure.setHighlightCondition(HighlightConditions.sameLocation());
            Icon icon1 = new Icon(VaadinIcon.FACTORY);
            hMenu2.add(icon1, orgStructure);

            RouterLink plcControllersList = new RouterLink("Список контроллеров", MainView.class);
            plcControllersList.setHighlightCondition(HighlightConditions.sameLocation());
            Icon icon4 = new Icon(VaadinIcon.LINES);
            hMenu4.add(icon4, plcControllersList);

            RouterLink plcSignalsList = new RouterLink("Список сигналов", PlcSignalPage.class);
            plcSignalsList.setHighlightCondition(HighlightConditions.sameLocation());
            Icon icon5 = new Icon(VaadinIcon.LINES_LIST);
            hMenu5.add(icon5, plcSignalsList);

            RouterLink writeToDBList = new RouterLink("Управление записью в БД", WriteToDB.class);
            writeToDBList.setHighlightCondition(HighlightConditions.sameLocation());
            Icon icon6 = new Icon(VaadinIcon.DATABASE);
            hMenu6.add(icon6, writeToDBList);
        }





        addToDrawer(new VerticalLayout(hMenu3, hMenu1, hMenu2, hMenu4, hMenu5));

        //Закрываем меню на стороне клиента
        //т.к. при первом запуске меню показывается автоматически
        //drawerToggle.clickInClient();
    }
}
