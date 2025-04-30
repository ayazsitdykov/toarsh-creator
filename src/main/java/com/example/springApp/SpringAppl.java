package com.example.springApp;

import com.example.springApp.model.Equipment;
import com.example.springApp.model.IPU;
import com.example.springApp.model.Key;
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
        HashMap<Key, IPU> waterMeterList = new ExelParser().parse("file.xlsx");

        new CreatorParameters().paramCreate(waterMeterList);

        Map<String, Object> regFifList = new MpiJsonParser("regFif.json").regFifList;

        ErrorChecking ec = new ErrorChecking(waterMeterList, regFifList);
        List<Equipment> eqL = new EquipmentParser().parse("equipment.json");
        new EquipmentWriter().writingByMetrologist(waterMeterList, eqL);

        for (IPU ip : waterMeterList.values()) {
            System.out.println(ip.getManufactureNum() + " " + ip.getMetrologist() + " " + ip.getOwner());


        }




    }
}


