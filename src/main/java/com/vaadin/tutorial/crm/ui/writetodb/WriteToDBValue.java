package com.vaadin.tutorial.crm.ui.writetodb;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.service.plccontrollersservice.*;
import com.vaadin.tutorial.crm.service.writetodb.WriteToDBService;
import com.vaadin.tutorial.crm.threads.*;
import com.vaadin.tutorial.crm.ui.component.AnyComponent;
import com.vaadin.tutorial.crm.ui.layout.AdminLayout;

/**
 * Класс реализующий запись значений переменных из ПЛК контроллеров в БД
 */
@Route(value = "writetodb", layout = AdminLayout.class)
@PageTitle("Управление записью в БД | Система управления производством")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class WriteToDBValue extends VerticalLayout {
    VerticalLayout vMain = new VerticalLayout();

    Label alarmWashing = new Label();
    Label header1Washing = new Label();
    Label alarmDiffusion = new Label();
    Label header1Diffusion = new Label();
    Label alarmFermentation = new Label();
    Label header1Fermentation = new Label();
    Label alarmResidue = new Label();
    Label header1Residue = new Label();
    Label alarmBottling = new Label();
    Label header1Bottling = new Label();
    Label alarmDrying = new Label();
    Label header1Drying = new Label();
    Label timeLabelWashing = new Label();
    Label timeLabelDiffusion = new Label();
    Label timeLabelFermentation = new Label();
    Label timeLabelResidue = new Label();
    Label timeLabelBottling = new Label();
    Label timeLabelDrying = new Label();
    NumberField timeWashing = new NumberField();
    RadioButtonGroup<String> radioButtonWashing = new RadioButtonGroup<>();
    Thread recordToDBWashing = new Thread();

    NumberField timeDiffusion = new NumberField();
    RadioButtonGroup<String> radioButtonDiffusion = new RadioButtonGroup<>();
    Thread recordToDBDiffusion = new Thread();
    NumberField timeFermentation = new NumberField();
    RadioButtonGroup<String> radioButtonFermentation = new RadioButtonGroup<>();
    Thread recordToDBFermentation = new Thread();
    NumberField timeResidue = new NumberField();
    RadioButtonGroup<String> radioButtonResidue = new RadioButtonGroup<>();
    Thread recordToDBResidue = new Thread();
    NumberField timeBottling = new NumberField();
    RadioButtonGroup<String> radioButtonBottling = new RadioButtonGroup<>();
    Thread recordToDBBottling = new Thread();
    NumberField timeDrying = new NumberField();
    RadioButtonGroup<String> radioButtonDrying = new RadioButtonGroup<>();
    Thread recordToDBDrying = new Thread();

    private final WriteToDBService writeToDBService;
    private final SignalListService signalListService;
    private final PlcValueService plcValueService;

    public WriteToDBValue(WriteToDBService writeToDBService, SignalListService signalListService, PlcValueService plcValueService,
                          PlcDiffusioService plcDiffusioService, PlcFermentationService plcFermentationService,
                          PlcResidueService plcResidueService, PlcBottlingService plcBottlingService, PlcDryingService plcDryingService) {
        this.writeToDBService = writeToDBService;
        this.signalListService = signalListService;
        this.plcValueService = plcValueService;
        radioButtonWashing.setItems("Нет", "Да");
        radioButtonWashing.getStyle().set("color", "#d3b342");
        radioButtonWashing.setValue(writeToDBService.getAll().get(0).getWriteOff());
        radioButtonDiffusion.setItems("Нет", "Да");
        radioButtonDiffusion.getStyle().set("color", "#d3b342");
        radioButtonDiffusion.setValue(writeToDBService.getAll().get(1).getWriteOff());
        radioButtonFermentation.setItems("Нет", "Да");
        radioButtonFermentation.getStyle().set("color", "#d3b342");
        radioButtonFermentation.setValue(writeToDBService.getAll().get(2).getWriteOff());
        radioButtonResidue.setItems("Нет", "Да");
        radioButtonResidue.getStyle().set("color", "#d3b342");
        radioButtonResidue.setValue(writeToDBService.getAll().get(3).getWriteOff());
        radioButtonBottling.setItems("Нет", "Да");
        radioButtonBottling.getStyle().set("color", "#d3b342");
        radioButtonBottling.setValue(writeToDBService.getAll().get(4).getWriteOff());
        radioButtonDrying.setItems("Нет", "Да");
        radioButtonDrying.getStyle().set("color", "#d3b342");
        radioButtonDrying.setValue(writeToDBService.getAll().get(5).getWriteOff());

        header1Washing.setText(writeToDBService.getAll().get(0).getDescription());
        header1Washing.getStyle().set("color", "#d3b342");
        header1Washing.getStyle().set("font-weight", "bold");
        header1Washing.getStyle().set("font-size", "13pt");

        header1Diffusion.setText(writeToDBService.getAll().get(1).getDescription());
        header1Diffusion.getStyle().set("color", "#d3b342");
        header1Diffusion.getStyle().set("font-weight", "bold");
        header1Diffusion.getStyle().set("font-size", "13pt");

        header1Fermentation.setText(writeToDBService.getAll().get(2).getDescription());
        header1Fermentation.getStyle().set("color", "#d3b342");
        header1Fermentation.getStyle().set("font-weight", "bold");
        header1Fermentation.getStyle().set("font-size", "13pt");

        header1Residue.setText(writeToDBService.getAll().get(3).getDescription());
        header1Residue.getStyle().set("color", "#d3b342");
        header1Residue.getStyle().set("font-weight", "bold");
        header1Residue.getStyle().set("font-size", "13pt");

        header1Bottling.setText(writeToDBService.getAll().get(4).getDescription());
        header1Bottling.getStyle().set("color", "#d3b342");
        header1Bottling.getStyle().set("font-weight", "bold");
        header1Bottling.getStyle().set("font-size", "13pt");

        header1Drying.setText(writeToDBService.getAll().get(5).getDescription());
        header1Drying.getStyle().set("color", "#d3b342");
        header1Drying.getStyle().set("font-weight", "bold");
        header1Drying.getStyle().set("font-size", "13pt");

        timeLabelWashing.setText("Время повтора, мин.:");
        timeLabelWashing.getStyle().set("color", "#d3b342");
        timeLabelWashing.getStyle().set("font-weight", "bold");
        timeLabelWashing.getStyle().set("font-size", "13pt");
        timeLabelDiffusion.setText("Время повтора, мин.:");
        timeLabelDiffusion.getStyle().set("color", "#d3b342");
        timeLabelDiffusion.getStyle().set("font-weight", "bold");
        timeLabelDiffusion.getStyle().set("font-size", "13pt");
        timeLabelFermentation.setText("Время повтора, мин.:");
        timeLabelFermentation.getStyle().set("color", "#d3b342");
        timeLabelFermentation.getStyle().set("font-weight", "bold");
        timeLabelFermentation.getStyle().set("font-size", "13pt");
        timeLabelResidue.setText("Время повтора, мин.:");
        timeLabelResidue.getStyle().set("color", "#d3b342");
        timeLabelResidue.getStyle().set("font-weight", "bold");
        timeLabelResidue.getStyle().set("font-size", "13pt");
        timeLabelBottling.setText("Время повтора, мин.:");
        timeLabelBottling.getStyle().set("color", "#d3b342");
        timeLabelBottling.getStyle().set("font-weight", "bold");
        timeLabelBottling.getStyle().set("font-size", "13pt");
        timeLabelDrying.setText("Время повтора, мин.:");
        timeLabelDrying.getStyle().set("color", "#d3b342");
        timeLabelDrying.getStyle().set("font-weight", "bold");
        timeLabelDrying.getStyle().set("font-size", "13pt");

        timeWashing.setWidth("45px");
        timeWashing.setValue((double) writeToDBService.getAll().get(0).getRepeatTime());
        timeDiffusion.setWidth("45px");
        timeDiffusion.setValue((double) writeToDBService.getAll().get(1).getRepeatTime());
        timeFermentation.setWidth("45px");
        timeFermentation.setValue((double) writeToDBService.getAll().get(2).getRepeatTime());
        timeResidue.setWidth("45px");
        timeResidue.setValue((double) writeToDBService.getAll().get(3).getRepeatTime());
        timeBottling.setWidth("45px");
        timeBottling.setValue((double) writeToDBService.getAll().get(4).getRepeatTime());
        timeDrying.setWidth("45px");
        timeDrying.setValue((double) writeToDBService.getAll().get(5).getRepeatTime());

        if (radioButtonWashing.getValue().equals("Нет")) {
            alarmDisable(alarmWashing);
        }
        else {
            alarmEnabled(alarmWashing);
        }
        if (radioButtonDiffusion.getValue().equals("Нет")) {
            alarmDisable(alarmDiffusion);
        }
        else {
            alarmEnabled(alarmDiffusion);
        }
        if (radioButtonFermentation.getValue().equals("Нет")) {
            alarmDisable(alarmFermentation);
        }
        else {
            alarmEnabled(alarmFermentation);
        }
        if (radioButtonResidue.getValue().equals("Нет")) {
            alarmDisable(alarmResidue);
        }
        else {
            alarmEnabled(alarmResidue);
        }
        if (radioButtonBottling.getValue().equals("Нет")) {
            alarmDisable(alarmBottling);
        }
        else {
            alarmEnabled(alarmBottling);
        }
        if (radioButtonDrying.getValue().equals("Нет")) {
            alarmDisable(alarmDrying);
        }
        else {
            alarmEnabled(alarmDrying);
        }

        radioButtonWashing.addValueChangeListener(e -> {
           if (e.getValue().equals("Да")) {
               alarmEnabled(alarmWashing);
               writeToDBService.updateWritePLC("Да", 1L);
               recordToDBWashing = new StartRecordForPLC(signalListService.findSignalList(1L), PLCConnect.clientForStatusWashing,
                       timeWashing.getValue().longValue() * 60 * 1000, plcValueService, 1L);
               recordToDBWashing.start();
           }
           if (e.getValue().equals("Нет")) {
               recordToDBWashing.interrupt();
               recordToDBWashing = null;
               alarmDisable(alarmWashing);
               writeToDBService.updateWritePLC("Нет", 1L);
           }
        });
        radioButtonDiffusion.addValueChangeListener(e -> {
            if (e.getValue().equals("Да")) {
                alarmEnabled(alarmDiffusion);
                writeToDBService.updateWritePLC("Да", 2L);
                recordToDBDiffusion = new StartRecordForDiffusion(signalListService.findSignalList(2L), PLCConnect.clientForStatusDiffusion,
                        timeDiffusion.getValue().longValue() * 60 * 1000, plcDiffusioService);
                recordToDBDiffusion.start();
            }
            if (e.getValue().equals("Нет")) {
                recordToDBDiffusion.interrupt();
                recordToDBDiffusion = null;
                alarmDisable(alarmDiffusion);
                writeToDBService.updateWritePLC("Нет", 2L);
            }
        });
        radioButtonFermentation.addValueChangeListener(e -> {
            if (e.getValue().equals("Да")) {
                alarmEnabled(alarmFermentation);
                writeToDBService.updateWritePLC("Да", 3L);
                recordToDBFermentation = new StartRecordForFermentation(signalListService.findSignalList(3L), PLCConnect.clientForStatusFermentation,
                        timeFermentation.getValue().longValue() * 60 * 1000, plcFermentationService);
                recordToDBFermentation.start();
            }
            if (e.getValue().equals("Нет")) {
                recordToDBFermentation.interrupt();
                recordToDBFermentation = null;
                alarmDisable(alarmFermentation);
                writeToDBService.updateWritePLC("Нет", 3L);
            }
        });
        radioButtonResidue.addValueChangeListener(e -> {
            if (e.getValue().equals("Да")) {
                alarmEnabled(alarmResidue);
                writeToDBService.updateWritePLC("Да", 4L);
                recordToDBResidue = new StartRecordForResidue(signalListService.findSignalList(4L), PLCConnect.clientForStatus,
                        timeResidue.getValue().longValue() * 60 * 1000, plcResidueService);
                recordToDBResidue.start();
            }
            if (e.getValue().equals("Нет")) {
                recordToDBResidue.interrupt();
                recordToDBResidue = null;
                alarmDisable(alarmResidue);
                writeToDBService.updateWritePLC("Нет", 4L);
            }
        });
        radioButtonBottling.addValueChangeListener(e -> {
            if (e.getValue().equals("Да")) {
                alarmEnabled(alarmBottling);
                writeToDBService.updateWritePLC("Да", 5L);
                recordToDBBottling = new StartRecordForBottling(signalListService.findSignalList(5L), PLCConnect.clientForStatusBottling,
                        timeBottling.getValue().longValue() * 60 * 1000, plcBottlingService);
                recordToDBBottling.start();
            }
            if (e.getValue().equals("Нет")) {
                recordToDBBottling.interrupt();
                recordToDBBottling = null;
                alarmDisable(alarmBottling);
                writeToDBService.updateWritePLC("Нет", 5L);
            }
        });
        radioButtonDrying.addValueChangeListener(e -> {
            if (e.getValue().equals("Да")) {
                alarmEnabled(alarmDrying);
                writeToDBService.updateWritePLC("Да", 6L);
                recordToDBDrying = new StartRecordForDrying(signalListService.findSignalList(6L), PLCConnect.clientForStatusDrying,
                        timeDrying.getValue().longValue() * 60 * 1000, plcDryingService);
                recordToDBDrying.start();
            }
            if (e.getValue().equals("Нет")) {
                recordToDBDrying.interrupt();
                recordToDBDrying = null;
                alarmDisable(alarmDrying);
                writeToDBService.updateWritePLC("Нет", 6L);
            }
        });

        timeWashing.addValueChangeListener(e -> {
           if (e.getValue() != null) {
               if (!timeWashing.isEmpty())
                   writeToDBService.updateRTPLC(timeWashing.getValue().longValue(), 1L);
           }
        });
        timeDiffusion.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                if (!timeDiffusion.isEmpty())
                    writeToDBService.updateRTPLC(timeDiffusion.getValue().longValue(), 2L);
            }
        });
        timeFermentation.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                if (!timeFermentation.isEmpty())
                    writeToDBService.updateRTPLC(timeFermentation.getValue().longValue(), 3L);
            }
        });
        timeResidue.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                if (!timeResidue.isEmpty())
                    writeToDBService.updateRTPLC(timeResidue.getValue().longValue(), 4L);
            }
        });
        timeBottling.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                if (!timeBottling.isEmpty())
                    writeToDBService.updateRTPLC(timeBottling.getValue().longValue(), 5L);
            }
        });
        timeDrying.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                if (!timeDrying.isEmpty())
                    writeToDBService.updateRTPLC(timeDrying.getValue().longValue(), 6L);
            }
        });

        FormLayout formLayout = new FormLayout();
        FormLayout formLayout1 = new FormLayout();
        FormLayout formLayout2 = new FormLayout();
        FormLayout formLayout3 = new FormLayout();
        FormLayout formLayout4 = new FormLayout();
        FormLayout formLayout5 = new FormLayout();

        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 5));
        formLayout.add(header1Washing, radioButtonWashing, timeLabelWashing, timeWashing, alarmWashing);

        formLayout1.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 5));
        formLayout1.add(header1Diffusion, radioButtonDiffusion, timeLabelDiffusion, timeDiffusion, alarmDiffusion);

        formLayout2.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 5));
        formLayout2.add(header1Fermentation, radioButtonFermentation, timeLabelFermentation, timeFermentation, alarmFermentation);

        formLayout3.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 5));
        formLayout3.add(header1Residue, radioButtonResidue, timeLabelResidue, timeResidue, alarmResidue);

        formLayout4.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 5));
        formLayout4.add(header1Bottling, radioButtonBottling, timeLabelBottling, timeBottling, alarmBottling);

        formLayout5.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 5));
        formLayout5.add(header1Drying, radioButtonDrying, timeLabelDrying, timeDrying, alarmDrying);

        vMain.add(new AnyComponent().labelTitle("Управление записью в БД"), formLayout, formLayout1, formLayout2, formLayout3, formLayout4, formLayout5);
        vMain.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        add(vMain);
    }
    private void alarmDisable(Label label) {
        label.setText(" - Запись отключена");
        label.getStyle().set("color", "red");
        label.getStyle().set("font-weight", "bold");
        label.getStyle().set("font-size", "14pt");
    }
    private void alarmEnabled(Label label) {
        label.setText(" - Запись включена");
        label.getStyle().set("color", "green");
        label.getStyle().set("font-weight", "bold");
        label.getStyle().set("font-size", "14pt");
    }
}
