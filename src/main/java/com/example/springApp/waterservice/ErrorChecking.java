package com.example.springApp.waterservice;

import com.example.springApp.model.IPUModel;
import com.example.springApp.model.Key;
import com.example.springApp.model.MpiJsonParser;

import java.time.LocalDate;
import java.util.HashMap;


public class ErrorChecking {
    HashMap<Key, IPUModel> waterMeterList;
    HashMap<String, HashMap<String, Integer>> regFifList;
    public boolean hasError = false;

    public ErrorChecking(HashMap<Key, IPUModel> waterMeterList, HashMap<String, HashMap<String, Integer>> regFifList) {
        this.regFifList = regFifList;
        this.waterMeterList = waterMeterList;
        this.errorChecking();
    }


    public void errorChecking() {
        waterMeterList.forEach((key, value) -> {
            int mpi=0;
            try {
                mpi = regFifList.get(value.getMitypeNumber()).get(value.isHot() ? "ГВС" : "ХВС");
            } catch (Exception ex) {
                printMessage(value.getManufactureNum(), "Рег номер - " + value.getMitypeNumber() +  "- не найден в базе");
            }

            if (!(value.getVrfDate().plusYears(mpi).minusDays(1).equals(value.getValidDate()))) {


                printMessage(value.getManufactureNum(), "Несоответствие дат МПИ");
                hasError = true;

            }

        });
    }


    public void printMessage(String number, String message) {
        System.out.println("Счетчик с номером " + number + " - " + message);
    }
}