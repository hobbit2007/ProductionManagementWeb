package com.vaadin.tutorial.crm.ui.layout;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.tutorial.crm.security.SecurityConfiguration;
import com.vaadin.tutorial.crm.service.storage.StorageService;
import com.vaadin.tutorial.crm.ui.MainView;
import com.vaadin.tutorial.crm.ui.component.LabelComponent;
import com.vaadin.tutorial.crm.ui.storage.CreateStorageDialog;
import com.vaadin.tutorial.crm.ui.storage.StorageSearch;

/**
 * Класс реализующий шапку и боковое меню для меню Склад
 */
public class StorageLayout extends AppLayout {
    LabelComponent labelComponent;
    HorizontalLayout hMenu1 = new HorizontalLayout();
    HorizontalLayout hMenu2 = new HorizontalLayout();
    HorizontalLayout hMenu3 = new HorizontalLayout();
    private final SecurityConfiguration securityConfiguration;
    private final StorageService storageService;

    public StorageLayout(SecurityConfiguration securityConfiguration, StorageService storageService) {
        this.securityConfiguration = securityConfiguration;
        this.storageService = storageService;
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

        RouterLink search = new RouterLink("Поиск по складу", StorageSearch.class);
        search.setHighlightCondition(HighlightConditions.sameLocation());
        Icon icon2 = new Icon(VaadinIcon.SEARCH);
        hMenu2.add(icon2, search);

        RouterLink addStore = new RouterLink("Добавить склад", CreateStorageDialog.class);
        addStore.setHighlightCondition(HighlightConditions.sameLocation());
        Icon icon3 = new Icon(VaadinIcon.ADD_DOCK);
        hMenu3.add(icon3, addStore);

        addToDrawer(new VerticalLayout(hMenu1, hMenu3, hMenu2));
    }
}
