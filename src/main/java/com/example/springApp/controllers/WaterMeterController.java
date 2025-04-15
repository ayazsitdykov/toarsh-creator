package com.example.springApp.controllers;

import com.example.springApp.dto.WaterMeterDto;
import com.example.springApp.services.WMCRUDServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/watermetters")
public class WaterMeterController {

    private final WMCRUDServices wmcrudServices;

    public WaterMeterController(WMCRUDServices wmcrudServices) {
        this.wmcrudServices = wmcrudServices;
    }

    @GetMapping("/{id}")
    public ResponseEntity getMetterById(@PathVariable Long id) {
        WaterMeterDto waterMeterDto = wmcrudServices.getById(id);
        return new ResponseEntity(waterMeterDto, HttpStatus.OK);

    }

    @GetMapping
    public Collection<WaterMeterDto> getAllMetters() {
        return wmcrudServices.getAll();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteMetter(@PathVariable Long id) {
        wmcrudServices.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PostMapping
    public ResponseEntity createMetter(@RequestBody WaterMeterDto waterMeterDto) {
        if (wmcrudServices.waterMeterExist(waterMeterDto)) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }

        wmcrudServices.create(waterMeterDto);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateMetter(@PathVariable Long id, @RequestBody WaterMeterDto waterMeterDto) {
        wmcrudServices.update(waterMeterDto);
        return new ResponseEntity<>(wmcrudServices.getById(id), HttpStatus.ACCEPTED);
    }
}
