package com.example.springApp.service.FSA;

import com.example.springApp.model.RegistredMeter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class MergeFiles {

    private StringBuilder erMessage;
    private boolean hasError;

    public void merge(List<RegistredMeter> comFile, List<RegistredMeter> ffFile) {
        erMessage = new StringBuilder("\n");
        hasError = false;

        if (comFile.size() != ffFile.size()) {
            hasError = true;
            erMessage.append("Размеры файлов не равны \n")
                    .append("Файл из Аршина - ").append(ffFile.size()).append("\n")
                    .append("Файл за месяц - ").append(comFile.size()).append("\n");
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

        if (hasError) {
            log.error(erMessage.toString());
            throw new RuntimeException(erMessage.toString());
        }
        log.info("Сформирован общий файл");
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