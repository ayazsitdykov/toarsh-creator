package com.example.springApp.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class RegistredMeter {
    private int numberVerification;
    private LocalDate dateVerification;
    private LocalDate dateEndVerification;
    private String typeMeasuringInstrument;
    private String last;
    private String first;
    private String middle;
    private String snils;
    private boolean ResultVerification;
}
