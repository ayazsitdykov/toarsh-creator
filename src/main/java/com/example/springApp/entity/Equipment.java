package com.example.springApp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "equipment")
@Getter
@Setter
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "metrologist", nullable = false)
    private String metrologist;

    @Column(name = "number_upsz", nullable = false)
    private String numberUpsz;

    @Column(name = "type_num_integral", nullable = false)
    private String typeNumIntegral;

    @Column(name = "manufacture_num_integral", nullable = false)
    private String manufactureNumIntegral;

    @Column(name = "type_num_iva", nullable = false)
    private String typeNumIva;

    @Column(name = "manufacture_num_iva", nullable = false)
    private String manufactureNumIva;

    @Column(name = "type_num_tl", nullable = false)
    private String typeNumTl;

    @Column(name = "manufacture_num_tl", nullable = false)
    private String manufactureNumTl;

    @Column(name = "snils", nullable = false)
    private String snils;

    @OneToMany(mappedBy = "equipment")
    private List<WaterMeter> waterMeter;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
