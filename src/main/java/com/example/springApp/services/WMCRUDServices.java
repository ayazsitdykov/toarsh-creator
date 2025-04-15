package com.example.springApp.services;

import com.example.springApp.dto.WaterMeterDto;
import com.example.springApp.entity.WaterMeter;
import com.example.springApp.repositories.WMRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class WMCRUDServices implements CRUDServices<WaterMeterDto> {

    private final WMRepository wmRepository;

    @Override
    public WaterMeterDto getById(long id) {
        if (!wmRepository.existsById(id)) {
            return null;
        }
        WaterMeter waterMeter = wmRepository.findById(id).orElseThrow();
        return mapToDto(waterMeter);
    }

    @Override
    public Collection<WaterMeterDto> getAll() {
        return wmRepository.findAll().stream().map(WMCRUDServices::mapToDto).toList();
    }

    public boolean waterMeterExist(WaterMeterDto waterMeterDto) {
        Collection<WaterMeterDto> waterMeterDtoList = getAll();
        for (WaterMeterDto wmDto : waterMeterDtoList) {
            if (waterMeterDto.equals(wmDto)) {
                return true;
            }
        }
        return false;

    }

    @Override
    public void create(WaterMeterDto item) {
        wmRepository.save(mapToEntity(item));

    }

    @Override
    public void delete(long id) {
        wmRepository.deleteById(id);

    }

    @Override
    public void update(WaterMeterDto item) {
        wmRepository.save(mapToEntity(item));

    }

    public static WaterMeterDto mapToDto(WaterMeter waterMeter) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        WaterMeterDto waterMeterDto = new WaterMeterDto();
        waterMeterDto.setMitypeNumber(waterMeter.getMitypeNumber());
        waterMeterDto.setManufactureNum(waterMeter.getManufactureNum());
        waterMeterDto.setModification(waterMeter.getModification());
        waterMeterDto.setMetrologist(waterMeter.getMetrologist());
        waterMeterDto.setManufacturedYear(waterMeter.getManufacturedYear());
        waterMeterDto.setVrfDate((waterMeter.getVrfDate().format(formatter)));
        waterMeterDto.setValidDate(waterMeter.getValidDate().format(formatter));
        return waterMeterDto;


    }

    public static WaterMeter mapToEntity(WaterMeterDto waterMeterDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        WaterMeter waterMeter = new WaterMeter();
        waterMeter.setMitypeNumber(waterMeterDto.getMitypeNumber());
        waterMeter.setMetrologist(waterMeterDto.getMetrologist());
        waterMeter.setModification(waterMeterDto.getModification());
        waterMeter.setManufacturedYear(waterMeterDto.getManufacturedYear());
        waterMeter.setManufactureNum(waterMeterDto.getManufactureNum());
        waterMeter.setVrfDate(LocalDate.parse(waterMeterDto.getVrfDate(), formatter));
        waterMeter.setValidDate(LocalDate.parse(waterMeterDto.getValidDate(), formatter));
        return waterMeter;


    }
}
