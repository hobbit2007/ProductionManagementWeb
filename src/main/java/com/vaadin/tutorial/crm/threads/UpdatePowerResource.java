package com.vaadin.tutorial.crm.threads;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.tutorial.crm.entity.powerresources.PowerResources;
import com.vaadin.tutorial.crm.service.powerresources.PowerResourcesService;

import java.util.List;

/**
 * Класс поток реализующий логику вычислений показаний энергоресурсов и запись результатов вычислений в базу
 */
public class UpdatePowerResource extends Thread{
    private final UI ui;
    private final Dialog dialog;
    private final long coefficient1;
    private final long coefficient2;
    private final PowerResourcesService powerResourcesService;

    public UpdatePowerResource(UI ui, Dialog dialog, long coefficient1, long coefficient2, PowerResourcesService powerResourcesService) {
        this.ui = ui;
        this.dialog = dialog;
        this.coefficient1 = coefficient1;
        this.coefficient2 = coefficient2;
        this.powerResourcesService = powerResourcesService;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            synchronized (this) {
                //Получаем ежедневную разницу показаний между текущими и предыдущими для воды
                List<PowerResources> listWater = powerResourcesService.getAllByResourceId(1L);

                int indexWater = 0;
                if (listWater.size() != 0 && listWater.size() >= 2) {
                    //if (listWater.size() <= 56) {
                    for (int j = 0; j < listWater.size(); j++) {
                        indexWater += 1;
                        double varWater;
                        if (indexWater < listWater.size()) {
                            varWater = listWater.get(indexWater).getValue() - listWater.get(j).getValue();
                            //System.out.println("VALUE FOR UPDATE ID = " + listWater.get(indexWater).getId() + " VALUE = " + varWater);
                            powerResourcesService.updateValueWeekly(listWater.get(indexWater).getId(), varWater);
                        }
                    }
                }

                //Получаем еженедельную сумму показаний для газа
                List<PowerResources> listGas = powerResourcesService.getAllByResourceId(4L);

                int indexGas = 0;
                double varGas = 0;
                if (listGas.size() != 0 && listGas.size() >= 2) {
                    if (listGas.size() <= 56) {
                        for (int j = 0; j < listGas.size(); j++) {
                            indexGas += 1;
                            if (indexGas < listGas.size()) {
                                varGas += listGas.get(j).getValue();
                                if (indexGas % 7 == 0) {
                                    //System.out.println("VALUE FOR UPDATE GAS ID = " + listGas.get(j).getId() + " VALUE GAS = " + varGas);
                                    powerResourcesService.updateValueWeekly(listGas.get(j).getId(), varGas);
                                    varGas = 0;
                                }
                            }
                        }
                    }
                    if (listGas.size() > 56 && listGas.size() % 7 == 0) {
                        int indexGas1 = listGas.size() - 56;
                        double varGas1 = 0;
                        for (int j = listGas.size() - 56; j < listGas.size(); j++) {//Обрабатываем только часть массива содержащую 60 записей
                            indexGas1 += 1;
                            if (indexGas1 < listGas.size()) {
                                varGas1 += listGas.get(indexGas1).getValue();//listGas.get(j).getValue() + listGas.get(j - 1).getValue();
                                if (j % 7 == 0) {
                                    //System.out.println("VALUE FOR UPDATE2 GAS ID = " + listGas.get(j).getId() + " VALUE GAS = " + varGas1);
                                    powerResourcesService.updateValueWeekly(listGas.get(j).getId(), varGas1);
                                    varGas1 = 0;
                                }
                            }
                        }
                    }
                }

                //Получаем разницу показаний текущей недели и предыдущей для газа
                List<PowerResources> listGasWeekly = powerResourcesService.getAllByResourceId(4L);

                int indexGasWeekly = 0;
                double varGasWeekly;
                if (listGasWeekly.size() != 0 && listGasWeekly.size() >= 2) {
                    if (listGasWeekly.size() <= 56) {
                        for (int j = 0; j < listGasWeekly.size(); j++) {
                            indexGasWeekly += 1;
                            if (indexGasWeekly < listGasWeekly.size()) {
                                if (indexGasWeekly % 14 == 0) {
                                    varGasWeekly = listGasWeekly.get(j).getValue() - listGasWeekly.get(j - 7).getValue();
                                    //System.out.println("VALUE FOR UPDATE GAS WEEKLY ID = " + listGasWeekly.get(j).getId() + " VALUE GAS WEEKLY = " + varGasWeekly);
                                    powerResourcesService.updateTotalValueWeekly(listGasWeekly.get(j).getId(), varGasWeekly);
                                    varGasWeekly = 0;
                                }
                            }
                        }
                    }
                    if (listGasWeekly.size() > 56 && listGasWeekly.size() % 14 == 0) {
                        int indexGasWeekly1 = listGasWeekly.size() - 56;
                        double varGasWeekly1 = 0;
                        for (int j = listGasWeekly.size() - 56; j < listGasWeekly.size(); j++) {//Обрабатываем только часть массива содержащую 60 записей
                            indexGasWeekly1 += 1;
                            if (indexGasWeekly1 < listGasWeekly.size()) {
                                varGasWeekly1 += listGasWeekly.get(indexGasWeekly1).getValue();//listGas.get(j).getValue() + listGas.get(j - 1).getValue();
                                if (j % 14 == 0) {
                                    //System.out.println("VALUE FOR UPDATE2 GAS ID = " + listGasWeekly.get(j).getId() + " VALUE GAS = " + varGasWeekly1);
                                    powerResourcesService.updateTotalValueWeekly(listGasWeekly.get(j).getId(), varGasWeekly1);
                                    varGasWeekly1 = 0;
                                }
                            }
                        }
                    }
                }

                //Получаем разницу показаний между текущими и предыдущими для стоков
                List<PowerResources> listStock = powerResourcesService.getAllByResourceId(10L);

                int indexStock = 0;
                if (listStock.size() != 0 && listStock.size() >= 2) {
                    //if (listWater.size() <= 56) {
                    for (int j = 0; j < listStock.size(); j++) {
                        indexStock += 1;
                        double varStock;
                        if (indexStock < listStock.size()) {
                            varStock = listStock.get(indexStock).getValue() - listStock.get(j).getValue();
                            //System.out.println("VALUE FOR UPDATE ID = " + listStock.get(indexStock).getId() + " VALUE = " + varStock);
                            powerResourcesService.updateValueWeekly(listStock.get(indexStock).getId(), varStock);
                        }
                    }
                }

                //Получаем разницу показаний между текущими и предыдущими для ввода №1
                List<PowerResources> listEnter1 = powerResourcesService.getAllByResourceId(5L);

                int indexEnter1 = 0;
                if (listEnter1.size() != 0 && listEnter1.size() >= 2) {
                    //if (listEnter1.size() < 61) {
                    for (int j = 0; j < listEnter1.size(); j++) {
                        indexEnter1 += 1;
                        double varEnter1;
                        if (indexEnter1 < listEnter1.size()) {
                            varEnter1 = listEnter1.get(indexEnter1).getValue() - listEnter1.get(j).getValue();
                            //System.out.println("VALUE FOR UPDATE ENTER1 ID = " + listEnter1.get(indexEnter1).getId() + " VALUE ENTER1 = " + varEnter1 * coefficient1);
                            powerResourcesService.updateValueWeekly(listEnter1.get(indexEnter1).getId(), varEnter1 * coefficient1);
                        }
                    }
                }

                //Получаем разницу показаний между текущими и предыдущими для ввода №2
                List<PowerResources> listEnter2 = powerResourcesService.getAllByResourceId(6L);

                int indexEnter2 = 0;
                if (listEnter2.size() != 0 && listEnter2.size() >= 2) {
                    //if (listEnter1.size() < 61) {
                    for (int j = 0; j < listEnter2.size(); j++) {
                        indexEnter2 += 1;
                        double varEnter2;
                        if (indexEnter2 < listEnter2.size()) {
                            varEnter2 = listEnter2.get(indexEnter2).getValue() - listEnter2.get(j).getValue();
                            //System.out.println("VALUE FOR UPDATE ENTER2 ID = " + listEnter2.get(indexEnter2).getId() + " VALUE ENTER2 = " + varEnter2 * coefficient2);
                            powerResourcesService.updateValueWeekly(listEnter2.get(indexEnter2).getId(), varEnter2 * coefficient2);
                        }
                    }
                }

                //Получаем разницу показаний между текущими и предыдущими для суммарной электроэнергии
                List<PowerResources> listTotalElectric = powerResourcesService.getAllByResourceId(9L);

                int indexTotalElectric = 0;
                if (listTotalElectric.size() != 0 && listTotalElectric.size() >= 2) {
                    //if (listEnter1.size() < 61) {
                    for (int j = 0; j < listTotalElectric.size(); j++) {
                        indexTotalElectric += 1;
                        double varTotalElectric = 0;
                        if (indexTotalElectric < listTotalElectric.size()) {
                            varTotalElectric = listTotalElectric.get(indexTotalElectric).getValue() - listTotalElectric.get(j).getValue();
                            //System.out.println("VALUE FOR UPDATE TOTAL ELECTRIC ID = " + listTotalElectric.get(indexTotalElectric).getId() + " VALUE TOTAL = " + varTotalElectric);
                            powerResourcesService.updateTotalValueWeekly(listTotalElectric.get(indexTotalElectric).getId(), varTotalElectric);
                            powerResourcesService.updateValueWeekly(listTotalElectric.get(indexTotalElectric).getId(), varTotalElectric);
                        }
                    }
                }
                ui.access(() -> {
                    dialog.close();
                    ui.push();
                });
                interrupt();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
