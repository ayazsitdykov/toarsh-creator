package com.example.springApp.parser;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Slf4j
public class TxtParser {

    public List<String> parseToList(String path) {

        List<String> parsedList;
        try {

            parsedList = Files.readAllLines(Path.of(path)).stream()
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .toList();

        } catch (IOException e) {
            log.error("Error while parsing file", e);
            throw new RuntimeException(e);
        }

        return parsedList;
    }
}