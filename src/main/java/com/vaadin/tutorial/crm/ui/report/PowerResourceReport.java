package com.vaadin.tutorial.crm.ui.report;

import ar.com.fdvs.dj.domain.AutoText;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.StyleBuilder;
import ar.com.fdvs.dj.domain.constants.Border;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.constants.VerticalAlign;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;
import com.vaadin.flow.function.SerializableSupplier;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.tutorial.crm.entity.powerresources.PowerReportModel;
import com.vaadin.tutorial.crm.entity.powerresources.PowerResources;
import com.vaadin.tutorial.crm.service.powerresources.PowerResourcesService;
import com.vaadin.tutorial.crm.ui.component.AnyComponent;
import com.vaadin.tutorial.crm.ui.layout.ReportLayout;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.vaadin.reports.PrintPreviewReport;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Класс отчет по показаниям энергоресурсов
 */
@Route(value = "powerreport", layout = ReportLayout.class)
@PageTitle("Отчет по показаниям энергоресурсов | Система управления производством")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class PowerResourceReport extends VerticalLayout {
    PowerResourcesService powerResourcesService;
    PowerReportModel powerReportModel;
    DatePicker dateBegin = new DatePicker("с:");
    DatePicker dateEnd = new DatePicker("по:");
    Button sortButton = new Button("Сортировка");
    Button allViewButton = new Button("Показать все");
    Anchor pdf;
    PrintPreviewReport<PowerResources> report;
    HorizontalLayout hSort = new HorizontalLayout();

    public PowerResourceReport(PowerResourcesService powerResourcesService, PowerReportModel powerReportModel) {
        this.powerResourcesService = powerResourcesService;
        this.powerReportModel = powerReportModel;

        dateBegin.setI18n(new AnyComponent().datePickerRus());
        dateEnd.setI18n(new AnyComponent().datePickerRus());

        hSort.add(new AnyComponent().labelTitle("Сортировка по дате:"), dateBegin, dateEnd, sortButton, allViewButton);
        hSort.setDefaultVerticalComponentAlignment(Alignment.BASELINE);

        reportGenerate(powerResourcesService.getAllByResourceId(powerReportModel.getId()));

        add(hSort, report, pdf);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        allViewButton.addClickListener(e -> {
            reportGenerate(powerResourcesService.getAllByResourceId(powerReportModel.getId()));
            removeAll();
            add(hSort, report, pdf);
        });

        sortButton.addClickListener(e -> {
            if (!dateBegin.isEmpty() && !dateEnd.isEmpty()) {
                if (java.sql.Date.valueOf(dateBegin.getValue()).getTime() <= java.sql.Date.valueOf(dateEnd.getValue()).getTime()) {
                    if (java.sql.Date.valueOf(dateEnd.getValue()).getTime() <= new java.util.Date().getTime()) {
                        reportGenerate(powerResourcesService.getResourceBySort(java.sql.Date.valueOf(dateBegin.getValue().toString()),
                            java.sql.Date.valueOf(dateEnd.getValue().toString()), powerReportModel.getId()));
                        removeAll();
                        add(hSort, report, pdf);
                    }
                    else {
                        Notification.show("Дата окончания не может быть больше текущей!", 3000, Notification.Position.MIDDLE);
                        return;
                    }
                }
                else {
                    Notification.show("Дата начала не может быть больше даты окончания!", 3000, Notification.Position.MIDDLE);
                    return;
                }
            }
            else {
                Notification.show("Не выбраны даты для сортировки!", 3000, Notification.Position.MIDDLE);
                return;
            }
        });
    }

    private void reportGenerate(List<PowerResources> list) {
        Font fontHeader = new Font(14, "Times New Roman", "/fonts/times.ttf", Font.PDF_ENCODING_CP1251_Cyrillic, true);
        fontHeader.setBold(true);
        Style headerStyle = new StyleBuilder(true)
                .setFont(fontHeader)
                .setBackgroundColor(Color.GRAY)
                .setBorder(Border.PEN_1_POINT())
                .setHorizontalAlign(HorizontalAlign.CENTER)
                .setVerticalAlign(VerticalAlign.MIDDLE)
                .build();
        Font fontColumn = new Font(12, "Times New Roman", "/fonts/times.ttf", Font.PDF_ENCODING_CP1251_Cyrillic, true);
        Style columnStyle = new StyleBuilder(true)
                .setFont(fontColumn)
                .setBorder(Border.PEN_1_POINT())
                .setHorizontalAlign(HorizontalAlign.CENTER)
                .build();
        Style style = new StyleBuilder(true).setFont(Font.ARIAL_MEDIUM).build();

        report = new PrintPreviewReport<>();
        report.getReportBuilder()
                .setMargins(20, 20, 40, 40)
                .setTitle(powerReportModel.getMsg())
                .setTitleStyle(headerStyle)
                //.addAutoText("Для внутреннего использования", AutoText.POSITION_HEADER, AutoText.ALIGMENT_LEFT, 200, columnStyle)
                //.addAutoText(LocalDateTime.now().toString(), AutoText.POSITION_HEADER, AutoText.ALIGNMENT_RIGHT, 200, style)
                .addAutoText(AutoText.AUTOTEXT_PAGE_X_OF_Y, AutoText.POSITION_HEADER, AutoText.ALIGNMENT_RIGHT, 200, 10, style)
                .setPrintBackgroundOnOddRows(true)
                .addColumn(ColumnBuilder.getNew()
                        .setColumnProperty("powerResourceDict.resourceName", String.class)
                        .setTitle("Наименование")
                        .setWidth(95)
                        .setStyle(columnStyle)
                        .setHeaderStyle(headerStyle)
                        .build())
                .addColumn(ColumnBuilder.getNew()
                        .setColumnProperty("value", Double.class)
                        .setTitle("Показания")
                        .setStyle(columnStyle)
                        .setHeaderStyle(headerStyle)
                        .build())
                .addColumn(ColumnBuilder.getNew()
                        .setColumnProperty("valueWeekly", Double.class)
                        .setTitle("Разница(еже-но)")
                        .setStyle(columnStyle)
                        .setHeaderStyle(headerStyle)
                        .build())
                .addColumn(ColumnBuilder.getNew()
                        .setColumnProperty("dateCreate", Date.class)
                        .setTitle("Дата снятия")
                        .setStyle(columnStyle)
                        .setHeaderStyle(headerStyle)
                        .build());

        report.setItems(list);//powerResourcesService.getAll()

        SerializableSupplier<List<? extends PowerResources>> itemsSupplier = () -> list;

        StreamResource streamResource = report.getStreamResource(
                "report.pdf", itemsSupplier, PrintPreviewReport.Format.PDF);
        pdf = new Anchor(streamResource, "Экспорт и печать");
    }
}
