package com.vaadin.tutorial.crm.ui.layout;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.tutorial.crm.security.SecurityConfiguration;
import com.vaadin.tutorial.crm.security.SecurityUtils;
import com.vaadin.tutorial.crm.ui.MainView;
import com.vaadin.tutorial.crm.ui.component.LabelComponent;
import com.vaadin.tutorial.crm.ui.storage.*;
import com.vaadin.tutorial.crm.ui.users.RuleOut;

/**
 * Класс реализующий шапку и боковое меню для меню Склад
 */
public class StorageLayout extends AppLayout {
    LabelComponent labelComponent;
    HorizontalLayout hMenu1 = new HorizontalLayout();
    HorizontalLayout hMenu2 = new HorizontalLayout();
    HorizontalLayout hMenu3 = new HorizontalLayout();
    HorizontalLayout hMenu4 = new HorizontalLayout();
    HorizontalLayout hMenu5 = new HorizontalLayout();
    HorizontalLayout hMenu6 = new HorizontalLayout();
    HorizontalLayout hMenu7 = new HorizontalLayout();
    private final String ROLE = "ADMIN";
    private final String ROLE1 = "USER";
    private final SecurityConfiguration securityConfiguration;
    RouterLink editStore = new RouterLink("Редактировать склад", RuleOut.class);
    RouterLink addStore = new RouterLink("Добавить склад", RuleOut.class);
    RouterLink addCell = new RouterLink("Добавить ячейку", RuleOut.class);
    RouterLink editCell = new RouterLink("Редактировать ячейку", RuleOut.class);

    public StorageLayout(SecurityConfiguration securityConfiguration) {
        this.securityConfiguration = securityConfiguration;
        if (SecurityUtils.getAuthentication().getDetails().getRole().equals(ROLE1)) {
            editStore.setHighlightCondition(HighlightConditions.sameLocation());
            Icon icon6 = new Icon(VaadinIcon.EDIT);
            hMenu6.add(icon6, editStore);

            addStore.setHighlightCondition(HighlightConditions.sameLocation());
            Icon icon3 = new Icon(VaadinIcon.ADD_DOCK);
            hMenu3.add(icon3, addStore);

            addStore.setHighlightCondition(HighlightConditions.sameLocation());
            Icon icon4 = new Icon(VaadinIcon.ADD_DOCK);
            hMenu4.add(icon4, addCell);

            editCell.setHighlightCondition(HighlightConditions.sameLocation());
            Icon icon7 = new Icon(VaadinIcon.EDIT);
            hMenu7.add(icon7, editCell);
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

        RouterLink search = new RouterLink("Поиск по складу", StorageSearch.class);
        search.setHighlightCondition(HighlightConditions.sameLocation());
        Icon icon2 = new Icon(VaadinIcon.SEARCH);
        hMenu2.add(icon2, search);

        if (SecurityUtils.getAuthentication().getDetails().getRole().equals(ROLE)) {
            addStore = new RouterLink("Добавить склад", CreateStorageDialog.class);
            addStore.setHighlightCondition(HighlightConditions.sameLocation());
            Icon icon3 = new Icon(VaadinIcon.ADD_DOCK);
            hMenu3.add(icon3, addStore);

            editStore = new RouterLink("Редактировать склад", EditStorageName.class);
            editStore.setHighlightCondition(HighlightConditions.sameLocation());
            Icon icon6 = new Icon(VaadinIcon.EDIT);
            hMenu6.add(icon6, editStore);

            addCell = new RouterLink("Добавить ячейку", CreateCellDialog.class);
            addStore.setHighlightCondition(HighlightConditions.sameLocation());
            Icon icon4 = new Icon(VaadinIcon.ADD_DOCK);
            hMenu4.add(icon4, addCell);

            editCell = new RouterLink("Редактировать ячейку", EditCellName.class);
            editCell.setHighlightCondition(HighlightConditions.sameLocation());
            Icon icon7 = new Icon(VaadinIcon.EDIT);
            hMenu7.add(icon7, editCell);
        }

        RouterLink addMaterial = new RouterLink("Добавить объект хранения", CreateMaterialDialog.class);
        addMaterial.setHighlightCondition(HighlightConditions.sameLocation());
        Icon icon5 = new Icon(VaadinIcon.INVOICE);
        hMenu5.add(icon5, addMaterial);

        addToDrawer(new VerticalLayout(hMenu1, hMenu3, hMenu6, hMenu4, hMenu7, hMenu5, hMenu2));
    }
}
