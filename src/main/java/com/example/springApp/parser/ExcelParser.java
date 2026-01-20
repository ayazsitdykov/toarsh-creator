package com.example.springApp.parser;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ExcelParser {

    public List<Sheet> parse(String path) {
        List<Sheet> sheets = new ArrayList<>();
        try (InputStream inputStream = new FileInputStream(path)) {
            Workbook workbook;
            if (path.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(inputStream);

            } else if (path.endsWith(".xls")) {
                workbook = new HSSFWorkbook(inputStream);
            } else {
                log.error("Unsupported file format");
                throw new RuntimeException("Unsupported file format");
            }
            int numberOfSheets = workbook.getNumberOfSheets();
            for (int i = 0; i < numberOfSheets; i++) {
                sheets.add(workbook.getSheetAt(i));
            }
            return sheets;

        } catch (IOException e) {
            log.error("Error reading file: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}