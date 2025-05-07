package com.example.springApp.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class IPU {

    private final String signCipher = "ДЧЯ";
    private final String docTitle = "МИ1592-2015 «Рекомендация.ГСИ.Счетчики воды.Методика поверки»";

    private String mitypeNumber;
    private String manufactureNum;
    private String modification;
    private LocalDate vrfDate;
    private LocalDate validDate;
    private String metrologist;

    private String temperature;
    private String pressure;
    private String humidity;
    private String other;

    private boolean isHot;
    private String address;
    private String owner;
    private String actNum;


    private String numberUpsz;
    private String typeNumIntegral;
    private String manufactureNumIntegral;
    private String typeNumIva;
    private String manufactureNumIva;
    private String typeNumTl;
    private String manufactureNumTl;


    @Override
    public String toString() {
        return "IPU{" +
                "signCipher='" + signCipher + '\'' +
                ", docTitle='" + docTitle + '\'' +
                ", mitypeNumber='" + mitypeNumber + '\'' +
                ", manufactureNum='" + manufactureNum + '\'' +
                ", modification='" + modification + '\'' +
                ", vrfDate=" + vrfDate +
                ", validDate=" + validDate +
                ", metrologist='" + metrologist + '\'' +
                ", temperature='" + temperature + '\'' +
                ", pressure='" + pressure + '\'' +
                ", humidity='" + humidity + '\'' +
                ", other='" + other + '\'' +
                ", isHot=" + isHot +
                ", address='" + address + '\'' +
                ", owner='" + owner + '\'' +
                ", numberUpsz='" + numberUpsz + '\'' +
                ", typeNumIntegral='" + typeNumIntegral + '\'' +
                ", manufactureNumIntegral='" + manufactureNumIntegral + '\'' +
                ", typeNumIva='" + typeNumIva + '\'' +
                ", manufactureNumIva='" + manufactureNumIva + '\'' +
                ", typeNumTl='" + typeNumTl + '\'' +
                ", manufactureNumTl='" + manufactureNumTl + '\'' +
                '}';
    }
}



