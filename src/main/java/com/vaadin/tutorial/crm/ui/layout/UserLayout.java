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
import com.vaadin.tutorial.crm.ui.users.UsersCreate;

/**
 * Класс реализующий боковое меню управления пользователями
 */
@CssImport("./styles/shared-styles.css")
public class UserLayout extends AppLayout {

    private final SecurityConfiguration securityConfiguration;

    LabelComponent labelComponent;

    public UserLayout(SecurityConfiguration securityConfiguration) {
        this.securityConfiguration = securityConfiguration;
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

        RouterLink usersCreate = new RouterLink("Создать пользователя", UsersCreate.class);
        usersCreate.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink userEdit = new RouterLink("Редактировать пользователя", MainView.class);
        userEdit.setHighlightCondition(HighlightConditions.sameLocation());

        addToDrawer(new VerticalLayout(back, usersCreate, userEdit));

        //Закрываем меню на стороне клиента
        //т.к. при первом запуске меню показывается автоматически
        //drawerToggle.clickInClient();
    }
}
