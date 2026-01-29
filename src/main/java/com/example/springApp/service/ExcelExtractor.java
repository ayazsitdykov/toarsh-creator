package com.example.springApp.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public abstract class ExcelExtractor {

    protected String getStringCell(Cell cell) {
        if (cell == null) {
            return "";
        }

        if (cell.getCellType().name().equals("NUMERIC")) {

            int numeric = (int) cell.getNumericCellValue();
            return String.valueOf(numeric);
        }
        return cell.getStringCellValue().trim();
    }

    protected LocalDate getDateCell(Cell cell) {
        LocalDate localDate;

        if (cell.getCellType().name().equals("STRING")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            localDate = LocalDate.parse(cell.getStringCellValue(), formatter);

        }

        if (cell.getCellType().name().equals("NUMERIC")) {
            localDate = cell.getDateCellValue().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate();

        } else {
            localDate = LocalDate.of(1970, 1, 1);
        }

        return localDate;
    }

    protected String getCellValueAsString(Cell cell) {
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
}