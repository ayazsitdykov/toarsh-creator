package com.example.springApp.waterservice;

import com.example.springApp.model.Equipment;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EquipmentParser {

    public List<Equipment> parse(String filePath) {
        List<Equipment> equipmentList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        File jsonFile = new File(filePath);

        try {
            equipmentList = objectMapper.readValue(jsonFile,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Equipment.class));;

        } catch (Exception e) {
            e.printStackTrace();
        }


        return equipmentList;
    }

}
