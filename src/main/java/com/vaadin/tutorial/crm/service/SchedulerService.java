package com.vaadin.tutorial.crm.service;

import com.sourceforge.snap7.moka7.S7;
import com.sourceforge.snap7.moka7.S7Client;
import com.vaadin.tutorial.crm.entity.plccontrollersentity.PlcControllers;
import com.vaadin.tutorial.crm.service.plccontrollersservice.PlcControllersService;
import com.vaadin.tutorial.crm.service.plccontrollersservice.SignalListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс работающий в фоновом режиме и опрашивающий существующие контроллеры
 */
@Slf4j
@Service
public class SchedulerService {
    SignalListService signalListService;
    PlcControllersService plcControllersService;
    private static final String CRON = "*/10 * * * * *";
    public static final S7Client[] client = new S7Client[100];
    public static final byte[] buffer = new byte[65536];
    public static final byte[] bufferWrite = new byte[65536];
    private List<PlcControllers> plcControllersList = new ArrayList<>();
    public static String controllerConnected = "";
    private StringBuilder numController;

    @Autowired
    public SchedulerService(SignalListService signalListService, PlcControllersService plcControllersService) {
        this.signalListService = signalListService;
        this.plcControllersService = plcControllersService;

        plcControllersList = plcControllersService.getAll();

        for (int i = 0; i < plcControllersList.size(); i++) {
            client[i].SetConnectionType(S7.OP);
            client[i].ConnectTo(plcControllersList.get(i).getIp(), 0, 1);
        }
    }

    @Scheduled(cron = CRON)
    public void sendsSignal() {
        for (int i = 0; i < plcControllersList.size(); i++) {
            if (!client[i].Connected) {
                numController.append(plcControllersList.get(i).getIp() + " ");
                controllerConnected = "Нет подключения к контроллеру: " + numController;
            }
        }
        System.out.println("TEST CRON!!!");
    }
}
