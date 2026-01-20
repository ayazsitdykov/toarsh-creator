package com.example.springApp.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;

@Slf4j
public class JsonParser<T> {

    private final ObjectMapper mapper;
    private final Class<T> type;

    public JsonParser(Class<T> type, ObjectMapper mapper) {
        this.type = type;
        this.mapper = mapper;
    }

    public List<T> parse(String path) {

        List<T> parsedList;
        File file = new File(path);
        try {
            parsedList = mapper.readValue(file,
                    mapper.getTypeFactory()
                            .constructCollectionType(List.class, type));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return parsedList;
    }
}