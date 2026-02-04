package com.example.springApp.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Equipment {
    private String metrologist;//Полное имя
    private String numberUpsz;
    private String typeNumIntegral;
    private String manufactureNumIntegral;
    private String typeNumIva;
    private String manufactureNumIva;
    private String typeNumTl;
    private String manufactureNumTl;
    private String snils;
    private String abbreviatedName; // Фамилия И.О.
}