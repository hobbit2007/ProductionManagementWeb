package com.vaadin.tutorial.crm.service.plccontrollersservice;

import com.sourceforge.snap7.moka7.*;
import com.vaadin.tutorial.crm.entity.plccontrollersentity.PlcControllers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс проверяющий есть ли подключение к ПЛК контроллерам
 */
@Slf4j
@Service
public class PLCConnect {

    static PlcControllersService plcControllersService;
    public static S7Client[] client = new S7Client[100];
    public static S7Client clientForStatus;
    public static S7Client clientForStatusWashing;
    private static List<PlcControllers> plcControllersList = new ArrayList<>();
    public static String controllerConnected = "";
    private static StringBuilder numController = new StringBuilder();
    private static boolean contrConnected;
    private static boolean contrConnectedWashing;

    @Autowired
    public PLCConnect(PlcControllersService plcControllersService) {
        this.plcControllersService = plcControllersService;

        plcControllersList = plcControllersService.getAll();

        for (int i = 0; i < plcControllersList.size(); i++) {
            client[i] = new S7Client();
            client[i].SetConnectionType(S7.OP);
            client[i].ConnectTo(plcControllersList.get(i).getIp(), 0, 1);
        }


    }

    public static String anyControllersStatus() {
        plcControllersList = plcControllersService.getAll();
        numController.setLength(0);
        for (int i = 0; i < plcControllersList.size(); i++) {
            client[i].ConnectTo(plcControllersList.get(i).getIp(), 0, 1);
            if (!client[i].Connected) {
                numController.append(plcControllersList.get(i).getIp() + " - " + plcControllersList.get(i).getControllerName() + "  ");
                controllerConnected = "Нет подключения к контроллеру: " + numController;
            }
            client[i].Disconnect();
        }
        return controllerConnected;
    }

    public static boolean controllerStatus(String controllerIP) {
        clientForStatus = new S7Client();
        clientForStatus.SetConnectionType(S7.OP);
        clientForStatus.ConnectTo(controllerIP, 0, 1);
        if (clientForStatus.Connected)
            return contrConnected = true;
        else
            return contrConnected = false;
    }

    public static boolean controllerStatusWashing(String controllerIP) {
        clientForStatusWashing = new S7Client();
        clientForStatusWashing.SetConnectionType(S7.OP);
        clientForStatusWashing.ConnectTo(controllerIP, 0, 1);
        if (clientForStatusWashing.Connected)
            return contrConnectedWashing = true;
        else
            return contrConnectedWashing = false;
    }

    public static void controllerDisconnect() {
        if (clientForStatus.Connected)
            clientForStatus.Disconnect();
    }
}
