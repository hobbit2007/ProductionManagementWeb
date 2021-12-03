package com.vaadin.tutorial.crm.ui;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.AttachNotifier;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.service.plccontrollersservice.SchedulerService;
import com.vaadin.tutorial.crm.threads.FeederThread;
import com.vaadin.tutorial.crm.ui.layout.MainLayout;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Главный класс приложения
 */
@Route(value = "", layout = MainLayout.class)
@PageTitle("Dashboard | Система управления производством")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class MainView extends VerticalLayout {

    private Label labelUser  = new Label();
    VerticalLayout vContent = new VerticalLayout();
    HorizontalLayout hContent = new HorizontalLayout();
    FeederThread thread;

    public MainView() {
        SchedulerService.stopThread = true;

        labelUser.getStyle().set("color", "red");
        labelUser.getStyle().set("font-weight", "bold");
        labelUser.getStyle().set("font-size", "11pt");
        //labelUser.getStyle().set("border", "1px inset blue");

        hContent.add(labelUser);

        vContent.add(hContent);
        vContent.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        vContent.setSizeFull();


        add(vContent);

    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        // Start the data feed thread
        thread = new FeederThread(attachEvent.getUI(), labelUser);
        thread.start();
    }

    //@Override
    //protected void onDetach(DetachEvent detachEvent) {
        // Cleanup
    //    thread.interrupt();
    //    thread = null;
    //}
}