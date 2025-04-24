package com.example.springApp.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MpiModel {


    HashMap<String, HashMap<String, Integer>> mpi;
    File file;

    public MpiModel(String path) throws IOException {
        mpi = new HashMap<>();
       this.file = new File(path);
        this.mapping();
    }
    public HashMap<String, HashMap<String, Integer>> getMpi() {
        return mpi;
    }

    ObjectMapper objectMapper = new ObjectMapper();

    public void mapping() throws IOException {
    Map<String, Object> listM = objectMapper.readValue(file, new TypeReference<Map<String, Object>>() {});
        listM.entrySet().forEach(k -> {
            try {
                HashMap<String, Integer> ob = (HashMap<String, Integer>) k.getValue();
                mpi.put(k.getKey(), ob);
            } catch (Exception e) {
                try {
                    int value = (Integer) k.getValue();
                    HashMap<String, Integer> hm = new HashMap<>();
                    hm.put("ГВС", value);
                    hm.put("ХВС", value);
                    mpi.put(k.getKey(), hm);
                } catch (Exception ex) {
                    System.out.println("Ошибка файла JSON");
                   ;
                }

            }


        });
        System.out.println(mpi.entrySet());
        System.out.println(mpi.size());

    }
}
