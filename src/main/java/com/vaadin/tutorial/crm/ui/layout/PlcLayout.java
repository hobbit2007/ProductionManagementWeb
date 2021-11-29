package com.vaadin.tutorial.crm.ui.layout;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.tutorial.crm.security.SecurityConfiguration;
import com.vaadin.tutorial.crm.ui.MainView;
import com.vaadin.tutorial.crm.ui.component.LabelComponent;
import com.vaadin.tutorial.crm.ui.plccontrollersui.PlcWashingController;

/**
 * Класс шаблон создающий боковое меню в разделе контроллеры
 */
@CssImport("./styles/shared-styles.css")
public class PlcLayout extends AppLayout {
    private final SecurityConfiguration securityConfiguration;
    LabelComponent labelComponent;

    public PlcLayout(SecurityConfiguration securityConfiguration) {
        this.securityConfiguration = securityConfiguration;
        createHeader();
        createDrawer();
    }
    private void createHeader() {
        labelComponent = new LabelComponent(securityConfiguration);
        addToNavbar(labelComponent.labelHead());
    }
    private void createDrawer() {
        RouterLink plcWashingRT = new RouterLink("PLC контроллер Мойка(реальное время)", PlcWashingController.class);
        plcWashingRT.setHighlightCondition(HighlightConditions.sameLocation());
        RouterLink plcWashingStorage = new RouterLink("PLC контроллер Мойка(хранилище)", MainView.class);

        plcWashingStorage.setHighlightCondition(HighlightConditions.sameLocation());
        RouterLink plcDiffusionRT = new RouterLink("PLC контроллер Диффузия(реальное время)", MainView.class);
        plcDiffusionRT.setHighlightCondition(HighlightConditions.sameLocation());
        RouterLink plcDiffusionStorage = new RouterLink("PLC контроллер Диффузия(хранилище)", MainView.class);
        plcDiffusionStorage.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink plcFermentationRT = new RouterLink("PLC контроллер Ферментация(реальное время)", MainView.class);
        plcFermentationRT.setHighlightCondition(HighlightConditions.sameLocation());
        RouterLink plcFermentationStorage = new RouterLink("PLC контроллер Ферментация(хранилище)", MainView.class);
        plcFermentationStorage.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink plcResidueRT = new RouterLink("PLC контроллер Выпарка(реальное время)", MainView.class);
        plcResidueRT.setHighlightCondition(HighlightConditions.sameLocation());
        RouterLink plcResidueStorage = new RouterLink("PLC контроллер Выпарка(хранилище)", MainView.class);
        plcResidueStorage.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink plcRollingRT = new RouterLink("PLC контроллер Розлив сиропа(реальное время)", MainView.class);
        plcRollingRT.setHighlightCondition(HighlightConditions.sameLocation());
        RouterLink plcRollingStorage = new RouterLink("PLC контроллер Розлив сиропа(хранилище)", MainView.class);
        plcRollingStorage.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink plcGranulatorRT = new RouterLink("PLC контроллер Гранулятор(реальное время)", MainView.class);
        plcGranulatorRT.setHighlightCondition(HighlightConditions.sameLocation());
        RouterLink plcGranulatorStorage = new RouterLink("PLC контроллер Гранулятор(хранилище)", MainView.class);
        plcGranulatorStorage.setHighlightCondition(HighlightConditions.sameLocation());

        addToDrawer(new VerticalLayout(plcWashingRT, plcWashingStorage, plcDiffusionRT, plcDiffusionStorage, plcFermentationRT, plcFermentationStorage,
                plcResidueRT, plcResidueStorage, plcRollingRT, plcRollingStorage, plcGranulatorRT, plcGranulatorStorage));

        //Закрываем меню на стороне клиента
        //т.к. при первом запуске меню показывается автоматически
        //drawerToggle.clickInClient();
    }
}
