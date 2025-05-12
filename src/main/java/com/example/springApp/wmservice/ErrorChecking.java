package com.example.springApp.wmservice;

import com.example.springApp.model.IPU;
import com.example.springApp.model.KeyMeter;

import java.util.HashMap;
import java.util.Map;


public class ErrorChecking {
    HashMap<KeyMeter, IPU> waterMeterList;
    HashMap<String, HashMap<String, Integer>> regFifList;
    public boolean hasError = false;

    public ErrorChecking(Map<KeyMeter, IPU> waterMeterList, Object regFifList) {
        this.regFifList = (HashMap<String, HashMap<String, Integer>>) regFifList;
        this.waterMeterList = (HashMap<KeyMeter, IPU>) waterMeterList;
        this.errorChecking();
    }


    public void errorChecking() {
        if (waterMeterList == null) {
            hasError = true;
        } else {
            waterMeterList.forEach((key, value) -> {
                int mpi = 0;
                try {
                    mpi = regFifList.get(value.getMitypeNumber()).get(value.isHot() ? "ГВС" : "ХВС");
                } catch (Exception ex) {
                    printMessage(value.getManufactureNum(), "Рег номер - " + value.getMitypeNumber() + "- не найден в базе");
                }

                if (!(value.getVrfDate().plusYears(mpi).minusDays(1).equals(value.getValidDate()))) {


                    printMessage(value.getManufactureNum(), "Несоответствие дат МПИ");
                    hasError = true;

                }

            });
        }
    }


    public void printMessage(String number, String message) {
        System.out.println("Счетчик с номером " + number + " - " + message);
    }
}