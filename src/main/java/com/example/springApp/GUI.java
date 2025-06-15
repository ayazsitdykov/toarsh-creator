package com.example.springApp;

import com.example.springApp.model.Equipment;
import com.example.springApp.model.IPU;
import com.example.springApp.model.KeyMeter;
import com.example.springApp.services.ExcelWriter;
import com.example.springApp.services.XMLWriter;
import com.example.springApp.wmservice.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;


public class GUI extends JFrame {
    private JLabel statusLabel;
    private JTextArea logArea;

    private JButton toFgisButton;
    private JButton toFsaButton;

    private JButton saveButton1;
    private JButton saveButton2;

    private JProgressBar progressBar;

    private File selectedFileToFgis;
    private File selectedFileFromFgis;
    private File selectedCompiledFile;

    private String saveToFgisPath;
    private String saveToFsaPath;

    private final JFileChooser fileChooser = new JFileChooser();

    public GUI() {
        setTitle("Автозагрузка поверок ООО \"КБС\"");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 400);
        ImageIcon icon = new ImageIcon(Objects.requireNonNull
                (GUI.class.getResource("/Images/kbs.png")));
        setIconImage(icon.getImage());

        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Control panel
        JPanel toFgisPanel = new JPanel(new GridLayout(4, 1, 10, 5));
        JPanel toFsaPanel = new JPanel(new GridLayout(4, 1, 10, 5));

        JButton selectRawFileButton = new JButton("Загрузить журнал городов");
        selectRawFileButton.addActionListener(this::selectFileToFgis);

        JButton selectCompiledFileButton = new JButton
                ("<html><div style='text-align: center;'>Загрузить файл <br> отчетов за месяц</html>");
        selectCompiledFileButton.setBackground(Color.CYAN);
        selectCompiledFileButton.addActionListener(this::selectCompiledFile);

        JButton selectFromFgisFileButton = new JButton
                ("<html><div style='text-align: center;'>Загрузить файл <br> отчетов из Аршина</html>");
        selectFromFgisFileButton.setBackground(Color.CYAN);
        selectFromFgisFileButton.addActionListener(this::selectFileFromFgis);

        saveButton1 = new JButton("Изменить место сохранения");
        saveButton1.setEnabled(false);
        saveButton1.addActionListener(this::selectSaveLocationToFgis);

        saveButton2 = new JButton("Изменить место сохранения");
        saveButton2.setBackground(Color.CYAN);
        saveButton2.setEnabled(false);
        saveButton2.addActionListener(this::selectSaveLocationToFsa);


        toFgisButton = new JButton
                ("<html><div style='text-align: center;'>Создать файл загрузки <br> в Аршин</html>");
        toFgisButton.setEnabled(false);
        toFgisButton.addActionListener(this::toFgisAction);

        toFsaButton = new JButton
                ("<html><div style='text-align: center;'>Создать файл загрузки <br> в Росаккредитацию</html>");
        toFsaButton.setBackground(Color.CYAN);
        toFsaButton.setEnabled(false);
        toFsaButton.addActionListener(this::toFsaAction);

        toFgisPanel.add(selectRawFileButton);
        toFgisPanel.add(saveButton1);
        toFgisPanel.add(toFgisButton);

        toFsaPanel.add(selectFromFgisFileButton);
        toFsaPanel.add(selectCompiledFileButton);
        toFsaPanel.add(saveButton2);
        toFsaPanel.add(toFsaButton);



        // Progress bar
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);

        // Log area with proper styling
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.BOLD, 12));
        logArea.setBackground(new Color(240, 240, 240));
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setPreferredSize(new Dimension(850, 500));

        // Status bar
        statusLabel = new JLabel("Готов к работе");
        statusLabel.setBorder(BorderFactory.createLoweredBevelBorder());

        // Add components
        mainPanel.add(toFgisPanel, BorderLayout.WEST);
        mainPanel.add(toFsaPanel, BorderLayout.EAST);
        mainPanel.add(progressBar, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(statusLabel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void selectFileToFgis(ActionEvent e) {

        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Excel files (*.xls, *.xlsx)", "xls", "xlsx"));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedFileToFgis = fileChooser.getSelectedFile();
            saveToFgisPath = selectedFileToFgis.getParent() + "\\Запись в Аршин." + LocalDate.now()
                    .format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + "\\";
            logMessage("Выбран файл: " + selectedFileToFgis.getName());
            logMessage("Результат сохранится в: " + saveToFgisPath);
            saveButton1.setEnabled(true);
            toFgisButton.setEnabled(true);

        }
    }

    private void selectCompiledFile(ActionEvent e) {

        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Excel files (*.xls, *.xlsx)", "xls", "xlsx"));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedCompiledFile = fileChooser.getSelectedFile();
            saveToFsaPath = selectedCompiledFile.getParent() + "\\Запись в Росаккредитацию." + LocalDate.now()
                    .format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + "\\";
            logMessage("Выбран файл: " + selectedCompiledFile.getName());
            logMessage("Результат сохранится в: " + saveToFsaPath);
            saveButton2.setEnabled(true);

            if (selectedFileFromFgis != null) {
                toFsaButton.setEnabled(true);
            }

        }
    }

    private void selectFileFromFgis(ActionEvent e) {

        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Excel files (*.xls, *.xlsx)", "xls", "xlsx"));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedFileFromFgis = fileChooser.getSelectedFile();
            logMessage("Выбран файл: " + selectedFileFromFgis.getName());
            if (selectedCompiledFile != null) {

                toFsaButton.setEnabled(true);
            }

        }
    }



    private void selectSaveLocationToFgis(ActionEvent e) {
        JFileChooser dirChooser = new JFileChooser();
        dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        dirChooser.setDialogTitle("Выберите папку для сохранения результатов");


        if (dirChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            saveToFgisPath = dirChooser.getSelectedFile().toPath() + "\\Запись в Аршин." + LocalDate.now()
                    .format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + "\\";
            logMessage("Выбрана папка для сохранения: " + saveToFgisPath);

        }
    }

    private void selectSaveLocationToFsa(ActionEvent e) {
        JFileChooser dirChooser = new JFileChooser();
        dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        dirChooser.setDialogTitle("Выберите папку для сохранения результатов");


        if (dirChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            saveToFsaPath = dirChooser.getSelectedFile().toPath() + "\\Запись в Росаккредитацию." + LocalDate.now()
                    .format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + "\\";
            logMessage("Выбрана папка для сохранения: " + saveToFsaPath);

        }
    }

    private void toFgisAction(ActionEvent e) {
        if (selectedFileToFgis == null) return;
        if (saveToFgisPath.isEmpty()) return;

        toFgisButton.setEnabled(false);
        saveButton1.setEnabled(false);
        progressBar.setVisible(true);
        progressBar.setIndeterminate(true);
        logMessage("Обработка...");

        SwingWorker<Void, String> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                publish("Чтение файла...");
                String filePath = selectedFileToFgis.getAbsolutePath();
                String fileName = filePath.substring(filePath.lastIndexOf('\\') + 1);
                ExcelParser parser = new ExcelParser();
                Map<KeyMeter, IPU> waterMeterList = parser.parse(filePath);
                if (!parser.parsingResult.isEmpty()) {
                    logMessage(parser.parsingResult);
                }
                if (waterMeterList != null) {
                    publish("Проверка ошибок...");
                    Map<String, Object> regFifList = new MpiJsonParser("regFif.json").regFifList;
                    List<Equipment> eqL = new EquipmentParser().parse("equipment.json");
                    new EquipmentWriter().writingByMetrologist(waterMeterList, eqL);
                    ErrorChecking ec = new ErrorChecking(waterMeterList, regFifList);
                    logMessage(ec.errorChecking().toString());

                    if (!ec.hasError) {
                        logMessage("Прочитан файл, содержащий " + waterMeterList.size() + " счетчиков");
                        new CreatorParameters().paramCreate(waterMeterList);
                        publish("Запись файлов...");
                        XMLWriter xmlWriter = new XMLWriter();
                        xmlWriter.toArchWriter(waterMeterList, fileName, saveToFgisPath);
                        logMessage(xmlWriter.xmlResult);
                        ExcelWriter excelWriter = new ExcelWriter();
                        excelWriter.exelCreator(waterMeterList, fileName, saveToFgisPath);
                        logMessage(excelWriter.excelResult);
                    }


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
                    toFgisButton.setEnabled(true);
                    saveButton1.setEnabled(true);
                }
            }
        };

        worker.execute();
    }

    private void toFsaAction(ActionEvent e) {
        if (selectedCompiledFile == null || selectedFileFromFgis == null
    || saveToFsaPath == null) return;

        toFsaButton.setEnabled(false);
        saveButton2.setEnabled(false);
        progressBar.setVisible(true);
        progressBar.setIndeterminate(true);
        logMessage("Обработка...");

        SwingWorker<Void, String> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                publish("Чтение файла...");
                String filePath = selectedCompiledFile.getAbsolutePath();
                String fileName = filePath.substring(filePath.lastIndexOf('\\') + 1);



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
                    toFgisButton.setEnabled(true);
                    saveButton1.setEnabled(true);
                }
            }
        };

        worker.execute();
    }

    private void logMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            // logArea.setForeground(Color.black);
            logArea.append("[" + java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "] " + message + "\n");
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




