package com.vaadin.tutorial.crm.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.tutorial.crm.repository.UserRepository;
import com.vaadin.tutorial.crm.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Класс создающий титульный заголовок вверху страницы
 */

@CssImport("./styles/shared-styles.css")
public class MainLayout extends AppLayout {
    private Label logo, labelTitle, exit;
    private HorizontalLayout userSeparator;
    private DrawerToggle drawerToggle = new DrawerToggle();

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private UserRepository userRepository;

    public MainLayout() {
        createHeader();
        createDrawer();
    }
    private void createHeader() {
        labelTitle = new Label("Адресное хранилище");
        labelTitle.getStyle().set("color", "#d3b342");
        labelTitle.getStyle().set("font-weight", "bold");
        labelTitle.getStyle().set("font-size", "15pt");
        labelTitle.getStyle().set("margin-left", "30px");

        logo = new Label("Пользователь: " + SecurityUtils.getAuthentication().getDetails().getFio() + " РОЛЬ: " + SecurityUtils.getAuthentication().getRole());
        logo.getStyle().set("color", "green");
        logo.getStyle().set("font-weight", "bold");
        logo.getStyle().set("font-size", "15pt");
        logo.addClassName("logo");

        exit = new Label("Выйти");
        exit.getStyle().set("color", "red");
        exit.getStyle().set("font-weight", "bold");
        exit.getStyle().set("font-size", "11pt");

        userSeparator = new HorizontalLayout();
        userSeparator.setWidth("12em");

        Anchor logout = new Anchor("logout", exit);

        HorizontalLayout header = new HorizontalLayout(drawerToggle, labelTitle, userSeparator, logo, logout);
        header.expand(logo);
        header.setDefaultVerticalComponentAlignment(
                FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.addClassName("header");
        addToNavbar(header);
    }
    private void createDrawer() {
        RouterLink listLink = new RouterLink("Поиск места хранения", MainView.class);
        listLink.setHighlightCondition(HighlightConditions.sameLocation());

        addToDrawer(new VerticalLayout(listLink));

        //Закрываем меню на стороне клиента
        //т.к. при первом запуске меню показывается автоматически
        //drawerToggle.clickInClient();
    }
}