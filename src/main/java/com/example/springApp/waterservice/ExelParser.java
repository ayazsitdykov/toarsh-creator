package com.example.springApp.waterservice;

import com.example.springApp.model.IPUModel;
import com.example.springApp.model.Key;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Iterator;

public class ExelParser {

    public HashMap<Key, IPUModel> parse(String file) {
        HashMap<Key, IPUModel> waterMeterList = new HashMap<>();
        InputStream inputStream;
        XSSFWorkbook workBook = null;
        try {
            inputStream = new FileInputStream(file);
            workBook = new XSSFWorkbook(inputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
        Sheet sheet = workBook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            Iterator<Cell> cells = row.iterator();
            IPUModel waterMeter = new IPUModel();

            while (cells.hasNext()) {

                Cell cell = cells.next();
                switch (cell.getColumnIndex()) {
                    case 0 -> waterMeter.setMitypeNumber(cell.getStringCellValue());
                    case 1 -> waterMeter.setManufactureNum(cell.getStringCellValue());
                    case 2 -> waterMeter.setModification(cell.getStringCellValue());
                    case 3 -> waterMeter.setVrfDate(cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    case 4 -> waterMeter.setValidDate(cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    case 5 -> waterMeter.setHot(cell.getStringCellValue().equals("ГВС"));
                    case 6 -> waterMeter.setAddress(cell.getStringCellValue());
                }



            }
            waterMeterList.put(new Key(waterMeter.getManufactureNum(), waterMeter.getVrfDate()), waterMeter);
        }


        return waterMeterList;
    }


}


