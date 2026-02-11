package com.example.springApp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "water_meter")
@Getter
@Setter
public class WaterMeter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "mitype_number", nullable = false)
    private String mitypeNumber;

    @Column(name = "manufacture_num, nullable = false")
    private String manufactureNum;

    @Column(name = "modification", nullable = false)
    private String modification;

    @Column(name = "manufactured_year")
    private int manufacturedYear;

    @Column(name = "vrf_date", nullable = false)
    private LocalDate vrfDate;

    @Column(name = "valid_date", nullable = false)
    private LocalDate validDate;

    @Column(name = "temperature", nullable = false)
    private String temperature;

    @Column(name = "pressure", nullable = false)
    private String pressure;

    @Column(name = "humidity", nullable = false)
    private String humidity;

    @Column(name = "other", nullable = false)
    private String other;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "act_number", nullable = false)
    private String actNumber;

    @ManyToOne
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}