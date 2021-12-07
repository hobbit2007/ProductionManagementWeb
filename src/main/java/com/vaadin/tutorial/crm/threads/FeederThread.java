package com.vaadin.tutorial.crm.threads;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Label;
import com.vaadin.tutorial.crm.service.plccontrollersservice.PLCConnect;

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
        while (!interrupted()){ //count>-1
            try {
                Thread.sleep(5000);
                ui.access(()-> {
                    element.setText(String.valueOf(PLCConnect.anyControllersStatus()));
                    element.getStyle().set("color", "red");
                    element.getStyle().set("font-weight", "bold");
                    element.getStyle().set("font-size", "11pt");
                    ui.push();
                });
                count--;
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
