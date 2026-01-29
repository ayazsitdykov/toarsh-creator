package com.example.springApp.service.FGIS;

import com.example.springApp.model.IPU;
import com.example.springApp.model.Params;
import lombok.Getter;

import java.util.List;
import java.util.Random;

@Getter
public class ParamCreator {

    private static final int MIN_TEMP = 22;
    private static final int MAX_TEMP = 26;

    private static final int MIN_PRESSURE = 97;
    private static final int MAX_PRESSURE = 101;

    private static final int MIN_HUMIDITY = 32;
    private static final int MAX_HUMIDITY = 60;

    private static final int MIN_TEMP_HOT_WATER = 40;
    private static final int MAX_TEMP_HOT_WATER = 65;

    private static final int MIN_TEMP_COLD_WATER = 10;
    private static final int MAX_TEMP_COLD_WATER = 18;

    public void create(List<IPU> waterMeterList) {

        for (IPU meter : waterMeterList) {

            double temperature = randomValue(MIN_TEMP, MAX_TEMP, 1);
            double pressure = randomValue(MIN_PRESSURE, MAX_PRESSURE, 2);
            double humidity = randomValue(MIN_HUMIDITY, MAX_HUMIDITY, 1);
            double waterTemp = randomWaterTemp(meter.isHot());

            Params params = new Params(getString(temperature) + " °С",
                    getString(pressure) + " кПа",
                    getString(humidity) + " %",
                    "Температура раб. ср. " + getString(waterTemp) + " °С");

            meter.setParams(params);
        }
    }

    private static double randomValue(int min, int max, int rank) {
        Random random = new Random();

        if (rank == 0) {
            return random.nextInt(min, max);
        }

        int integerPart = random.nextInt(min, max);
        int scale = (int) Math.pow(10, rank);
        int fractionalPart = random.nextInt(scale);

        String format = "%d." + "%0" + rank + "d";
        String resultStr = String.format(format, integerPart, fractionalPart);

        return Double.parseDouble(resultStr);
    }

    private double randomWaterTemp(boolean hot) {
        double halfStep = randomValue(0, 2, 0) * 0.5;
        if (hot) {
            return randomValue(MIN_TEMP_HOT_WATER, MAX_TEMP_HOT_WATER, 0) + halfStep;
        } else {
            return randomValue(MIN_TEMP_COLD_WATER, MAX_TEMP_COLD_WATER, 0) + halfStep;
        }
    }

    private String getString(double value) {
        return String.valueOf(value).replace('.', ',');
    }
}