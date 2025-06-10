package com.example.springApp;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        String address = "УЛ.Ц.БУЛЬВАР 15-15";

        String addressReplased = address.toLowerCase().replaceAll("сибирека", "сибиряка")
                .replaceAll("8марта", "8 марта")
                .replaceAll("40летоктября", "40 лет октября");
        String format = Arrays.stream(addressReplased.split("\\."))
                .map(s -> s.isEmpty() ? s : s.substring(0, 1).toUpperCase() + s.substring(1))
                .collect(Collectors.joining("."));
        String addressFormated = Arrays.stream(format.split(" "))
                .map(s -> s.isEmpty() ? s : s.substring(0, 1).toUpperCase() + s.substring(1))
                .collect(Collectors.joining(" "));



        System.out.println(addressFormated);

    }
}
