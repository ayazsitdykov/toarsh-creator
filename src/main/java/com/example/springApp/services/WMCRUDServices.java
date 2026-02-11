package com.example.springApp.services;

import com.example.springApp.dto.WaterMeterDto;
import com.example.springApp.repositories.WMRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class WMCRUDServices implements CRUDServices<WaterMeterDto> {

    private final WMRepository wmRepository;

    public WaterMeterDto getById(long id) {

        return null;
    }

    public Collection<WaterMeterDto> getAll() {
        return null;
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

    public void create(WaterMeterDto item) {


    }


    public void delete(long id) {
        wmRepository.deleteById(id);

    }


    public void update(WaterMeterDto item) {

    }
}
