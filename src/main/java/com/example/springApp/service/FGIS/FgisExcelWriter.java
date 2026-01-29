package com.example.springApp.service.FGIS;

import com.example.springApp.model.IPU;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Slf4j
public class FgisExcelWriter {

    public String excelResult = "";

    public void exelCreator(List<IPU> waterMeterList, String fileName, String savePath) {

        String fileNameFormat = fileName.substring(0, fileName.lastIndexOf('.'));

        try (FileOutputStream out = new FileOutputStream(savePath + fileNameFormat + ".xls");
             Workbook wb = new XSSFWorkbook()) {

            Sheet sheet = wb.createSheet("Sheet1");
            int rowNum = 0;
            int colNums = 0;

            CellStyle dateCellStyle = wb.createCellStyle();
            CreationHelper createHelper = wb.getCreationHelper();
            dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd.MM.yyyy"));

            for (IPU ipu : waterMeterList) {
                Row row = sheet.createRow(rowNum++);
                int i = 0;
                row.createCell(i++).setCellValue(ipu.getMitypeNumber());
                row.createCell(i++).setCellValue(ipu.getManufactureNum());
                row.createCell(i++).setCellValue(ipu.getModification());
                row.createCell(i++).setCellValue(IPU.signCipher);
                row.createCell(i++).setCellValue(ipu.getOwner());

                Cell cell = row.createCell(i++);
                cell.setCellValue(ipu.getVrfDate());
                cell.setCellStyle(dateCellStyle);

                cell = row.createCell(i++);
                cell.setCellValue(ipu.getValidDate());
                cell.setCellStyle(dateCellStyle);

                row.createCell(i++).setCellValue(2);
                row.createCell(i++).setCellValue(String.valueOf(false));
                row.createCell(i++).setCellValue(String.valueOf(ipu.isSignPass()));
                row.createCell(i++).setCellValue(String.valueOf(ipu.isSignMi()));
                row.createCell(i++).setCellValue(ipu.getDocTitle());
                row.createCell(i++).setCellValue(ipu.getEquipment().getAbbreviatedName());
                row.createCell(i++).setCellValue(ipu.getEquipment().getNumberUpsz());
                row.createCell(i++).setCellValue(ipu.getEquipment().getTypeNumIntegral());
                row.createCell(i++).setCellValue(ipu.getEquipment().getManufactureNumIntegral());
                row.createCell(i++).setCellValue(ipu.getEquipment().getTypeNumIva());
                row.createCell(i++).setCellValue(ipu.getEquipment().getManufactureNumIva());
                row.createCell(i++).setCellValue(ipu.getEquipment().getTypeNumTl());
                row.createCell(i++).setCellValue(ipu.getEquipment().getManufactureNumTl());
                row.createCell(i++).setCellValue(ipu.getParams().temperature());
                row.createCell(i++).setCellValue(ipu.getParams().pressure());
                row.createCell(i++).setCellValue(ipu.getParams().humidity());
                row.createCell(i++).setCellValue(ipu.getParams().other());
                row.createCell(i++).setCellValue(ipu.getActNum());
                row.createCell(i).setCellValue(ipu.getAddress());
                colNums = i;

            }

            for (int i = 0; i < colNums; i++) {
                sheet.autoSizeColumn(i);
            }
            wb.write(out);
            excelResult = "Создан файл \"" + fileNameFormat + ".xls\"";

        } catch (IOException e) {
            log.error("Ошибка при создании файла Excel");
            throw new RuntimeException(e);
        }
    }
}