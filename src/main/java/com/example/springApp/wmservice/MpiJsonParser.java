package com.example.springApp.wmservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Getter
public class MpiJsonParser {

    public Map<String, Object> regFifList = new HashMap<>();
    File file;


    public MpiJsonParser(String path) throws IOException {
        this.file = new File(path);
        this.mapping();
    }


    private void mapping() throws IOException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(file);

            // Общий Map для всех данных


            Iterator<Map.Entry<String, JsonNode>> fields = rootNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                String key = entry.getKey();
                JsonNode value = entry.getValue();

                if (key.equals("stop")) {
                    // Обработка списка stop
                    List<String> stopList = new ArrayList<>();
                    for (JsonNode stopItem : value) {
                        stopList.add(stopItem.asText());
                    }
                    regFifList.put(key, stopList);
                } else if (value.isObject()) {
                    // Обработка сложных объектов с ГВС/ХВС
                    Map<String, Object> serviceMap = new LinkedHashMap<>();
                    Iterator<Map.Entry<String, JsonNode>> serviceFields = value.fields();
                    while (serviceFields.hasNext()) {
                        Map.Entry<String, JsonNode> serviceEntry = serviceFields.next();
                        String serviceKey = serviceEntry.getKey();
                        JsonNode serviceValue = serviceEntry.getValue();


                        if (serviceValue.isArray()) {
                            // Обработка массивов
                            List<Object> valuesDate = new ArrayList<>();

                            for (JsonNode item : serviceValue) {
                                if (item.isTextual()) { //добавление массива типов
                                    valuesDate.add(item.textValue());
                                }
                                if (item.isNumber()) { //добавление массива МПИ
                                    valuesDate.add(item.asInt());
                                }

                            }
                            serviceMap.put(serviceKey, valuesDate);

                        } else {
                            if (serviceKey.equals("МПИ")) {
                                serviceMap.put("ГВС", serviceValue.intValue());
                                serviceMap.put("ХВС", serviceValue.intValue());

                            } else {
                                // Обработка одиночных значений
                                serviceMap.put(serviceKey, serviceValue.asInt());
                            }
                        }
                    }
                    regFifList.put(key, serviceMap);
                } else if (value.isNumber()) {
                    // Обработка простых числовых значений
                    Map<String, Integer> createValue = new LinkedHashMap<>();
                    createValue.put("ГВС", value.intValue());
                    createValue.put("ХВС", value.intValue());
                    regFifList.put(key, createValue);
                }
            }


        } catch (Exception e) {
            System.out.println("Ошибка файла JSON");
        }
    }
}



