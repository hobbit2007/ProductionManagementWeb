package com.vaadin.tutorial.crm.ui.storage;

import ar.com.fdvs.dj.domain.AutoText;
import ar.com.fdvs.dj.domain.ImageBanner;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.StyleBuilder;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.constants.Page;
import ar.com.fdvs.dj.domain.constants.VerticalAlign;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableSupplier;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.tutorial.crm.model.DataLabel;
import com.vaadin.tutorial.crm.service.storage.MaterialInfoService;
import com.vaadin.tutorial.crm.ui.layout.StorageLayout;
import org.vaadin.reports.PrintPreviewReport;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;

/**
 * Класс реализующий печать внутренней этикетки после постановки на приход объекта хранения
 */
@Route(value = "labelreport", layout = StorageLayout.class)
@PageTitle("Этикетка приход | Система управления производством")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class PrihodLabelPrint extends Dialog {
    PrintPreviewReport<DataLabel> report;
    Anchor pdf;
    private final String path = "/opt/uploads/prihod/QRCode_"+System.currentTimeMillis() / 1000L+".png";//D:\Book\QRCode_
    Button close = new Button("Закрыть");
    HorizontalLayout hMain = new HorizontalLayout();
    VerticalLayout vMain = new VerticalLayout();
    public PrihodLabelPrint(List<DataLabel> dataLabels, Long id, MaterialInfoService materialInfoService) {

        if (dataLabels.size() != 0) {

            generateQRCodeImage("Артикул: " + dataLabels.get(0).getArticle(), 75, 75, path);
            Font fontHeader = new Font(15, "Times New Roman", "/fonts/times.ttf", Font.PDF_ENCODING_CP1251_Cyrillic, true);
            fontHeader.setBold(true);
            Font fontColumn = new Font(12, "Times New Roman", "/fonts/times.ttf", Font.PDF_ENCODING_CP1251_Cyrillic, true);
            Style columnStyle = new StyleBuilder(true)
                    .setFont(fontColumn)
                    .setVerticalAlign(VerticalAlign.MIDDLE)
                    .build();
            Style materialStyle = new StyleBuilder(true)
                    .setFont(fontHeader)
                    //.setPaddingLeft(10)
                    .setPaddingTop(-5)
                    .build();
            Page page = new Page();
            page.setWidth(303);
            page.setHeight(379);

            report = new PrintPreviewReport<>();

            report.getReportBuilder()
                    .setMargins(2, 2, 4, 4)
                    .setPageSizeAndOrientation(page)
                    .setPrintBackgroundOnOddRows(true)
                    .addAutoText(dataLabels.get(0).getMaterialInfo(), AutoText.POSITION_HEADER, AutoText.ALIGMENT_CENTER, 300, materialStyle)
                    .addAutoText("Склад: " + dataLabels.get(0).getStorage(), AutoText.POSITION_HEADER, AutoText.ALIGMENT_CENTER, 300, columnStyle)
                    .addAutoText("Ячейка: " + dataLabels.get(0).getCell(), AutoText.POSITION_HEADER, AutoText.ALIGMENT_CENTER, 300, columnStyle)
                    .addAutoText("Производитель: " + dataLabels.get(0).getSupplier(), AutoText.POSITION_HEADER, AutoText.ALIGMENT_CENTER, 300, columnStyle)
                    .addAutoText("Артикул: " + dataLabels.get(0).getArticle(), AutoText.POSITION_HEADER, AutoText.ALIGMENT_CENTER, 300, columnStyle)
                    .addImageBanner(path, 75, 75, ImageBanner.Alignment.Center);

            report.setItems(dataLabels);

            SerializableSupplier<List<? extends DataLabel>> itemsSupplier = () -> dataLabels;

            StreamResource streamResource = report.getStreamResource(
                    "report.pdf", itemsSupplier, PrintPreviewReport.Format.PDF);
            pdf = new Anchor(streamResource, "Экспорт и печать");
            pdf.setTarget("_blank");
            pdf.getStyle().set("color", "#d3b342");
            pdf.getStyle().set("font-size", "20pt");

            Icon icon1 = new Icon(VaadinIcon.CLOSE);
            close.setIcon(icon1);
            close.getStyle().set("background-color", "#d3b342");
            close.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            close.addClickListener(e -> close());

            hMain.add(pdf, close);
            hMain.setVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
            vMain.add(report, hMain);
            vMain.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
            add(vMain);

            try {
                materialInfoService.updateQrField(path, id);
            }
            catch (Exception ex) {
                Notification.show("Не могу записать путь к qr коду" + ex.getMessage(), 5000, Notification.Position.MIDDLE);
                return;
            }
        }
    }
    private void generateQRCodeImage(String text, int width, int height, String filePath) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        Path path = FileSystems.getDefault().getPath(filePath);
        try {
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private byte[] getQRCodeImage(String text, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        try {
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] pngData = pngOutputStream.toByteArray();
        return pngData;
    }
}
