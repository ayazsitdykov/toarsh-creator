package com.example.springApp.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Setter
@Getter
public class IPU {

    public static final String signCipher = "ДЧЯ";
    private String docTitle = "МИ1592-2015 «Рекомендация.ГСИ.Счетчики воды.Методика поверки»";

    private Equipment equipment;

    private String mitypeNumber;
    private String manufactureNum;
    private String modification;
    private LocalDate vrfDate;
    private LocalDate validDate;

    private boolean signPass;
    private boolean signMi;

    private Params params;

    private boolean isHot;
    private String address;
    private String owner;
    private String actNum;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IPU ipu)) return false;
        return Objects.equals(manufactureNum, ipu.manufactureNum)
                && Objects.equals(equipment, ipu.equipment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(manufactureNum, vrfDate);
    }
}