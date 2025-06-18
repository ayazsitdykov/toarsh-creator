package com.example.springApp.FSAservice;

import com.example.springApp.FGISservice.EquipmentParser;
import com.example.springApp.model.Equipment;
import com.example.springApp.model.RegistredMeter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class ComplExcelParser {
    List<RegistredMeter> registredMeters = new ArrayList<>();

    public List<RegistredMeter> parser(String filePath) {

        try (InputStream inputStream = new FileInputStream(filePath);
             HSSFWorkbook workBook = new HSSFWorkbook(inputStream)) {
            Sheet sheet = workBook.getSheetAt(0);

            for (Row row : sheet) {

                if (row.getCell(0) == null || !row.getCell(0).getCellType().name().equals("STRING")) {
                    //прерываем цикл, если в первой ячейке строчки не строка
                    break;

                }

                RegistredMeter registredMeter = new RegistredMeter();

                registredMeter.setManufactureNum(getStringCell(row.getCell(1)));
                registredMeter.setTypeMeasuringInstrument(getStringCell(row.getCell(2)));
                registredMeter.setDateVerification(row.getCell(5)
                        .getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                registredMeter.setDateEndVerification(row.getCell(6)
                        .getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                writeFioSnils(registredMeter, getStringCell(row.getCell(12)));


                registredMeters.add(registredMeter);


            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return registredMeters;
    }

    private String getStringCell(Cell cell) {

        if (cell == null) {

            return "";
        }

        return cell.getStringCellValue().trim();
    }

    private void writeFioSnils(RegistredMeter regMeter, String metrologist) { // метод записи ФИО
        List<Equipment> eqL = new EquipmentParser().parse("equipment.json");

        eqL.forEach(equipment -> {
            if (equipment.getMetrologist().equals(metrologist)) {
                String[] separatedName = separateStringBySpace(equipment.getFulName());
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
