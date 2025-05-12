package com.example.springApp.services;

import com.example.springApp.model.IPU;
import com.example.springApp.model.KeyMeter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.Map;


public class ExelWriter {

    public void exelCreator(Map<KeyMeter, IPU> waterMeterList, String fileName, String savePath) {


        String fileNameFormat = fileName.substring(0, fileName.lastIndexOf('.')); //убираем расширение из названия


        try (FileOutputStream out = new FileOutputStream(savePath + fileNameFormat + ".xls")) {
            Workbook wb = new HSSFWorkbook();

            Sheet sheet = wb.createSheet("Sheet1");
            int rowNum = 0;
           /* int cellNum = 0;

             //создаем подписи к столбцам (это будет первая строчка в листе Excel файла)
            Row row0 = sheet.createRow(rowNum);
            row0.createCell(cellNum++).setCellValue("Госреестр");
            row0.createCell(cellNum++).setCellValue("Номер счетчика");
            row0.createCell(cellNum++).setCellValue("Тип");
            row0.createCell(cellNum++).setCellValue("Шифр клейма");
            row0.createCell(cellNum++).setCellValue("Собственник СИ");
            row0.createCell(cellNum++).setCellValue("Дата поверки");
            row0.createCell(cellNum++).setCellValue("Действительно до");
            row0.createCell(cellNum++).setCellValue("ns1:type");
            row0.createCell(cellNum++).setCellValue("Калибровка");
            row0.createCell(cellNum++).setCellValue("Отметка в паспорте");
            row0.createCell(cellNum++).setCellValue("Отметка на счетчике");
            row0.createCell(cellNum++).setCellValue("ns1:docTitle");
            row0.createCell(cellNum++).setCellValue("Метролог");
            row0.createCell(cellNum++).setCellValue("УПСЖ");
            row0.createCell(cellNum++).setCellValue("Рег № Секундомер");
            row0.createCell(cellNum++).setCellValue("Зав № Секундомер");
            row0.createCell(cellNum++).setCellValue("Рег № ИВА");
            row0.createCell(cellNum++).setCellValue("Зав № ИВА");
            row0.createCell(cellNum++).setCellValue("Рег № Термометр");
            row0.createCell(cellNum++).setCellValue("Зав № Термометр");
            row0.createCell(cellNum++).setCellValue("Температура");
            row0.createCell(cellNum++).setCellValue("Давление");
            row0.createCell(cellNum++).setCellValue("Влажность");
            row0.createCell(cellNum++).setCellValue("Температура воды");
            row0.createCell(cellNum++).setCellValue("Номер акта");
            row0.createCell(cellNum).setCellValue("Адрес");

            */

                for (IPU ipu : waterMeterList.values()) {
                    Row row = sheet.createRow(rowNum++);
                    int i = 0;
                    row.createCell(i++).setCellValue(ipu.getMitypeNumber());
                    row.createCell(i++).setCellValue(ipu.getManufactureNum());
                    row.createCell(i++).setCellValue(ipu.getModification());
                    row.createCell(i++).setCellValue(ipu.getSignCipher());
                    row.createCell(i++).setCellValue(ipu.getOwner());
                    row.createCell(i++).setCellValue(ipu.getVrfDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                    row.createCell(i++).setCellValue(ipu.getValidDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                    row.createCell(i++).setCellValue(2);
                    row.createCell(i++).setCellValue(String.valueOf(false));
                    row.createCell(i++).setCellValue(String.valueOf(false));
                    row.createCell(i++).setCellValue(String.valueOf(true));
                    row.createCell(i++).setCellValue(ipu.getDocTitle());
                    row.createCell(i++).setCellValue(ipu.getMetrologist());
                    row.createCell(i++).setCellValue(ipu.getNumberUpsz());
                    row.createCell(i++).setCellValue(ipu.getTypeNumIntegral());
                    row.createCell(i++).setCellValue(ipu.getManufactureNumIntegral());
                    row.createCell(i++).setCellValue(ipu.getTypeNumIva());
                    row.createCell(i++).setCellValue(ipu.getManufactureNumIva());
                    row.createCell(i++).setCellValue(ipu.getTypeNumTl());
                    row.createCell(i++).setCellValue(ipu.getManufactureNumTl());
                    row.createCell(i++).setCellValue(ipu.getTemperature());
                    row.createCell(i++).setCellValue(ipu.getPressure());
                    row.createCell(i++).setCellValue(ipu.getHumidity());
                    row.createCell(i++).setCellValue(ipu.getOther());
                    row.createCell(i++).setCellValue(ipu.getActNum());
                    row.createCell(i).setCellValue(ipu.getAddress());

            }
            // Автонастройка ширины столбцов
            for (int i = 0; i < 26; i++) {
                sheet.autoSizeColumn(i);
            }
            wb.write(out);
            System.out.println("Создан файл \"" + fileNameFormat + ".xls\"");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

