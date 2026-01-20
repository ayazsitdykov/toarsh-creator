package com.example.springApp.FSAservice;

import com.example.springApp.model.RegistredMeter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public class FromFgisParser {

    public List<RegistredMeter> parser(String filePath) {

        List<RegistredMeter> registredMeters = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new HSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0); // Берём первый лист
            Iterator<Row> rowIterator = sheet.iterator();
            Pattern pattern = Pattern.compile("[А-яа-яЁё]");

            // Парсим данные
            while (rowIterator.hasNext()) {
                RegistredMeter registredMeter = new RegistredMeter();
                Row row = rowIterator.next();

                //Пропускаем заголовки (первые 3 строки), если не дата
//                    if (pattern.matcher(getCellValue(row.getCell(0))).find()) {
//                        continue;
//                    }

                // Парсим каждую ячейку
                registredMeter.setDateVerification(parseDate(getCellValue(row.getCell(6))));
                registredMeter.setManufactureNum(getCellValue(row.getCell(5)));
                registredMeter.setResultVerification("Да".equalsIgnoreCase(getCellValue(row.getCell(9))) ? 1 : 0);
                registredMeter.setNumberVerification(splitBySlash(getCellValue(row.getCell(8))));
                registredMeters.add(registredMeter);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return registredMeters;
    }

    private String splitBySlash(String str) {
        String[] separated = str.split("/");
        return separated[2];
    }

    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                } else {
                    return String.valueOf((int) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            return LocalDate.parse(dateStr, formatter);
        } catch (Exception e) {
            return null;
        }
    }


}
