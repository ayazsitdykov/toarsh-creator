package com.example.springApp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Setter
@Getter
@NoArgsConstructor
public class WaterMeterDto {
    private String mitypeNumber;
    private String manufactureNum;
    private String modification;
    private int manufacturedYear;
    private String vrfDate;
    private String validDate;
    private String metrologist;

    @Override
    public boolean equals(Object obj){
        WaterMeterDto waterMeterDto = (WaterMeterDto) obj;
        return
                (this.manufactureNum.equals(waterMeterDto.getManufactureNum())
                        && this.vrfDate.equals(waterMeterDto.getVrfDate()));

    }

}
