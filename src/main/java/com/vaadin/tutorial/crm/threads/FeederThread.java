package com.vaadin.tutorial.crm.threads;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Label;
import com.vaadin.tutorial.crm.service.plccontrollersservice.SchedulerService;

/**
 * Класс поток - проверяет подключение к контроллерам каждые 10 сек.
 */
public class FeederThread extends Thread {
    private final UI ui;
    private  final Label element;

    private int count = 30;


    public FeederThread(UI ui, Label element) {
        this.ui = ui;
        this.element = element;
    }

    @Override
    public void run() {
        while (true){ //count>-1
            try {
                Thread.sleep(1000);
                ui.access(()-> {
                    element.setText(String.valueOf(SchedulerService.controllerConnected));
                    ui.push();
                });
                count--;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
