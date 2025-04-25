package com.example.springApp.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class MpiJsonParser {

    public HashMap<String, HashMap<String, Integer>> regFifList = new HashMap<>();
    File file;

    public MpiJsonParser(String path) throws IOException {
        this.file = new File(path);
        this.mapping();
    }


    ObjectMapper objectMapper = new ObjectMapper();

    private void mapping() throws IOException {
        Map<String, Object> listM = objectMapper.readValue(file, new TypeReference<>() {
        });

        listM.forEach((key, value) -> {
            try {
                HashMap<String, Integer> ob = (HashMap<String, Integer>) value;
                regFifList.put(key, ob);
            } catch (Exception e) {
                try {
                    int v = (Integer) value;
                    LinkedHashMap<String, Integer> hm = new LinkedHashMap<>();
                    hm.put("ГВС", v);
                    hm.put("ХВС", v);
                    regFifList.put(key, hm);
                } catch (Exception ex) {
                    System.out.println("Ошибка файла JSON");

                }

            }


        });


    }
}
