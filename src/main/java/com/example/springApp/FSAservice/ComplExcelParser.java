package com.example.springApp.FSAservice;

import com.example.springApp.FGISservice.EquipmentParser;
import com.example.springApp.model.Equipment;
import com.example.springApp.model.RegistredMeter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ComplExcelParser {
    List<RegistredMeter> registredMeters = new ArrayList<>();

    public List<RegistredMeter> parser(String filePath) {

        try (FileInputStream inputStream = new FileInputStream(filePath);
             Workbook workBook = new HSSFWorkbook(inputStream)) {
            Sheet sheet = workBook.getSheetAt(0);

            for (Row row : sheet) {

                if (row.getCell(0) == null || !row.getCell(0).getCellType().name().equals("STRING")) {
                    //прерываем цикл, если в первой ячейке строчки не строка
                    break;

                }

                RegistredMeter registredMeter = new RegistredMeter();

                registredMeter.setManufactureNum(getManufactureNum(row.getCell(1)));
                registredMeter.setTypeMeasuringInstrument(getStringCell(row.getCell(2)));
                registredMeter.setDateVerification(getDate(row.getCell(5)));

                registredMeter.setDateEndVerification(getDate(row.getCell(6)));

                writeFioSnils(registredMeter, getStringCell(row.getCell(12)));


                registredMeters.add(registredMeter);


            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return registredMeters;
    }

    private String getManufactureNum(Cell cell) {

        String str = "";


        if (cell.getCellType().name().equals("STRING")) {
            str = getStringCell(cell);

        }

        if (cell.getCellType().name().equals("NUMERIC")) {
            int numberAsNumber = (int) cell.getNumericCellValue();

            str = String.valueOf(numberAsNumber);
        }
        return str;
    }

    private LocalDate getDate(Cell cell) {
        LocalDate localDate = null;

        if (cell.getCellType().name().equals("STRING")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            localDate = LocalDate.parse(cell.getStringCellValue(), formatter);

        }

        if (cell.getCellType().name().equals("NUMERIC")) {
            localDate = cell.getDateCellValue().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate();

        }

        return localDate;
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
