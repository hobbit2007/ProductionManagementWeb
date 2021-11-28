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
 * Класс реализующий шапку и боковое меню для меню Администрирование
 */
@CssImport("./styles/shared-styles.css")
public class AdminLayout extends AppLayout {

    private final SecurityConfiguration securityConfiguration;

    LabelComponent labelComponent;

    public AdminLayout(SecurityConfiguration securityConfiguration) {
        this.securityConfiguration = securityConfiguration;
        createHeader();
        createDrawer();
    }
    private void createHeader() {
        labelComponent = new LabelComponent(securityConfiguration);
       addToNavbar(labelComponent.labelTitle());
    }

    private void createDrawer() {
        RouterLink usersAction = new RouterLink("Пользователи", MainView.class);
        usersAction.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink orgStructure = new RouterLink("Орг. структура", MainView.class);
        orgStructure.setHighlightCondition(HighlightConditions.sameLocation());

        addToDrawer(new VerticalLayout(usersAction, orgStructure));

        //Закрываем меню на стороне клиента
        //т.к. при первом запуске меню показывается автоматически
        //drawerToggle.clickInClient();
    }
}
