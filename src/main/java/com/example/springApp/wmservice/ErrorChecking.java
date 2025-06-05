package com.example.springApp.wmservice;

import com.example.springApp.model.IPU;
import com.example.springApp.model.KeyMeter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ErrorChecking {
    StringBuilder stringBuilder = new StringBuilder("\n");
    Map<KeyMeter, IPU> waterMeterList;
    Map<String, Object> regFifList;
    public boolean hasError = false;

    public ErrorChecking(Map<KeyMeter, IPU> waterMeterList, Object regFifList) {

        this.regFifList = (Map<String, Object>) regFifList;
        this.waterMeterList = waterMeterList;

    }


    public StringBuilder errorChecking() {

        if (waterMeterList == null) {
            hasError = true;
        } else {
            @SuppressWarnings("unchecked")
            List<String> stopList = (List<String>) regFifList.get("stop"); //проверка стоп-листа
            waterMeterList.forEach((key, value) -> {
                String manufactureNumber = value.getManufactureNum();
                String regNumber = value.getMitypeNumber();
                ArrayList<Integer> mpi = new ArrayList<>();

                try {
                    if (stopList.contains(regNumber)) {
                        printMessage(manufactureNumber, "не поверяется по МИ1599-15");
                        return;
                    }

                    @SuppressWarnings("unchecked")
                    Map<String, Object> siData = (Map<String, Object>) regFifList.get(regNumber);

                    @SuppressWarnings("unchecked")
                    List<String> typeValues = (List<String>) siData.get("ТИП");

                    if (!typeValues.contains(value.getModification())) {
                        printMessage(manufactureNumber, "Неправильный тип счетчика");
                    }

                    if (siData.get("ГВС") instanceof List) {
                        @SuppressWarnings("unchecked")
                        List<Integer> gvsValues = (List<Integer>) siData.get("ГВС");
                        @SuppressWarnings("unchecked")
                        List<Integer> hvsValues = (List<Integer>) siData.get("ХВС");
                        mpi.addAll(value.isHot() ? gvsValues : hvsValues);

                    } else {
                        @SuppressWarnings("unchecked")
                        Map<String, ?> mpiValues = (Map<String, ?>) regFifList.get(regNumber);

                        if (value.isHot()) {
                            if (!mpiValues.containsKey("ГВС")) {
                                //проверка госреестров счетчиков, которые используются только для ГВС
                                printMessage(manufactureNumber, "Не используется для ГВС");
                                return;
                            } else {
                                mpi.add((Integer) mpiValues.get("ГВС"));

                            }
                        } else {
                            if (!mpiValues.containsKey("ХВС")) {
                                //проверка госреестров счетчиков, которые используются только для ХВС
                                printMessage(manufactureNumber, "Не используется для XВС");
                                return;
                            } else {
                                mpi.add((Integer) mpiValues.get("ХВС"));
                            }
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


                if (value.getVrfDate().equals(LocalDate.EPOCH) ||
                        value.getValidDate().equals(LocalDate.EPOCH)) {
                    printMessage(manufactureNumber, "Проверьте формат даты");
                    return;
                    // проверяем что даты не равны 1 января 1970
                }


                if (!isDateCorrect && !mpi.isEmpty()) {

                    printMessage(manufactureNumber, "Несоответствие дат МПИ");


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