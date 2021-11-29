package com.vaadin.tutorial.crm.entity.plccontrollersentity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;

/**
 * Класс модель описывающий таблицу plcwashing101
 */
//@Entity(name = "plcwashing101")
@Getter
@Setter
public class OLDPlcWashing implements Externalizable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Date dateCreate;
    private Long idUserCreate;
    private Long delete;
    private Long idOrderNum;
    private float FT1_005;
    private float FT1_008;
    private float FT1_009_1;
    private float FT1_009_2;
    private float FT1_101;
    private float FT1_102;
    private float FT1_103;
    private float FT1_106;
    private float FT1_107_1;
    private float FT1_107_2;
    private float WT1_011;
    private float LT1_205;
    private float LT1_206;
    private float LT1_204;
    private float LT1_203;
    private float LT1_208;
    private float LT1_202;
    private float PT1_108;
    private float PT1_010;
    private float TOT_FT1_005;
    private float TOT_FT1_008;
    private float TOT_FT1_009_1;
    private float TOT_FT1_009_2;
    private float TOT_FT1_101;
    private float TOT_FT1_102;
    private float TOT_FT1_103;
    private float TOT_FT1_106;
    private float TOT_FT1_107_1;
    private float TOT_FT1_107_2;
    private float TOT_WT1_011;

    public OLDPlcWashing(float FT1_005, float FT1_008, float FT1_009_1, float FT1_009_2, float FT1_101, float FT1_102, float FT1_103, float FT1_106,
                         float FT1_107_1, float FT1_107_2, float WT1_011, float LT1_205, float LT1_206, float LT1_204, float LT1_203 , float LT1_208,
                         float LT1_202, float PT1_108, float PT1_010, float TOT_FT1_005, float TOT_FT1_008, float TOT_FT1_009_1, float TOT_FT1_009_2,
                         float TOT_FT1_101, float TOT_FT1_102, float TOT_FT1_103, float TOT_FT1_106, float TOT_FT1_107_1, float TOT_FT1_107_2,
                         float TOT_WT1_011) {
        this.FT1_005 = FT1_005;
        this.FT1_008 = FT1_008;
        this.FT1_009_1 = FT1_009_1;
        this.FT1_009_2 = FT1_009_2;
        this.FT1_101 = FT1_101;
        this.FT1_102 = FT1_102;
        this.FT1_103 = FT1_103;
        this.FT1_106 = FT1_106;
        this.FT1_107_1 = FT1_107_1;
        this.FT1_107_2 = FT1_107_2;
        this.WT1_011 = WT1_011;
        this.LT1_205 = LT1_205;
        this.LT1_206 = LT1_206;
        this.LT1_204 = LT1_204;
        this.LT1_203 = LT1_203;
        this.LT1_208 = LT1_208;
        this.LT1_202 = LT1_202;
        this.PT1_108 = PT1_108;
        this.PT1_010 = PT1_010;
        this.TOT_FT1_005 = TOT_FT1_005;
        this.TOT_FT1_008 = TOT_FT1_008;
        this.TOT_FT1_009_1 = TOT_FT1_009_1;
        this.TOT_FT1_009_2 = TOT_FT1_009_2;
        this.TOT_FT1_101 = TOT_FT1_101;
        this.TOT_FT1_102 = TOT_FT1_102;
        this.TOT_FT1_103 = TOT_FT1_103;
        this.TOT_FT1_106 = TOT_FT1_106;
        this.TOT_FT1_107_1 = TOT_FT1_107_1;
        this.TOT_FT1_107_2 = TOT_FT1_107_2;
        this.TOT_WT1_011 = TOT_WT1_011;
    }

    public OLDPlcWashing() {

    }

    @Override
    public void writeExternal(ObjectOutput objectOutput) throws IOException {

    }

    @Override
    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {

    }
}
