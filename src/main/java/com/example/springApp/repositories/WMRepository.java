package com.example.springApp.repositories;

import com.example.springApp.entity.WaterMeter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WMRepository extends JpaRepository<WaterMeter, Long> {
}