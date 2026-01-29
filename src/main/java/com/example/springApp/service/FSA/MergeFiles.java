package com.example.springApp.service.FSA;

import com.example.springApp.model.RegistredMeter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class MergeFiles {
    private final List<RegistredMeter> comFile;
    private final List<RegistredMeter> ffFile;

    private final StringBuilder erMessage = new StringBuilder("\n");
    private boolean hasError;

    public String merge() {

        if (comFile.size() != ffFile.size()) {
            hasError = true;
            erMessage.append("Размеры файлов не равны \n");
        }

        comFile.forEach(registredMeter -> {
            for (RegistredMeter meter : ffFile) {
                if (meter.equals(registredMeter)) {

                    registredMeter.setNumberVerification(meter.getNumberVerification());
                    registredMeter.setResultVerification(meter.getResultVerification());
                    break;
                }

                if (meter.equals(ffFile.get(ffFile.size() - 1))) {
                    printErrorMessage(registredMeter.getManufactureNum(),
                            "Не найден в файле из Аршина");
                }
            }
        });

        ffFile.forEach(registredMeter -> {
            for (RegistredMeter meter : comFile) {
                if (meter.equals(registredMeter)) {
                    break;
                }

                if (meter.equals(comFile.get(comFile.size() - 1))) {
                    printErrorMessage(registredMeter.getManufactureNum(),
                            "Не найден в файле за месяц");
                }
            }
        });
        
       if (!hasError) {
           erMessage.append("Сформирован общий файл");
       }

       return erMessage.toString();
    }

    private void printErrorMessage(String number, String message) {
        hasError = true;
        erMessage.append("Счетчик с номером ")
                .append(number)
                .append(" - ")
                .append(message)
                .append("\n");
    }
}