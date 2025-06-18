package com.example.springApp.FSAservice;

import com.example.springApp.model.RegistredMeter;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class MergeFiles {
    public List<RegistredMeter> comFile;
    public List<RegistredMeter> ffFile;
    public boolean hasError;
    public String erMessage



    public void merge () {


        ffFile.forEach(registredMeter -> {
            for (RegistredMeter meter : comFile) {
                if (meter.equals(registredMeter)) {
                    registredMeter.setNumberVerification(meter.getNumberVerification());
                    registredMeter.setResultVerification(meter.isResultVerification());
                    break;
                }
            }
        });

    }

    private void hasError() {
        if (comFile.size() != ffFile.size()) {
            hasError = true;
            erMessage = "Размеры файлов не равны";
        }
    }
}
