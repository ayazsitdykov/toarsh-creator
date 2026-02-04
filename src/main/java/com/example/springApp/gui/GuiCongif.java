package com.example.springApp.gui;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.swing.*;

@Configuration
public class GuiCongif {

    @Bean
    public JFileChooser fileChooser() {
        return new JFileChooser();
    }
}
