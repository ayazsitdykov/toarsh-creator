package com.example.springApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.concurrent.ExecutionException;

public class GUI extends JFrame {
    private JLabel statusLabel;
    private JTextArea resultArea;
    private JButton processButton;
    private JProgressBar progressBar;
    private File selectedFile;

    public GUI() {
        setTitle("Обработчик Excel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        // Главная панель с BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Панель управления сверху
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JButton selectButton = new JButton("Выбрать файл");
        selectButton.addActionListener(this::selectFileAction);

        processButton = new JButton("Обработать");
        processButton.setEnabled(false);
        processButton.addActionListener(this::processFileAction);

        controlPanel.add(selectButton);
        controlPanel.add(processButton);

        // Прогресс-бар
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);

        // Область результатов с JTextArea
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        // Статус бар внизу
        statusLabel = new JLabel("Готов к работе");
        statusLabel.setBorder(BorderFactory.createLoweredBevelBorder());

        // Добавляем компоненты
        mainPanel.add(controlPanel, BorderLayout.NORTH);
        mainPanel.add(progressBar, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(statusLabel, BorderLayout.SOUTH);

        // Начальное состояние - скрываем прогресс-бар
        progressBar.setVisible(false);
        scrollPane.setVisible(true);

        add(mainPanel);
    }

    private void selectFileAction(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Excel файлы (*.xls, *.xlsx)", "xls", "xlsx"));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            statusLabel.setText("Выбран файл: " + selectedFile.getName());
            processButton.setEnabled(true);
            resultArea.setText("");
        }
    }

    private void processFileAction(ActionEvent e) {
        if (selectedFile == null) return;

        // Блокируем кнопку на время обработки
        processButton.setEnabled(false);

        // Показываем прогресс
        progressBar.setVisible(true);
        progressBar.setIndeterminate(true);
        statusLabel.setText("Обработка файла...");

        // Создаем фоновую задачу
        SwingWorker<String, Void> worker = new SwingWorker<>() {
            @Override
            protected String doInBackground() throws Exception {
                


                // Здесь должен быть ваш реальный код обработки
                // List<Device> devices = ExcelParser.parseDevicesFromExcel(selectedFile.getPath());

                return "Файл успешно обработан!\n" +
                        "Путь: " + selectedFile.getAbsolutePath() + "\n" +
                        "Размер: " + selectedFile.length()/1024 + " KB";
            }

            @Override
            protected void done() {
                try {
                    String result = get();
                    resultArea.setText(result);
                    statusLabel.setText("Обработка завершена");
                } catch (InterruptedException | ExecutionException ex) {
                    statusLabel.setText("Ошибка обработки");
                    resultArea.setText("Ошибка: " + ex.getMessage());
                    ex.printStackTrace();
                } finally {
                    progressBar.setVisible(false);
                    processButton.setEnabled(true);
                }
            }
        };

        worker.execute();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUI app = new GUI();
            app.setVisible(true);
        });
    }
}