package com.example.springApp.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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
