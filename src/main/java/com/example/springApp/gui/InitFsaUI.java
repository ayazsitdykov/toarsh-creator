package com.example.springApp.gui;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import org.springframework.stereotype.Component;

@Getter
@Component
public class InitFsaUI {

    private JTextArea logArea;
    private JButton toFsaButton;
    private JButton addFromFgisFileButton;
    private JButton saveButtonFsa;
    private JButton selectCompiledFileButton;
    private JButton selectFromFgisFileButton1;
    private JProgressBar progressBar;
    private final JPanel toFsaPanel = new JPanel(new GridLayout(5, 1, 10, 5));

    public void init() {

        selectCompiledFileButton = new JButton
                ("<html><div style='text-align: center;'>Файл отчетов <br> за месяц</html>");
        selectCompiledFileButton.setBackground(Color.CYAN);

        selectFromFgisFileButton1 = new JButton
                ("<html><div style='text-align: center;'> 1 файл отчетов <br> из Аршина</html>");
        selectFromFgisFileButton1.setBackground(Color.CYAN);

        addFromFgisFileButton = new JButton
                ("<html><div style='text-align: center;'> Добавить файл отчетов <br> из Аршина(не обязательно)</html>");
        addFromFgisFileButton.setEnabled(false);// кнопка будет активна после выбора 1 файла
        addFromFgisFileButton.setBackground(Color.CYAN);

        saveButtonFsa = new JButton("Изменить место сохранения");
        saveButtonFsa.setBackground(Color.lightGray);
        saveButtonFsa.setEnabled(false);


        toFsaButton = new JButton
                ("<html><div style='text-align: center;'>Создать файл загрузки <br> в Росаккредитацию</html>");
        toFsaButton.setBackground(Color.PINK);
        toFsaButton.setEnabled(false);

        toFsaPanel.add(selectFromFgisFileButton1);
        toFsaPanel.add(addFromFgisFileButton);
        toFsaPanel.add(selectCompiledFileButton);
        toFsaPanel.add(saveButtonFsa);
        toFsaPanel.add(toFsaButton);

        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.BOLD, 12));
        logArea.setBackground(new Color(240, 240, 240));
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setPreferredSize(new Dimension(850, 500));
    }
}