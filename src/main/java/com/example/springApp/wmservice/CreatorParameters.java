package com.example.springApp.wmservice;

import com.example.springApp.model.IPU;
import com.example.springApp.model.Key;
import lombok.Getter;

import java.util.Map;
import java.util.Random;

@Getter
public class CreatorParameters {


    public void paramCreate(Map<Key, IPU> waterMeterList) {
        for (IPU ipuModel : waterMeterList.values()) {


            double temperature = new Random().nextInt(22, 26) + (double) new Random().nextInt(10) / 10;
            ipuModel.setTemperature(getString(temperature) + " °С");

            double pressure = new Random().nextInt(97, 101) + (double) new Random().nextInt(100) / 100;
            ipuModel.setPressure(getString(pressure) + " кПа");

            double humidity = new Random().nextInt(32, 60) + (double) new Random().nextInt(10) / 10;
            ipuModel.setHumidity(getString(humidity) + " %");


            double other;
            double halfStep = (double) new Random().nextInt(0, 2) * 0.5;
            if (ipuModel.isHot()) {
                other = new Random().nextInt(40, 65) + halfStep;
            } else {
                other = new Random().nextInt(10, 18) + halfStep;
            }
            ipuModel.setOther("Температура раб. ср. " + getString(other) + " °С");

        }



    }

    private String getString (double value) {
        String param = String.valueOf(value).replace('.', ',');
        return param;
    }



}
