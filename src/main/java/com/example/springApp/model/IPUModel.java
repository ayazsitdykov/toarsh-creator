package com.example.springApp.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class IPUModel {

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



    @Override
    public String toString() {
        return " mitypeNumber='" + mitypeNumber + '\'' +
                ", manufactureNum='" + manufactureNum + '\'' +
                ", modification='" + modification + '\'' +
                ", vrfDate=" + vrfDate +
                ", validDate=" + validDate +
                ", metrologist='" + metrologist + '\'' +
                ", isHot=" + isHot +
                ", address='" + address ;
    }
}

