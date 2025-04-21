package com.example.springApp.waterservice;

import com.example.springApp.model.IPUModel;
import com.example.springApp.model.Key;
import lombok.AllArgsConstructor;

import java.util.HashMap;


public class ErrorChecking {
    HashMap<Key, IPUModel> waterMeterList;

    public ErrorChecking(HashMap<Key, IPUModel> waterMeterList) {
        this.waterMeterList = waterMeterList;
        this.dateChecking();
    }

    public void dateChecking() {
        waterMeterList.entrySet().
                forEach(map ->
                {
                    if ((map.getValue().getVrfDate().getDayOfMonth() - map.getValue().getValidDate().getDayOfMonth()) != 1) {
                        printMessage(map.getValue().getManufactureNum(), "Неверные даты");

                    }

                });
    }


    public void printMessage(String number, String message) {
        System.out.println("Счетчик с номером " + number + " - " + message);
    }
}