package com.vaadin.tutorial.crm.threads;

import com.sourceforge.snap7.moka7.S7;
import com.sourceforge.snap7.moka7.S7Client;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.tutorial.crm.entity.for1c.For1CEntity;
import com.vaadin.tutorial.crm.entity.for1c.For1CSignalListEntity;
import com.vaadin.tutorial.crm.service.for1c.For1CService;

import java.util.Date;
import java.util.List;

/**
 * Класс поток запускает запись в БД данных для передачи в 1С
 */
public class StartRecordFor1C extends Thread{
    private List<For1CSignalListEntity> array;
    S7Client s7ClientWashing;
    S7Client s7ClientDiffusion;
    S7Client s7ClientFermentation;
    S7Client s7Client;
    S7Client s7ClientBottling;
    S7Client s7ClientDrying;

    byte[] buffer = new byte[65536];
    Long timeRepeat;
    private final For1CService for1CService;
    public StartRecordFor1C(List<For1CSignalListEntity> array, S7Client s7ClientWashing, S7Client s7ClientDiffusion,
                            S7Client s7ClientFermentation, S7Client s7Client, S7Client s7ClientBottling,
                            S7Client s7ClientDrying, Long timeRepeat, For1CService for1CService) {
        this.array = array;
        this.s7ClientWashing = s7ClientWashing;
        this.s7ClientDiffusion = s7ClientDiffusion;
        this.s7ClientFermentation = s7ClientFermentation;
        this.s7Client = s7Client;
        this.s7ClientBottling = s7ClientBottling;
        this.s7ClientDrying = s7ClientDrying;
        this.timeRepeat = timeRepeat;
        this.for1CService = for1CService;
    }
    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                Thread.sleep(timeRepeat);
                synchronized (this) {
                    float readData = -1.00f;
                    for (int i = 0; i < array.size(); i++) {
                        if (array.get(i).getSignalList().getIdController() == 1) {
                            s7ClientWashing.ReadArea(S7.S7AreaDB, array.get(i).getSignalList().getDbValue(), 0,
                                    array.get(i).getSignalList().getPosition() + array.get(i).getSignalList().getFOffset(), buffer);
                            readData = S7.GetFloatAt(buffer, array.get(i).getSignalList().getPosition());
                        }
                        if (array.get(i).getSignalList().getIdController() == 2) {
                            s7ClientDiffusion.ReadArea(S7.S7AreaDB, array.get(i).getSignalList().getDbValue(), 0,
                                    array.get(i).getSignalList().getPosition() + array.get(i).getSignalList().getFOffset(), buffer);
                            readData = S7.GetFloatAt(buffer, array.get(i).getSignalList().getPosition());
                        }
                        if (array.get(i).getSignalList().getIdController() == 3) {
                            s7ClientFermentation.ReadArea(S7.S7AreaDB, array.get(i).getSignalList().getDbValue(), 0,
                                    array.get(i).getSignalList().getPosition() + array.get(i).getSignalList().getFOffset(), buffer);
                            readData = S7.GetFloatAt(buffer, array.get(i).getSignalList().getPosition());
                        }
                        if (array.get(i).getSignalList().getIdController() == 4) {
                            s7Client.ReadArea(S7.S7AreaDB, array.get(i).getSignalList().getDbValue(), 0,
                                    array.get(i).getSignalList().getPosition() + array.get(i).getSignalList().getFOffset(), buffer);
                            readData = S7.GetFloatAt(buffer, array.get(i).getSignalList().getPosition());
                        }
                        if (array.get(i).getSignalList().getIdController() == 5) {
                            s7ClientBottling.ReadArea(S7.S7AreaDB, array.get(i).getSignalList().getDbValue(), 0,
                                    array.get(i).getSignalList().getPosition() + array.get(i).getSignalList().getFOffset(), buffer);
                            readData = S7.GetFloatAt(buffer, array.get(i).getSignalList().getPosition());
                        }
                        if (array.get(i).getSignalList().getIdController() == 6) {
                            s7ClientDrying.ReadArea(S7.S7AreaDB, array.get(i).getSignalList().getDbValue(), 0,
                                    array.get(i).getSignalList().getPosition() + array.get(i).getSignalList().getFOffset(), buffer);
                            readData = S7.GetFloatAt(buffer, array.get(i).getSignalList().getPosition());
                        }
                        double scale = Math.pow(10, 2);
                        For1CEntity for1CEntity = new For1CEntity();
                        for1CEntity.setIdOrderNum(1);
                        for1CEntity.setDelete(0);
                        for1CEntity.setValue((float) (Math.ceil(readData * scale) / scale)); //Precision.round(readData, 2) - округляем до двух знаков после запятой
                        for1CEntity.setUnits("кг.");
                        for1CEntity.setDatetime(new Date());
                        for1CEntity.setIdSignal(array.get(i).getId());

                        try {
                            for1CService.saveAll(for1CEntity);
                            //System.out.println("RECORD ADD TO DB");
                        }
                        catch (Exception ex) {
                            Notification.show("Не могу записать в БД данные для 1С" + ex.getMessage(), 5000, Notification.Position.MIDDLE);
                            return;
                        }
                    }
                }
            }
            catch (InterruptedException e) {
                break;
            }
        }
    }
}
