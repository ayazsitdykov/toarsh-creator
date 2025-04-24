package com.example.springApp;

import com.example.springApp.model.MpiModel;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class SpringAppl {

    public static void main(String[] args) throws IOException {
        //SpringApplication.run(SpringAppl.class, args);
        //ExelParser parser = new ExelParser();
       // HashMap<Key, IPUModel> waterMeterList = parser.parse("file.xlsx");
       // ParametersCreator creator = new ParametersCreator("ГВ");

       // ErrorChecking ec = new ErrorChecking(waterMeterList);

        MpiModel regFifMeters = new MpiModel("RegFif.json");






    }
}


