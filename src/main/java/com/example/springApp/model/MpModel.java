package com.example.springApp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record MpModel(
        @JsonProperty("Методика поверки")
        String name,
        @JsonProperty("Приборы")
        List<String> meters
) {
}
