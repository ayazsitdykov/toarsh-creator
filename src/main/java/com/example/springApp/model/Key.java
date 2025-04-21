package com.example.springApp.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
public class Key {
    private String number;
    private LocalDate vrfDate;

    public Key(String number, LocalDate vrfDate) {
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
        if (!(o instanceof Key)) return false;
        Key key = (Key) o;
        return Objects.equals(number, key.number) && Objects.equals(vrfDate, key.vrfDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, vrfDate);
    }
}
