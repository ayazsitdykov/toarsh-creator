package com.example.springApp.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class IPU {

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
                "mitypeNumber='" + mitypeNumber + '\'' +
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



