package com.example.springApp.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Setter
@Getter
public class RegistredMeter {

    private String manufactureNum;
    private String numberVerification;
    private LocalDate dateVerification;
    private LocalDate dateEndVerification;
    private String typeMeasuringInstrument;
    private String last;
    private String first;
    private String middle;
    private String snils;
    private boolean resultVerification;




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegistredMeter)) return false;
        RegistredMeter meter = (RegistredMeter) o;
        return Objects.equals(manufactureNum, meter.manufactureNum)
                && Objects.equals(dateVerification, meter.dateVerification);
    }

    @Override
    public String toString() {
        return "RegistredMeter{" +
                "manufactureNum='" + manufactureNum + '\'' +
                ", numberVerification='" + numberVerification + '\'' +
                ", dateVerification=" + dateVerification +
                ", dateEndVerification=" + dateEndVerification +
                ", typeMeasuringInstrument='" + typeMeasuringInstrument + '\'' +
                ", last='" + last + '\'' +
                ", first='" + first + '\'' +
                ", middle='" + middle + '\'' +
                ", snils='" + snils + '\'' +
                ", resultVerification=" + resultVerification +
                '}';
    }
}




