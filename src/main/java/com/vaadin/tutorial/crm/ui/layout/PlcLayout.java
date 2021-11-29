package com.vaadin.tutorial.crm.ui.layout;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.tutorial.crm.security.SecurityConfiguration;
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
        RouterLink plcWashingStorage = new RouterLink("PLC контроллер Мойка(хранилище)", PlcWashingController.class);

        plcWashingStorage.setHighlightCondition(HighlightConditions.sameLocation());
        RouterLink plcDiffusionRT = new RouterLink("PLC контроллер Диффузия(реальное время)", PlcWashingController.class);
        plcDiffusionRT.setHighlightCondition(HighlightConditions.sameLocation());
        RouterLink plcDiffusionStorage = new RouterLink("PLC контроллер Диффузия(хранилище)", PlcWashingController.class);
        plcDiffusionStorage.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink plcFermentationRT = new RouterLink("PLC контроллер Ферментация(реальное время)", PlcWashingController.class);
        plcFermentationRT.setHighlightCondition(HighlightConditions.sameLocation());
        RouterLink plcFermentationStorage = new RouterLink("PLC контроллер Ферментация(хранилище)", PlcWashingController.class);
        plcFermentationStorage.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink plcResidueRT = new RouterLink("PLC контроллер Выпарка(реальное время)", PlcWashingController.class);
        plcResidueRT.setHighlightCondition(HighlightConditions.sameLocation());
        RouterLink plcResidueStorage = new RouterLink("PLC контроллер Выпарка(хранилище)", PlcWashingController.class);
        plcResidueStorage.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink plcRollingRT = new RouterLink("PLC контроллер Розлив сиропа(реальное время)", PlcWashingController.class);
        plcRollingRT.setHighlightCondition(HighlightConditions.sameLocation());
        RouterLink plcRollingStorage = new RouterLink("PLC контроллер Розлив сиропа(хранилище)", PlcWashingController.class);
        plcRollingStorage.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink plcGranulatorRT = new RouterLink("PLC контроллер Гранулятор(реальное время)", PlcWashingController.class);
        plcGranulatorRT.setHighlightCondition(HighlightConditions.sameLocation());
        RouterLink plcGranulatorStorage = new RouterLink("PLC контроллер Гранулятор(хранилище)", PlcWashingController.class);
        plcGranulatorStorage.setHighlightCondition(HighlightConditions.sameLocation());

        addToDrawer(new VerticalLayout(plcWashingRT, plcWashingStorage, plcDiffusionRT, plcDiffusionStorage, plcFermentationRT, plcFermentationStorage,
                plcResidueRT, plcResidueStorage, plcRollingRT, plcRollingStorage, plcGranulatorRT, plcGranulatorStorage));

        //Закрываем меню на стороне клиента
        //т.к. при первом запуске меню показывается автоматически
        //drawerToggle.clickInClient();
    }
}
