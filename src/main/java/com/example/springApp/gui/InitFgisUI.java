package com.example.springApp.gui;

import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Getter
@Component
public class InitFgisUI {

    private JLabel statusLabel;
    private JTextArea logArea;
    private JButton toFgisButton;
    private JButton saveButtonFgis;
    private JButton selectRawFileButton;
    private JProgressBar progressBar;
    private JPanel mainPanel;

    public void init() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel toFgisPanel = new JPanel(new GridLayout(5, 1, 10, 5));

        selectRawFileButton = new JButton("Загрузить журнал городов");

        saveButtonFgis = new JButton("Изменить место сохранения");
        saveButtonFgis.setEnabled(false);

        toFgisButton = new JButton
                ("<html><div style='text-align: center;'>Создать файл загрузки <br> в Аршин</html>");
        toFgisButton.setEnabled(false);

        toFgisPanel.add(selectRawFileButton);
        toFgisPanel.add(saveButtonFgis);
        toFgisPanel.add(toFgisButton);

        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.BOLD, 12));
        logArea.setBackground(new Color(240, 240, 240));
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setPreferredSize(new Dimension(850, 500));

        statusLabel = new JLabel("Готов к работе");
        statusLabel.setBorder(BorderFactory.createLoweredBevelBorder());

        mainPanel.add(toFgisPanel, BorderLayout.WEST);
        mainPanel.add(progressBar, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(statusLabel, BorderLayout.SOUTH);
    }
}