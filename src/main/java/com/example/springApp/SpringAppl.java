package com.example.springApp;

import com.example.springApp.model.Equipment;
import com.example.springApp.model.IPU;
import com.example.springApp.model.Key;
import com.example.springApp.services.XMLWriter;
import com.example.springApp.waterservice.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class SpringAppl {

    public static void main(String[] args) throws IOException {
        //SpringApplication.run(SpringAppl.class, args);
        String filePath = "file.xlsx";
        String savePath = "C:/Users/a.sitdykov/Desktop/project/toarsh-creator/result/"; // путь сохранения файлов XML и Excel

        HashMap<Key, IPU> waterMeterList = new ExelParser().parse(filePath);

        new CreatorParameters().paramCreate(waterMeterList);

        Map<String, Object> regFifList = new MpiJsonParser("regFif.json").regFifList;

        ErrorChecking ec = new ErrorChecking(waterMeterList, regFifList);
        List<Equipment> eqL = new EquipmentParser().parse("equipment.json");
        new EquipmentWriter().writingByMetrologist(waterMeterList, eqL);

        new XMLWriter().toArchWriter(waterMeterList, filePath, savePath);




    }
}


