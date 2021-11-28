package com.vaadin.tutorial.crm.ui.layout;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.tutorial.crm.security.SecurityConfiguration;
import com.vaadin.tutorial.crm.ui.MainView;
import com.vaadin.tutorial.crm.ui.component.LabelComponent;

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
        addToNavbar(labelComponent.labelTitle());
    }

    private void createDrawer() {
        RouterLink userCreate = new RouterLink("Создать пользователя", MainView.class);
        userCreate.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink userEdit = new RouterLink("Редактировать пользователя", MainView.class);
        userEdit.setHighlightCondition(HighlightConditions.sameLocation());

        addToDrawer(new VerticalLayout(userCreate, userEdit));

        //Закрываем меню на стороне клиента
        //т.к. при первом запуске меню показывается автоматически
        //drawerToggle.clickInClient();
    }
}
