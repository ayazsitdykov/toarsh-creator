package com.example.springApp.config;

import com.example.springApp.model.Equipment;
import com.example.springApp.model.MeterDescription;
import com.example.springApp.model.MpModel;
import com.example.springApp.parser.JsonParser;
import com.example.springApp.parser.TxtParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SpringConfig {

    @Value("${files.path.reg-fif-json}")
    private String regFifFile;

    @Value("${files.path.stop-list}")
    private String stopListFile;

    @Value("${files.path.mp-other}")
    private String mpOtherFile;

    @Value("${files.path.equipment}")
    private String equipmentFile;

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }

    @Bean
    public List<MeterDescription> regFifList() {
        JsonParser<MeterDescription> rFParser = new JsonParser<>(MeterDescription.class, new ObjectMapper());
        return rFParser.parse(regFifFile);
    }

    @Bean
    public List<Equipment> equipmentList() {
        JsonParser<Equipment> equipmentJParser = new JsonParser<>(Equipment.class, new ObjectMapper());
        return equipmentJParser.parse(equipmentFile).stream()
                .peek(e -> e.setAbbreviatedName(abbreviateMiddleName(e.getMetrologist())))
                .toList();
    }

    @Bean
    public List<String> stopList() {
        TxtParser tParser = new TxtParser();
        return tParser.parseToList(stopListFile);
    }

    @Bean
    public List<MpModel> otherMethodicList() {
        JsonParser<MpModel> mParser = new JsonParser<>(MpModel.class, new ObjectMapper());
        return mParser.parse(mpOtherFile);
    }

    private String abbreviateMiddleName(String fullName) {
        if (fullName.isBlank()) {
            return null;
        }
        return fullName.trim()
                .replaceAll("^(\\S+)\\s+(\\S)\\S*\\s+(\\S)\\S*$", "$1 $2. $3.");
    }

}