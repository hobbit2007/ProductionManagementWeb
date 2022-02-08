package com.vaadin.tutorial.crm.model;

import lombok.Getter;
import lombok.Setter;
import net.sf.jasperreports.components.barcode4j.QRCodeComponent;

/**
 * Модель данных для этикетки на приход
 */
@Getter
@Setter
public class DataLabel {
    private String storage;
    private String cell;
    private String supplier;
    private String article;
    private String materialInfo;
    private Double qty;
    private QRCodeComponent qrCode;
}
