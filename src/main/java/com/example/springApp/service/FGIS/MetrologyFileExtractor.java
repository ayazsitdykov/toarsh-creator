package com.example.springApp.service.FGIS;

import com.example.springApp.model.Equipment;
import com.example.springApp.model.IPU;
import com.example.springApp.service.ExcelExtractor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class MetrologyFileExtractor extends ExcelExtractor {

    private final List<Equipment> equipmentList;

    private String addressMemory = "Ул. Ленина, " + (new Random().nextInt(100) + 50)
            + " - " + (new Random().nextInt(5) + 10);
    private String metrologyMemory = "Ситдыков Р. Н.";

    public String parsingResult = "";

    public List<IPU> transfer(Sheet sheet) {

        List<IPU> waterMeterList = new LinkedList<>();

        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                if (isFileCorrect(row)) {
                    continue;
                }
                parsingResult = "Выбран некорректный файл excel";
                return null;
            }

            if (row.getCell(0) == null
                    || !row.getCell(0).getCellType().name().equals("STRING")) {
                break;
            }

            IPU waterMeter = new IPU();
            waterMeter.setMitypeNumber(getStringCell(row.getCell(0)));
            waterMeter.setManufactureNum(getStringCell(row.getCell(1)));
            waterMeter.setModification(getStringCell(row.getCell(2)));
            waterMeter.setVrfDate(getDateCell(row.getCell(3)));
            waterMeter.setValidDate(getDateCell(row.getCell(4)));
            waterMeter.setHot(getStringCell((row.getCell(5))).equalsIgnoreCase("ГВС"));
            waterMeter.setAddress(getFormatAddress(row.getCell(6)));
            waterMeter.setActNum(getStringCell(row.getCell(7)));
            waterMeter.setOwner(getOwner(row.getCell(8)));
            waterMeter.setEquipment((fillEquipment(getMetrologist(row.getCell(9)))));

            if (waterMeterList.stream().anyMatch(waterMeter::equals)) {
                parsingResult = "Счетчик с номером " + waterMeter.getMitypeNumber() + " встречается повторно!";
                return null; //возвращаем объект null, если счетчики повторяются
            }
            waterMeterList.add(waterMeter);
        }
        return waterMeterList;
    }

    private Equipment fillEquipment(String metrologist) {
       return equipmentList.stream()
                .filter(eq -> eq.getAbbreviatedName() != null)
                .filter(eq -> eq.getAbbreviatedName().equals(metrologist))
                .findFirst().orElse(null);
    }


    private String getMetrologist(Cell cell) {
        String metrologist = getStringCell(cell);
        if (!metrologist.isEmpty()) {
            metrologyMemory = metrologist.replaceAll("\\.(\\p{L})", ". $1");
        }
        return metrologyMemory;
    }

    private String getOwner(Cell cell) {

        boolean isCompany = getStringCell(cell)
                .toLowerCase().trim()
                .matches("юл|юр\\.?\\s?лицо|юр лицо|юридическое лицо");

        return isCompany ? "Юридическое лицо" : "Физическое лицо";
    }

    private String getFormatAddress(Cell cell) {

        String address = getStringCell(cell);
        if (address.isEmpty()) {
            return addressMemory;
        }
        String replaced = address.trim().toLowerCase()
                .replaceAll("[Уу]лица", "Ул.")
                .replaceAll("[Пп]роспект", "Пр.")
                .replaceAll("\\bд\\.|дом\\b|корп\\.|к\\.|строение\\b|стр\\.", "")
                .replaceAll("\\s+", " ")
                .replaceAll("\\s*,\\s*", " ")
                .replaceAll("\\s*/\\s*", "/")
                .replaceAll("(\\D)(?<! )(\\d)", "$1 $2")
                .replaceAll("(\\d)(?<! )(\\D)", "$1 $2")
                .replaceAll("сибирека", "сибиряка")
                .replaceAll("8марта", "8 марта")
                .replaceAll("40летоктября", "40 лет октября");
        String splitPoint = Arrays.stream(replaced.split("\\."))
                .map(s -> s.isEmpty() ? s : s.substring(0, 1).toUpperCase() + s.substring(1))
                .collect(Collectors.joining("."));
        addressMemory = Arrays.stream(splitPoint.split(" "))
                .map(s -> s.isEmpty() ? s : s.substring(0, 1).toUpperCase() + s.substring(1))
                .collect(Collectors.joining(" "));

        return addressMemory;
    }

    private boolean isFileCorrect(Row row) {
        if (row.getCell(0) == null || row.getCell(5) == null
                || !row.getCell(0).getCellType().name().equals("STRING")
                || !row.getCell(5).getCellType().name().equals("STRING")) {
            return false;
        }
        String firstColumnName = row.getCell(0).getStringCellValue();
        String secondColumnName = row.getCell(5).getStringCellValue();

        String firstColumnNameOrigin = "Номер \n" +
                "в госреестре";
        String secondColumnNameOrigin = "ХВС/ГВС";

        return firstColumnNameOrigin.equals(firstColumnName) & secondColumnNameOrigin.equals(secondColumnName);
    }
}