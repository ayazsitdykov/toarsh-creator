package com.example.springApp.wmservice;

import com.example.springApp.model.IPU;
import com.example.springApp.model.KeyMeter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ErrorChecking {
    StringBuilder stringBuilder = new StringBuilder("\n");
    Map<KeyMeter, IPU> waterMeterList;
    Map<String, Object> regFifList;
    public boolean hasError = false;
    Logger logger = LoggerFactory.getLogger(ErrorChecking.class);

    public ErrorChecking(Map<KeyMeter, IPU> waterMeterList, Object regFifList) {

        this.regFifList = (Map<String, Object>) regFifList;
        this.waterMeterList = waterMeterList;

    }


    public StringBuilder errorChecking() {

        if (waterMeterList == null) {
            hasError = true;
        } else {
            waterMeterList.forEach((key, value) -> {

                String manufactureNumber = value.getManufactureNum();
                String regNumber = value.getMitypeNumber();

                ArrayList<Integer> mpi = new ArrayList<>();


                try {
                    @SuppressWarnings("unchecked")
                    List<String> stopList = (List<String>) regFifList.get("stop"); //проверка стоп-листа
                    if (stopList.contains(regNumber)) {
                        printMessage(manufactureNumber, "не поверяется по МИ1599-15");
                        mpi.add(-1);

                    }

                    if (regFifList.get(regNumber) instanceof Integer) {
                        int equalMpi = (Integer) regFifList.get(regNumber);
                        mpi.add(equalMpi);
                    } else if (regFifList.get(regNumber) instanceof Map) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> hotArray = (Map<String, Object>) regFifList.get(regNumber);
                        if (hotArray.get("ГВС") instanceof List) {
                            @SuppressWarnings("unchecked")
                            List<Integer> gvsValues = (List<Integer>) hotArray.get("ГВС");
                            @SuppressWarnings("unchecked")
                            List<Integer> hvsValues = (List<Integer>) hotArray.get("ХВС");

                            mpi.addAll(value.isHot() ? gvsValues : hvsValues);


                        } else {
                            @SuppressWarnings("unchecked")
                            Map<String, Integer> mpiValues = (Map<String, Integer>) regFifList.get(regNumber);
                            mpi.add(value.isHot() ? mpiValues.get("ГВС") : mpiValues.get("ХВС"));
                        }


                    }


                } catch (Exception ex) {
                    ex.printStackTrace();


                }

                if (mpi.isEmpty()) {
                    printMessage(value.getManufactureNum(), "Рег номер - " + value.getMitypeNumber() + " - не найден в базе");
                }

                boolean isDateCorrect = false;
                for (int year : mpi) {
                    if (value.getVrfDate().plusYears(year).minusDays(1)
                            .equals(value.getValidDate())) {
                        isDateCorrect = true;
                        break;
                    }
                }


                if (!mpi.contains(-1) && !isDateCorrect && !mpi.isEmpty()) {

                    printMessage(value.getManufactureNum(), "Несоответствие дат МПИ");


                }

            });
            if (!hasError) {
                stringBuilder.append("Файл прочитан - ошибок не обнаружено");
            }
        }
        return stringBuilder;
    }




    public void printMessage(String number, String message) {
        hasError = true;

        stringBuilder.append("Счетчик с номером " + number + " - " + message + "\n");

        System.out.println("Счетчик с номером " + number + " - " + message);
    }
}