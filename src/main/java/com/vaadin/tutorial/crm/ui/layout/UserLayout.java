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
    HorizontalLayout hMenu1 = new HorizontalLayout();
    HorizontalLayout hMenu2 = new HorizontalLayout();
    HorizontalLayout hMenu3 = new HorizontalLayout();

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
        Icon icon = new Icon(VaadinIcon.ARROW_BACKWARD);
        hMenu1.add(icon, back);

        RouterLink usersCreate = new RouterLink("Создать пользователя", UsersCreate.class);
        usersCreate.setHighlightCondition(HighlightConditions.sameLocation());
        Icon icon1 = new Icon(VaadinIcon.DATABASE);
        hMenu2.add(icon1, usersCreate);

        RouterLink userEdit = new RouterLink("Редактировать пользователя", MainView.class);
        userEdit.setHighlightCondition(HighlightConditions.sameLocation());
        Icon icon2 = new Icon(VaadinIcon.USER_CHECK);
        hMenu3.add(icon2, userEdit);

        addToDrawer(new VerticalLayout(hMenu1, hMenu2, hMenu3));

        //Закрываем меню на стороне клиента
        //т.к. при первом запуске меню показывается автоматически
        //drawerToggle.clickInClient();
    }
}
