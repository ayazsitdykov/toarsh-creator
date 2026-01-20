package com.example.springApp.model;

public record Params(
        String temperature,
        String pressure,
        String humidity,
        String other
) {
}
