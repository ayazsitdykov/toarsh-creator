package com.example.springApp;

import com.example.springApp.model.IPU;
import com.example.springApp.model.Key;
import com.example.springApp.waterservice.MpiJsonParser;
import com.example.springApp.waterservice.ErrorChecking;
import com.example.springApp.waterservice.ExelParser;
import com.example.springApp.waterservice.CreatorParameters;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.HashMap;

@SpringBootApplication
public class SpringAppl {

    public static void main(String[] args) throws IOException {
        //SpringApplication.run(SpringAppl.class, args);
        ExelParser parser = new ExelParser();
        HashMap<Key, IPU> waterMeterList = parser.parse("file.xlsx");

        new CreatorParameters(waterMeterList);

        HashMap<String, HashMap<String, Integer>> regFifList = new MpiJsonParser("RegFif.json").regFifList;

        ErrorChecking ec = new ErrorChecking(waterMeterList, regFifList);




    }
}


