package com.vaadin.tutorial.crm.ui.report;

import ar.com.fdvs.dj.domain.AutoText;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.StyleBuilder;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableSupplier;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.tutorial.crm.entity.powerresources.PowerResources;
import com.vaadin.tutorial.crm.service.powerresources.PowerResourcesService;
import com.vaadin.tutorial.crm.ui.layout.ReportLayout;
import org.vaadin.reports.PrintPreviewReport;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

/**
 * Класс отчет по показаниям энергоресурсов
 */
@Route(value = "powerreport", layout = ReportLayout.class)
@PageTitle("Отчет по показаниям энергоресурсов | Система управления производством")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class PowerResourceReport extends VerticalLayout {
    private final PowerResourcesService powerResourcesService;

    public PowerResourceReport(PowerResourcesService powerResourcesService) {
        this.powerResourcesService = powerResourcesService;
        Style headerStyle = new StyleBuilder(true).setFont(Font.ARIAL_MEDIUM).build();
        Style columnStyle = new StyleBuilder(true).setHorizontalAlign(HorizontalAlign.CENTER).build();

        PrintPreviewReport<PowerResources> report = new PrintPreviewReport<>();
        report.getReportBuilder()
                .setMargins(20, 20, 40, 40)
                .setReportLocale(Locale.UK)
                .setDefaultEncoding("UTF8")
                .setTitle("Отчет по воде")
                .addAutoText("Для внутреннего использования", AutoText.POSITION_HEADER, AutoText.ALIGMENT_LEFT, 200, headerStyle)
                .addAutoText(LocalDateTime.now().toString(), AutoText.POSITION_HEADER, AutoText.ALIGNMENT_RIGHT, 200, headerStyle)
                .addAutoText(AutoText.AUTOTEXT_PAGE_X_OF_Y, AutoText.POSITION_HEADER, AutoText.ALIGNMENT_RIGHT, 200, 10, headerStyle)
                .setPrintBackgroundOnOddRows(true)
                .addColumn(ColumnBuilder.getNew()
                        .setColumnProperty("powerResourceDict.resourceName", String.class)
                        .setTitle("Наименование")
                        .setStyle(columnStyle)
                        .build())
                .addColumn(ColumnBuilder.getNew()
                        .setColumnProperty("value", Double.class)
                        .setTitle("Введенные показания")
                        .setStyle(columnStyle)
                        .build())
                .addColumn(ColumnBuilder.getNew()
                        .setColumnProperty("valueWeekly", Double.class)
                        .setTitle("Разница показаний(еженедельно)")
                        .setStyle(columnStyle)
                        .build());

        report.setItems(powerResourcesService.getAll());//powerResourcesService.getAllByResourceId(1L)

        SerializableSupplier<List<? extends PowerResources>> itemsSupplier = () -> powerResourcesService.getAll();

        StreamResource streamResource = report.getStreamResource(
                "report.pdf", itemsSupplier, PrintPreviewReport.Format.valueOf("PDF"));
        Anchor pdf = new Anchor(streamResource, "Download PDF");

        add(report, pdf);
    }
}
