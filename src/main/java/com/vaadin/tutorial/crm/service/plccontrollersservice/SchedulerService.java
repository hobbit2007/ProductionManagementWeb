package com.vaadin.tutorial.crm.service.plccontrollersservice;

import com.sourceforge.snap7.moka7.S7;
import com.sourceforge.snap7.moka7.S7Client;
import com.vaadin.tutorial.crm.entity.plccontrollersentity.PlcControllers;
import com.vaadin.tutorial.crm.entity.plccontrollersentity.SignalList;
import com.vaadin.tutorial.crm.model.DataFromPlc;
import com.vaadin.tutorial.crm.service.plccontrollersservice.PlcControllersService;
import com.vaadin.tutorial.crm.service.plccontrollersservice.SignalListService;
import com.vaadin.tutorial.crm.ui.MainView;
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
    public static final S7Client clientRT = new S7Client();
    public static final byte[] buffer = new byte[65536];
    public static final byte[] bufferWrite = new byte[65536];
    private List<PlcControllers> plcControllersList = new ArrayList<>();
    public static String controllerConnected = "";
    private StringBuilder numController = new StringBuilder();
    public static List<SignalList> numDbPosOffset = new ArrayList<>();
    public static List<DataFromPlc> dataFromPlcList = new ArrayList<>(); //Массив в котором сохраняем данные после чтения из контроллера


    @Autowired
    public SchedulerService(SignalListService signalListService, PlcControllersService plcControllersService) {
        this.signalListService = signalListService;
        this.plcControllersService = plcControllersService;

        plcControllersList = plcControllersService.getAll();

        for (int i = 0; i < plcControllersList.size(); i++) {
            client[i] = new S7Client();
            client[i].SetConnectionType(S7.OP);
            client[i].ConnectTo(plcControllersList.get(i).getIp(), 0, 1);
        }
    }

    @Scheduled(cron = CRON)
    public void sendsSignalRT() {
        for (int i = 0; i < plcControllersList.size(); i++) {
            if (!client[i].Connected) {
                numController.append(plcControllersList.get(i).getIp() + " - " + plcControllersList.get(i).getControllerName() + "  ");
                controllerConnected = "Нет подключения к контроллеру: " + numController;
            }
            numController.delete(0, numController.length() - 1);
        }
        if (numDbPosOffset.size() != 0) {
            DataFromPlc dataFromPlc = new DataFromPlc();
            for (int i = 0; i < numDbPosOffset.size(); i++) {
                clientRT.ReadArea(S7.S7AreaDB, numDbPosOffset.get(i).getDbValue(), numDbPosOffset.get(i).getPosition(), numDbPosOffset.get(i).getPosition() + numDbPosOffset.get(i).getOffset(), buffer);
                float readData = S7.GetFloatAt(buffer, 266);
                dataFromPlc.setSignalName(numDbPosOffset.get(i).getSignalName());
                dataFromPlc.setValue(readData);

                dataFromPlcList.add(dataFromPlc);
            }
        }
        //System.out.println("TEST CRON!!!");
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
