package com.example.springApp.FSAservice;

import com.example.springApp.model.RegistredMeter;

import java.util.List;


public class MergeFiles {
    public List<RegistredMeter> comFile;
    public List<RegistredMeter> ffFile;
    public boolean hasError;
    public String erMessage = "";

    public MergeFiles(List<RegistredMeter> comFile, List<RegistredMeter> ffFile) {
        this.comFile = comFile;
        this.ffFile = ffFile;
    }

    public void merge() {

        if (comFile.size() != ffFile.size()) {

            hasError = true;
            erMessage += "Размеры файлов не равны";
        }
        comFile.forEach(registredMeter -> {
            for (RegistredMeter meter : ffFile) {
                if (meter.equals(registredMeter)) {


                    registredMeter.setNumberVerification(meter.getNumberVerification());
                    registredMeter.setResultVerification(meter.getResultVerification());
                    break;
                }
                if (meter.equals(ffFile.get(ffFile.size() - 1))) {
                    //если не нашли счетчик в comFile
                    printMessage(registredMeter.getManufactureNum(), "Не найден в файле из Аршина");
                }

            }
        });

        ffFile.forEach(registredMeter -> {
            for (RegistredMeter meter : comFile) {
                if (meter.equals(registredMeter)) {

                    break;
                }

                if (meter.equals(ffFile.get(ffFile.size() - 1))) {
                    //если не нашли счетчик в comFile
                    printMessage(registredMeter.getManufactureNum(), "Не найден в файле за месяц");
                }

            }
        });
       if (!hasError) {
           erMessage = "Сформирован общий файл";
       }

    }




    private void printMessage(String number, String message) {
        hasError = true;

        erMessage += "Счетчик с номером " + number + " - " + message + "\n";

    }
}
