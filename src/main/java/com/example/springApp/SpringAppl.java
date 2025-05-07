package com.example.springApp;

import com.example.springApp.model.Equipment;
import com.example.springApp.model.IPU;
import com.example.springApp.model.Key;
import com.example.springApp.services.ExelWriter;
import com.example.springApp.services.XMLWriter;
import com.example.springApp.wmservice.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class SpringAppl {

    public static void main(String[] args) throws IOException {


        //SpringApplication.run(SpringAppl.class, args);
        String filePath = "C:\\Users\\a.sitdykov\\Desktop\\project\\source\\Кировград апрель.2025.xlsx";
        String fileName = filePath.substring(filePath.lastIndexOf('\\') + 1);

        String savePath = "C:/Users/a.sitdykov/Desktop/project/result/"; // путь сохранения файлов XML и Excel

        HashMap<Key, IPU> waterMeterList = new ExelParser().parse(filePath);

        Map<String, Object> regFifList = new MpiJsonParser("regFif.json").regFifList;

        ErrorChecking ec = new ErrorChecking(waterMeterList, regFifList);


        if ( !ec.hasError) {
            new CreatorParameters().paramCreate(waterMeterList);

            List<Equipment> eqL = new EquipmentParser().parse("equipment.json");
            new EquipmentWriter().writingByMetrologist(waterMeterList, eqL);

            new XMLWriter().toArchWriter(waterMeterList, fileName, savePath);
            new ExelWriter().exelCreator(waterMeterList, fileName, savePath);

        }




    }
}


