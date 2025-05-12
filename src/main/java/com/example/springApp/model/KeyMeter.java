package com.example.springApp.model;

import lombok.Getter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
public class KeyMeter {
    private String number;
    private LocalDate vrfDate;

    public KeyMeter(String number, LocalDate vrfDate) {
        this.number = number;
        this.vrfDate = vrfDate;
    }

    @Override
    public String toString() {
        return "Key{" +
                "number='" + number + '\'' +
                ", vrfDate=" + vrfDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KeyMeter)) return false;
        KeyMeter key = (KeyMeter) o;
        return Objects.equals(number, key.number) && Objects.equals(vrfDate, key.vrfDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, vrfDate);
    }
}
