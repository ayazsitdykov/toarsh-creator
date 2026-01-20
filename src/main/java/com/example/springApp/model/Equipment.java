package com.example.springApp.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Equipment {
    private String metrologist;
    private String numberUpsz;
    private String typeNumIntegral;
    private String manufactureNumIntegral;
    private String typeNumIva;
    private String manufactureNumIva;
    private String typeNumTl;
    private String manufactureNumTl;
    private String fulName;
    private String snils;


    @Override
    public String toString() {
        return "Equipment{" +
                "metrologist='" + metrologist + '\'' +
                ", numberUpsz='" + numberUpsz + '\'' +
                ", typeNumIntegral='" + typeNumIntegral + '\'' +
                ", manufactureNumIntegral='" + manufactureNumIntegral + '\'' +
                ", typeNumIva='" + typeNumIva + '\'' +
                ", manufactureNumIva='" + manufactureNumIva + '\'' +
                ", typeNumTl='" + typeNumTl + '\'' +
                ", manufactureNumTl='" + manufactureNumTl + '\'' +
                ", fulName='" + fulName + '\'' +
                ", snils='" + snils + '\'' +
                '}';
    }
}