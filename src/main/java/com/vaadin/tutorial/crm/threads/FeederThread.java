package com.vaadin.tutorial.crm.threads;

import com.sourceforge.snap7.moka7.S7;
import com.sourceforge.snap7.moka7.S7Client;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Label;
import com.vaadin.tutorial.crm.entity.plccontrollersentity.PlcControllers;
import com.vaadin.tutorial.crm.service.plccontrollersservice.PLCConnect;

import java.util.List;

/**
 * Класс поток - проверяет подключение к контроллерам каждые 3 сек.
 */
public class FeederThread extends Thread {
    //private final UI ui;
    //private  final Label element;
    public static S7Client[] client = new S7Client[100];
    List<PlcControllers> array;

    private int count = 30;


    public FeederThread(List<PlcControllers> array) { //UI ui, Label element
        //this.ui = ui;
        //this.element = element;
        this.array = array;
    }

    @Override
    public void run() {
        while (!interrupted()){ //count>-1
            try {
                for (int i = 0; i < array.size(); i++) {
                    client[i] = new S7Client();
                    client[i].SetConnectionType(S7.OP);
                    client[i].ConnectTo(array.get(i).getIp(), 0, 1);
                    if (!client[i].Connected) {
                        if (array.get(i).getId() == 1) {
                            PLCConnect.contrConnectedWashing = false;
                            System.out.println("WASHING DIS");
                        }
                        if (array.get(i).getId() == 2) {
                            PLCConnect.contrConnectedDiffusion = false;
                            System.out.println("Diffusion DIS");
                        }
                        if (array.get(i).getId() == 3) {
                            PLCConnect.contrConnectedFermentation = false;
                            System.out.println("Fermentation DIS");
                        }
                        if (array.get(i).getId() == 4) {
                            PLCConnect.contrConnected = false;
                            System.out.println("Resuide DIS");
                        }
                        if (array.get(i).getId() == 5) {
                            PLCConnect.contrConnectedBottling = false;
                            System.out.println("Bottling DIS");
                        }
                        if (array.get(i).getId() == 6) {
                            PLCConnect.contrConnectedDrying = false;
                            System.out.println("Drying DIS");
                        }
                    }
                    else {
                        if (array.get(i).getId() == 1) {
                            PLCConnect.contrConnectedWashing = true;
                            System.out.println("WASHING CON");
                        }
                        if (array.get(i).getId() == 2) {
                            PLCConnect.contrConnectedDiffusion = true;
                            System.out.println("Diffusion CON");
                        }
                        if (array.get(i).getId() == 3) {
                            PLCConnect.contrConnectedFermentation = true;
                            System.out.println("Fermentation CON");
                        }
                        if (array.get(i).getId() == 4) {
                            PLCConnect.contrConnected = true;
                            System.out.println("Resuide CON");
                        }
                        if (array.get(i).getId() == 5) {
                            PLCConnect.contrConnectedBottling = true;
                            System.out.println("Bottling CON");
                        }
                        if (array.get(i).getId() == 6) {
                            PLCConnect.contrConnectedDrying = true;
                            System.out.println("Drying CON");
                        }
                    }
                    client[i].Disconnect();
                }
                sleep(3000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
