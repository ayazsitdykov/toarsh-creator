package com.example.springApp.service.FSA;

import com.example.springApp.model.Equipment;
import com.example.springApp.model.RegistredMeter;
import com.example.springApp.service.ExcelExtractor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class CompletedFileExtractor extends ExcelExtractor {

    private final List<RegistredMeter> registeredMeters = new ArrayList<>();
    private final List<Equipment> equipmentList;

    public List<RegistredMeter> transfer(Sheet sheet) {

        for (Row row : sheet) {

            if (row.getCell(0) == null || !row.getCell(0).getCellType().name().equals("STRING")) {
                //прерываем цикл, если в первой ячейке строчки не строка
                break;

            }

            RegistredMeter registredMeter = new RegistredMeter();

            registredMeter.setManufactureNum(getStringCell(row.getCell(1)));
            registredMeter.setTypeMeasuringInstrument(getStringCell(row.getCell(2)));
            registredMeter.setDateVerification(getDateCell(row.getCell(5)));
            registredMeter.setDateEndVerification(getDateCell(row.getCell(6)));
            writeFioSnils(registredMeter, getStringCell(row.getCell(12)));

            registeredMeters.add(registredMeter);
        }

        return registeredMeters;
    }

    private void writeFioSnils(RegistredMeter regMeter, String metrologist) {

        equipmentList.forEach(equipment -> {
            if (equipment.getAbbreviatedName().equals(metrologist)){
                String[] separatedName = separateStringBySpace(equipment.getMetrologist());
                regMeter.setLast(separatedName[0]);
                regMeter.setFirst(separatedName[1]);
                regMeter.setMiddle(separatedName[2]);
                regMeter.setSnils(equipment.getSnils());
            }
        });
    }

    private String[] separateStringBySpace(String str) { // деление строки по пробелам
        return str.split(" ");
    }
}