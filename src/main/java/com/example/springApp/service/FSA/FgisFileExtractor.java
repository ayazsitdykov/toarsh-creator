package com.example.springApp.service.FSA;

import com.example.springApp.model.RegistredMeter;
import com.example.springApp.service.ExcelExtractor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public class FgisFileExtractor extends ExcelExtractor {

    public List<RegistredMeter> parser(Sheet sheet) {

        List<RegistredMeter> registeredMeters = new ArrayList<>();

        Iterator<Row> rowIterator = sheet.iterator();
        Pattern pattern = Pattern.compile("[А-яа-яЁё]");

        while (rowIterator.hasNext()) {
            RegistredMeter registredMeter = new RegistredMeter();
            Row row = rowIterator.next();

            if (pattern.matcher(getCellValueAsString(row.getCell(0))).find()) {
                continue;
            }

            registredMeter.setDateVerification(getDateCell(row.getCell(6)));
            registredMeter.setManufactureNum(getCellValueAsString(row.getCell(5)));
            registredMeter.setResultVerification("Да".equalsIgnoreCase(getCellValueAsString(row.getCell(9))) ? 1 : 0);
            registredMeter.setNumberVerification(getCellValueAsString(row.getCell(8)).split("/")[2]);
            registeredMeters.add(registredMeter);
        }

        return registeredMeters;
    }
}