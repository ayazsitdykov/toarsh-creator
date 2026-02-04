package com.example.springApp;

import com.example.springApp.gui.MainFrame;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import javax.swing.*;


@SpringBootApplication
public class Main {
    public static void main(String[] args) {

        System.setProperty("java.awt.headless", "false");

        var ctx = new SpringApplicationBuilder(Main.class)
                .headless(false)                    // GUI приложение
                .web(WebApplicationType.NONE)       // Не веб-приложение
                .run(args);
        SpringApplication.run(Main.class, args);
        SwingUtilities.invokeLater(() -> {
            MainFrame app = ctx.getBean(MainFrame.class);
            app.setVisible(true);
        });

    }

}