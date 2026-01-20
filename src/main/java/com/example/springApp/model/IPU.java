package com.example.springApp.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class IPU {

    private final String signCipher = "ДЧЯ";
    private String docTitle = "МИ1592-2015 «Рекомендация.ГСИ.Счетчики воды.Методика поверки»";

    private Equipment equipment;

    private String mitypeNumber;
    private String manufactureNum;
    private String modification;
    private LocalDate vrfDate;
    private LocalDate validDate;

    private Params params;

    private boolean isHot;
    private String address;
    private String owner;
    private String actNum;

}



