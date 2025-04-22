package com.example.springApp.waterservice;

import lombok.Getter;

import java.io.PrintWriter;
import java.util.Random;

@Getter
public class ParametersCreator {

    private String temperature;
    private String pressure;
    private String humidity;
    private String other;
    private final String resource;

    public ParametersCreator(String resource) {
        this.resource = resource;
        this.paramCreate();
    }

    private void paramCreate() {
        double temperature = new Random().nextInt(22, 26) + (double) new Random().nextInt(10) / 10;
        this.temperature = temperature + " °С";

        double pressure = new Random().nextInt(97, 101) + (double) new Random().nextInt(100) / 100;
        this.pressure = pressure + " кПа";

        double humidity = new Random().nextInt(35, 60) + (double) new Random().nextInt(10) / 10;
        this.humidity = humidity + " %";

        if (resource.equals("ГВС")) {
            double other = new Random().nextInt(40, 65) + (double) new Random().nextInt(0, 2) * 0.5;
            this.other = "Температура раб. ср. " + other + " °С";

            ;
        } else if (resource.equals("ХВС")) {
            double other = new Random().nextInt(10, 18) + (double) new Random().nextInt(0, 2) * 0.5;
            this.other = "Температура раб. ср. " + other + " °С";
            System.out.println(this.other);
        }
    }
}
