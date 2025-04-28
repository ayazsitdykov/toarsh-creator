package com.example.springApp.waterservice;

import com.example.springApp.model.IPU;
import com.example.springApp.model.Key;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Iterator;

public class ExelParser {

    public HashMap<Key, IPU> parse(String filePath) {
        HashMap<Key, IPU> waterMeterList = new HashMap<>();

        try (InputStream inputStream = new FileInputStream(filePath);
             XSSFWorkbook workBook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workBook.getSheetAt(0);

            for (Row row : sheet) {

                if (row.getRowNum() == 0) {
                    if (isFileCorrect(row)) {
                        continue;}
                    System.out.println("Выбран некорректный файл excel");
                    break;

                }

                Iterator<Cell> cells = row.iterator();
                IPU waterMeter = new IPU();


                while (cells.hasNext()) {

                    Cell cell = cells.next();
                    switch (cell.getColumnIndex()) {
                        case 0 -> waterMeter.setMitypeNumber(getStringCell(cell));
                        case 1 -> waterMeter.setManufactureNum(cell.getStringCellValue());
                        case 2 -> waterMeter.setModification(cell.getStringCellValue());
                        case 3 -> waterMeter.setVrfDate(getDateCell(cell));
                        case 4 -> waterMeter.setValidDate(getDateCell(cell));
                        case 5 -> waterMeter.setHot(cell.getStringCellValue().equals("ГВС"));
                        case 6 -> waterMeter.setAddress(cell.getStringCellValue());
                    }


                }
                waterMeterList.put(new Key(waterMeter.getManufactureNum(), waterMeter.getVrfDate()), waterMeter);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return waterMeterList;

    }


    private LocalDate getDateCell(Cell cell) {
        return cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private String getStringCell(Cell cell) {
        return cell.getStringCellValue().trim();
    }

    private boolean isFileCorrect(Row row) {
        String firstColumnName = row.getCell(0).getStringCellValue();
        String secondColumnName = row.getCell(5).getStringCellValue();

        String firstColumnNameOrigin = "Номер \n" +
                "в госреестре";
        String secondColumnNameOrigin = "ХВС/ГВС";


        return firstColumnNameOrigin.equals(firstColumnName) & secondColumnNameOrigin.equals(secondColumnName);
    }

    private String getAdres(String adres) {
        //toDo Проверка адреса соответствию формата, возврат правильного адреса
        return "";
    }


}


