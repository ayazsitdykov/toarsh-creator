package com.example.springApp;

import com.example.springApp.model.Equipment;
import com.example.springApp.model.IPU;
import com.example.springApp.model.KeyMeter;
import com.example.springApp.services.ExelWriter;
import com.example.springApp.services.XMLWriter;
import com.example.springApp.wmservice.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Path;
import java.util.Map;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class GUI extends JFrame {
    private JLabel statusLabel;
    private JTextArea logArea;
    private JButton processButton;
    private JButton saveButton;
    private JProgressBar progressBar;
    private File selectedFile;
    private Path savePath;

    public GUI() {
        setTitle("Формирование XML в аршин");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Control panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));

        JButton selectButton = new JButton("Выберите файл Excel");
        selectButton.addActionListener(this::selectFileAction);

        saveButton = new JButton("Выбрать место сохранения");
        saveButton.setEnabled(false);
        saveButton.addActionListener(this::selectSaveLocation);


        processButton = new JButton("Обработать файл");
        processButton.setEnabled(false);
        processButton.addActionListener(this::processFileAction);


        controlPanel.add(selectButton);
        controlPanel.add(saveButton);
        controlPanel.add(processButton);
        // Progress bar
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);

        // Log area with proper styling
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        logArea.setBackground(new Color(240, 240, 240));
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setPreferredSize(new Dimension(850, 500));

        // Status bar
        statusLabel = new JLabel("Готов к работе");
        statusLabel.setBorder(BorderFactory.createLoweredBevelBorder());

        // Add components
        mainPanel.add(controlPanel, BorderLayout.NORTH);
        mainPanel.add(progressBar, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(statusLabel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void selectFileAction(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Excel files (*.xls, *.xlsx)", "xls", "xlsx"));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            logMessage("Выбран файл: " + selectedFile.getName());
            saveButton.setEnabled(true);
        }
    }

    private void selectSaveLocation(ActionEvent e) {
        JFileChooser dirChooser = new JFileChooser();
        dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        dirChooser.setDialogTitle("Выберите папку для сохранения результатов");


        if (dirChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            savePath = dirChooser.getSelectedFile().toPath();
            logMessage("Выбрана папка для сохранения: " + savePath);
            processButton.setEnabled(true);
        }
    }

    private void processFileAction(ActionEvent e) {
        if (selectedFile == null) return;
        if (savePath == null) return;

        processButton.setEnabled(false);
        saveButton.setEnabled(false);
        progressBar.setVisible(true);
        progressBar.setIndeterminate(true);
        logMessage("Обработка...");

        SwingWorker<Void, String> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                publish("Чтение файла...");
                String filePath = selectedFile.getAbsolutePath();
                String fileName = filePath.substring(filePath.lastIndexOf('\\') + 1);

                // String savePath = "C:/Users/a.sitdykov/Desktop/project/result/"; // путь сохранения файлов XML и Excel

                Map<KeyMeter, IPU> waterMeterList = new ExelParser().parse(filePath);

                publish("Проверка ошибок...");

                Map<String, Object> regFifList = new MpiJsonParser("regFif.json").regFifList;

                List<Equipment> eqL = new EquipmentParser().parse("equipment.json");
                new EquipmentWriter().writingByMetrologist(waterMeterList, eqL);


                ErrorChecking ec = new ErrorChecking(waterMeterList, regFifList);
                logMessage(ec.errorChecking().toString());


                if (!ec.hasError) {

                    System.out.println("Прочитан файл, содержащий " + waterMeterList.size() + " счетчиков");
                    new CreatorParameters().paramCreate(waterMeterList);


                    publish("Запись файлов...");
                    new XMLWriter().toArchWriter(waterMeterList, fileName, savePath.toString());
                    new ExelWriter().exelCreator(waterMeterList, fileName, savePath.toString());


                }
                return null;
            }

            @Override
            protected void process(java.util.List<String> chunks) {
                for (String message : chunks) {
                    logMessage(message);
                }
            }

            @Override
            protected void done() {
                try {
                    get(); // Check for exceptions
                    logMessage("Обработка завершена");
                    statusLabel.setText("Processing completed");
                } catch (InterruptedException | ExecutionException ex) {
                    logMessage("ERROR: " + ex.getMessage());
                    statusLabel.setText("Ошибка");
                } finally {
                    progressBar.setVisible(false);
                    processButton.setEnabled(true);
                    saveButton.setEnabled(true);
                }
            }
        };

        worker.execute();
    }

    private void logMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            logArea.append("[" + java.time.LocalTime.now() + "] " + message + "\n");
            // Auto-scroll to bottom
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUI app = new GUI();
            app.setVisible(true);
        });
    }
}




