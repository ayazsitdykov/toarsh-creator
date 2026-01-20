package com.example.springApp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "water_meter")
@Getter
@Setter
public class WaterMeter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String mitypeNumber;

    private String manufactureNum;

    private String modification;

    private int manufacturedYear;

    private LocalDate vrfDate;

    private LocalDate validDate;

    private String metrologist;


}
