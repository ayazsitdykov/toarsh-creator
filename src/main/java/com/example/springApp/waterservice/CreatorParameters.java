package com.example.springApp.waterservice;

import com.example.springApp.model.IPU;
import com.example.springApp.model.Key;
import lombok.Getter;

import java.util.HashMap;
import java.util.Random;

@Getter
public class CreatorParameters {
    HashMap<Key, IPU> waterMeterList;

    public CreatorParameters(HashMap<Key, IPU> waterMeterList) {
        this.waterMeterList = waterMeterList;
        this.paramCreate();

    }

    private void paramCreate() {
        for (IPU ipuModel : waterMeterList.values()) {


            double temperature = new Random().nextInt(22, 26) + (double) new Random().nextInt(10) / 10;
            ipuModel.setTemperature(temperature + " °С");

            double pressure = new Random().nextInt(97, 101) + (double) new Random().nextInt(100) / 100;
            ipuModel.setPressure(pressure + " кПа");

            double humidity = new Random().nextInt(35, 60) + (double) new Random().nextInt(10) / 10;
            ipuModel.setHumidity(humidity + " %");


            double other;
            double halfStep = (double) new Random().nextInt(0, 2) * 0.5;
            if (ipuModel.isHot()) {
                other = new Random().nextInt(40, 65) + halfStep;
            } else {
                other = new Random().nextInt(10, 18) + halfStep;
            }
            ipuModel.setOther("Температура раб. ср. " + other + " °С");
            ipuModel.setMetrologist("Ситдыков Р. Н.");
        }
        System.out.println(waterMeterList.entrySet());
    }




}
