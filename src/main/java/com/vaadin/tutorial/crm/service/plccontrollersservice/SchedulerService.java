package com.vaadin.tutorial.crm.service.plccontrollersservice;

import com.sourceforge.snap7.moka7.*;
import com.vaadin.tutorial.crm.entity.plccontrollersentity.PlcControllers;
import com.vaadin.tutorial.crm.entity.plccontrollersentity.SignalList;
import com.vaadin.tutorial.crm.model.DataFromPlc;
import com.vaadin.tutorial.crm.service.plccontrollersservice.PlcControllersService;
import com.vaadin.tutorial.crm.service.plccontrollersservice.SignalListService;
import com.vaadin.tutorial.crm.ui.MainView;
import com.vaadin.tutorial.crm.ui.plccontrollersui.PlcValueController;
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
    static PlcControllersService plcControllersService;
    private static final String CRON = "*/2 * * * * *";
    public static S7Client[] client = new S7Client[100];
    //public static S7Client clientRT;
    public static S7Client clientForStatus;
    public static byte[] buffer = new byte[65536];
    public static byte[] bufferWrite = new byte[65536];
    private static List<PlcControllers> plcControllersList = new ArrayList<>();
    public static String controllerConnected = "";
    private static StringBuilder numController = new StringBuilder();
    public static List<SignalList> numDbPosOffset = new ArrayList<>();
    public static boolean stopThread = false;//Переменная останвливающая запуск потоков, в том случае, если мы ушли из окна Визуализации

    @Autowired
    public SchedulerService(SignalListService signalListService, PlcControllersService plcControllersService) {
        this.signalListService = signalListService;
        this.plcControllersService = plcControllersService;

        plcControllersList = plcControllersService.getAll();

        clientForStatus = new S7Client();
        clientForStatus.SetConnectionType(S7.OP);

        for (int i = 0; i < plcControllersList.size(); i++) {
            client[i] = new S7Client();
            client[i].SetConnectionType(S7.OP);
            client[i].ConnectTo(plcControllersList.get(i).getIp(), 0, 1);
            //Коннектимся ко всем доступным контроллерам
            clientForStatus.ConnectTo(plcControllersList.get(i).getIp(), 0, 1);
        }


    }

    @Scheduled(cron = CRON)
    public void sendsSignalRT() {
        if (clientForStatus.Connected) {
            List<DataFromPlc> dataFromPlcList = new ArrayList<>();
            dataFromPlcList.removeAll(dataFromPlcList);
            if (numDbPosOffset.size() != 0) {

                for (int i = 0; i < numDbPosOffset.size(); i++) {
                    DataFromPlc dataFromPlc = new DataFromPlc();
                    clientForStatus.ReadArea(S7.S7AreaDB, numDbPosOffset.get(i).getDbValue(), 0, numDbPosOffset.get(i).getPosition() + numDbPosOffset.get(i).getOffset(), buffer);
                    float readData = S7.GetFloatAt(buffer, numDbPosOffset.get(i).getPosition());
                    dataFromPlc.setSignalName(numDbPosOffset.get(i).getSignalName());
                    dataFromPlc.setValue(readData);

                    dataFromPlcList.add(dataFromPlc);
                }
            }
            if (dataFromPlcList.size() == numDbPosOffset.size() && dataFromPlcList.size() != 0 && numDbPosOffset.size() != 0 && !stopThread) {
                PlcValueController.startThread(dataFromPlcList);
                //System.out.println("TEST CRON!!!");
            }
        }
    }

    public static String anyControllersStatus() {
        plcControllersList = plcControllersService.getAll();
        numController.setLength(0);
        for (int i = 0; i < plcControllersList.size(); i++) {
            //client[i].ConnectTo(plcControllersList.get(i).getIp(), 0, 1);
            if (!client[i].Connected) {
                numController.append(plcControllersList.get(i).getIp() + " - " + plcControllersList.get(i).getControllerName() + "  ");
                controllerConnected = "Нет подключения к контроллеру: " + numController;
            }
            //client[i].Disconnect();
        }
        return controllerConnected;
    }

    public static boolean controllerStatus(String controllerIP) {
        clientForStatus.ConnectTo(controllerIP, 0, 1);
        if (clientForStatus.Connected)
            return true;
        else
            return false;
    }

    public static void controllerDisconnect() {
        if (clientForStatus.Connected)
            clientForStatus.Disconnect();
    }

    /**
     * Метод оживает данные из класса PlcValueController и заполняет List
     * из таблицы signsllist
     * @param controllerSignalList - массив переменных сигналов контроллера
     */
    public static void controllerParam(List<SignalList> controllerSignalList) {
        numDbPosOffset = controllerSignalList;
    }
}
