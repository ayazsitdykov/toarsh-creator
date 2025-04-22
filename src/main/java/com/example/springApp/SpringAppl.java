package com.example.springApp;

import com.example.springApp.model.IPUModel;
import com.example.springApp.model.Key;
import com.example.springApp.waterservice.ErrorChecking;
import com.example.springApp.waterservice.ExelParser;
import com.example.springApp.waterservice.ParametersCreator;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;

@SpringBootApplication
public class SpringAppl {

    public static void main(String[] args) {
        //SpringApplication.run(SpringAppl.class, args);
        ExelParser parser = new ExelParser();
        HashMap<Key, IPUModel> waterMeterList = parser.parse("file.xlsx");
        ParametersCreator creator = new ParametersCreator("ГВ");

        ErrorChecking ec = new ErrorChecking(waterMeterList);







    }
}


