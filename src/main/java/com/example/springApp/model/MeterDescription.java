package com.example.springApp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public record MeterDescription(
        @JsonProperty("Рег.номер")
        String regNumber,
        @JsonProperty("МПИ")
        Object mpi,
        @JsonProperty("ТИП")
        List<String> types,
        @JsonProperty("Знак в паспорте")
        boolean signPass,
        @JsonProperty("Знак на СИ")
        boolean signMi
) {
    public boolean isMpiObject() {
        return mpi instanceof Map;
    }

    public boolean isMpiInteger() {
        return mpi instanceof Integer;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getMpiAsMap() {
        return (Map<String, Object>) mpi;
    }

    public Integer getMpiAsInteger() {
        return (Integer) mpi;
    }

    public boolean isListTypeMpiValue() {
        if (isMpiObject()) {
            return getMpiAsMap().values().stream()
                    .findFirst()
                    .map(val -> val instanceof List<?>)
                    .orElse(false);
        }
        return false;
    }
}