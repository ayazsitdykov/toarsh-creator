package com.example.springApp.wmservice;

import com.example.springApp.model.IPU;
import com.example.springApp.model.KeyMeter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



import java.io.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExcelParser {
    String addressInMemory = "Ул. Ленина, " + (new Random().nextInt(100) + 50)
            + " - " + (new Random().nextInt(5) + 10);
    //Если в файле отсутсвует адрес в 1 строчке-устанавливаем это значение

    String metrologyMemory = "Ситдыков Р. Н.";//Если в файле отсутсвует поверитель-устанавливаем это значение

    public LinkedHashMap<KeyMeter, IPU> waterMeterList = new LinkedHashMap<>();

    public LinkedHashMap<KeyMeter, IPU> parse(String filePath) {


        try (InputStream inputStream = new FileInputStream(filePath);
             XSSFWorkbook workBook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workBook.getSheetAt(0);

            for (Row row : sheet) {

                if (row.getRowNum() == 0) { //пропускаем первую строчку
                    if (isFileCorrect(row)) {
                        continue;
                    }

                    System.out.println("Выбран некорректный файл excel");
                    break;

                }


                if (row.getCell(0) == null || !row.getCell(0).getCellType().name().equals("STRING")) { //прерываем цикл, если в первой ячейке строчки не строка
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
                waterMeter.setMetrologist(getMetrologist(row.getCell(9)));
                KeyMeter key = new KeyMeter(waterMeter.getManufactureNum(), waterMeter.getVrfDate());

                if (isMeterExist(key)) {
                    System.out.println("Счетчик с номером " + key.getNumber() + " встречается повторно!");
                    return null; //возвращаем объект null, если счетчики повторяются
                }
                waterMeterList.put(key, waterMeter);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return waterMeterList;
    }


    private String getMetrologist(Cell cell) {
        String metrologist = getStringCell(cell);
        if (!metrologist.equals("")) {
            metrologyMemory = metrologist.replaceAll("\\.(\\p{L})", ". $1");
            //записываем в память значение
        }
        //если ячейка пустая-возвращаем последнее значение записанное в metrologyMemory
        return metrologyMemory;
    }

    private String getOwner(Cell cell) {

        boolean isCompany = getStringCell(cell)
                .toLowerCase().trim()
                .matches("юл|юр\\.?\\s?лицо|юр лицо|юридическое лицо");

        return isCompany ? "Юридическое лицо" : "Физическое лицо";   }


    private LocalDate getDateCell(Cell cell) {
        if (!cell.getCellType().name().equals("NUMERIC")) {
            return LocalDate.of(1970,1,1);
        }

        return cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private String getStringCell(Cell cell) {

        if (cell == null) {

            return "";
        }

        if (cell.getCellType().name().equals("NUMERIC")) {
            System.out.println(cell.getCellType().name());

            String str = String.valueOf(cell.getNumericCellValue()).replace(".", "")
                    .replace("E7", "");
            return str;
        }


        return cell.getStringCellValue().trim();
    }


    private String getFormatAddress(Cell cell) { //метод приведения адреса к формату "Ул. Ленина 99 - 101"
        // toDo Реализовать проверку всех типов адресов
        String address = getStringCell(cell);
        if (address.equals("")) {
            //если ячейка пустая-возвращаем последнее значение записанное в addressInMemory;
            return addressInMemory;
        }
        String formatted = address.trim()
                .replaceAll("[Уу]лица", "Ул.")
                .replaceAll("[Пп]роспект", "Пр.")
                .replaceAll("\\bд\\.|дом\\b|корп\\.|к\\.|строение\\b|стр\\.", "")
                .replaceAll("\\s+", " ")
                .replaceAll("\\s*,\\s*", " ")
                .replaceAll("\\s*/\\s*", "/");
        Pattern pattern = Pattern.compile(
                "^([Уу][Лл]\\.|[Пп][Рр]\\.|[Пп][Ее][Рр]\\.)?\\s*([А-Яа-яЁё-]+)\\s*(\\d+[А-Яа-яЁё]?)(?:\\s*[-–—]\\s*(\\d+[А-Яа-яЁё]?))?.*$"
        );
        Matcher matcher = pattern.matcher(formatted);
        if (matcher.matches()) {

            String streetType = matcher.group(1) != null ?
                    matcher.group(1).replaceAll("[уУ][Лл]\\.", "Ул.").replaceAll("[пП][Рр]\\.", "Пр.")
                            .replaceAll("[Пп][Ее][Рр]\\.", "Пер.") : "Ул.";

            String sName = (matcher.group(2).trim());
            String streetName = sName.substring(0, 1).toUpperCase() + sName.substring(1).toLowerCase();
            String startNumber = matcher.group(3);
            String endNumber = matcher.group(4);

            // 3. Собираем отформатированный адрес
            if (endNumber != null && !endNumber.isEmpty()) {
                addressInMemory = String.format("%s %s %s - %s", streetType, streetName, startNumber, endNumber);
                return addressInMemory;
            } else {
                addressInMemory = String.format("%s %s %s", streetType, streetName, startNumber);
                return addressInMemory;
            }
        }

        return address;
        // если значение не подходит под шаблон-возвращаем без именений
    }

    private boolean isFileCorrect(Row row) {
        String firstColumnName = row.getCell(0).getStringCellValue();
        String secondColumnName = row.getCell(5).getStringCellValue();

        String firstColumnNameOrigin = "Номер \n" +
                "в госреестре";
        String secondColumnNameOrigin = "ХВС/ГВС";


        return firstColumnNameOrigin.equals(firstColumnName) & secondColumnNameOrigin.equals(secondColumnName);
    }

    private boolean isMeterExist(KeyMeter key) {


        for (KeyMeter keyM : waterMeterList.keySet()) {
            if (keyM.getNumber().equals(key.getNumber())) {
                return true;
            }
        }
        return false;
    }


}


