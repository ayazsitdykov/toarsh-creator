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

    @Override
    public String toString() {
        return "metrologist='" + metrologist + '\'' +
                ", numberUpsz='" + numberUpsz + '\'' +
                ", typeNumIntegral='" + typeNumIntegral + '\'' +
                ", manufactureNumIntegral='" + manufactureNumIntegral + '\'' +
                ", typeNumIva='" + typeNumIva + '\'' +
                ", manufactureNumIva='" + manufactureNumIva + '\'' +
                ", typeNumTl='" + typeNumTl + '\'' +
                ", manufactureNumTl='" + manufactureNumTl + '\'';
    }
}