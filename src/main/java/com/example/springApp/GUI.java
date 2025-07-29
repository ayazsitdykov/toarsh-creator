package com.example.springApp;

import com.example.springApp.FSAservice.ComplExcelParser;
import com.example.springApp.FSAservice.FromFgisParser;
import com.example.springApp.FSAservice.FsaXmlWriter;
import com.example.springApp.FSAservice.MergeFiles;
import com.example.springApp.model.Equipment;
import com.example.springApp.model.IPU;
import com.example.springApp.model.KeyMeter;
import com.example.springApp.model.RegistredMeter;
import com.example.springApp.services.ExcelWriter;
import com.example.springApp.services.XMLWriter;
import com.example.springApp.FGISservice.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    private JButton selectFromFgisFileButton2;
    private JButton saveButton1;
    private JButton saveButton2;

    private JProgressBar progressBar;

    private File selectedFileToFgis;
    private File selectedFileFromFgis1;
    private File selectedFileFromFgis2;
    private File selectedCompiledFile;

    private String saveToFgisPath;
    private String saveToFsaPath;

    private final JFileChooser fileChooser = new JFileChooser();

    public GUI() {
        setTitle("Автозагрузка поверок ООО \"КБС\"");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 500);
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
        JPanel toFgisPanel = new JPanel(new GridLayout(5, 1, 10, 5));
        JPanel toFsaPanel = new JPanel(new GridLayout(5, 1, 10, 5));

        JButton selectRawFileButton = new JButton("Загрузить журнал городов");
        selectRawFileButton.addActionListener(this::selectFileToFgis);

        JButton selectCompiledFileButton = new JButton
                ("<html><div style='text-align: center;'>Файл отчетов <br> за месяц</html>");
        selectCompiledFileButton.setBackground(Color.CYAN);
        selectCompiledFileButton.addActionListener(this::selectCompiledFile);

        JButton selectFromFgisFileButton1 = new JButton
                ("<html><div style='text-align: center;'> 1 файл отчетов <br> из Аршина</html>");
        selectFromFgisFileButton1.setBackground(Color.CYAN);
        selectFromFgisFileButton1.addActionListener(this::selectFileFromFgis1);

        selectFromFgisFileButton2 = new JButton
                ("<html><div style='text-align: center;'> 2 файл отчетов <br> из Аршина(не обязательно)</html>");
        selectFromFgisFileButton2.setEnabled(false);// кнопка будет активна после выбора 1 файла
        selectFromFgisFileButton2.setBackground(Color.CYAN);
        selectFromFgisFileButton2.addActionListener(this::selectFileFromFgis2);

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
        toFsaButton.setBackground(Color.cyan);
        toFsaButton.setEnabled(false);
        toFsaButton.addActionListener(this::toFsaAction);

        toFgisPanel.add(selectRawFileButton);
        toFgisPanel.add(saveButton1);
        toFgisPanel.add(toFgisButton);

        toFsaPanel.add(selectFromFgisFileButton1);
        toFsaPanel.add(selectFromFgisFileButton2);
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

            if (selectedFileFromFgis1 != null) {
                toFsaButton.setEnabled(true);
            }

        }
    }

    private void selectFileFromFgis1(ActionEvent e) {

        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Excel files (*.xls, *.xlsx)", "xls", "xlsx"));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedFileFromFgis1 = fileChooser.getSelectedFile();
            logMessage("Выбран 1 файл: " + selectedFileFromFgis1.getName());
            selectFromFgisFileButton2.setEnabled(true); //активируем кнопку выбора 2 файла

            if (selectedCompiledFile != null) {

                toFsaButton.setEnabled(true);

            }

        }
    }

    private void selectFileFromFgis2(ActionEvent e) {

        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Excel files (*.xls, *.xlsx)", "xls", "xlsx"));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedFileFromFgis2 = fileChooser.getSelectedFile();
            logMessage("Выбран 2 файл: " + selectedFileFromFgis2.getName());

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

                    String regFifPath = new String(Files.readAllBytes(Paths.get("path.txt"))).trim()
                            .replaceAll("\"", "");

                    MpiJsonParser jsonParser = new MpiJsonParser(regFifPath);
                    logMessage(jsonParser.erMessage);

                    if (!jsonParser.hasError) {


                        Map<String, Object> regFifList = jsonParser.regFifList;

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

                }
            }
        };

        worker.execute();
    }

    private void toFsaAction(ActionEvent e) {

        progressBar.setVisible(true);
        progressBar.setIndeterminate(true);
        logMessage("Обработка...");

        SwingWorker<Void, String> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                publish("Чтение файла...");
                String filePathCF = selectedCompiledFile.getAbsolutePath(); // Путь к итоговому файлу за месяц
                String filePathFF1 = selectedFileFromFgis1.getAbsolutePath(); // Путь к 1 файлу из Аршина


                List<RegistredMeter> registredMetersCF = new ComplExcelParser().parser(filePathCF);
                // Парсинг итогового файла
                List<RegistredMeter> registredMetersFF = new FromFgisParser().parser(filePathFF1);
                // Парсинг 1 файла из Аршина(1-1000)

                if (selectedFileFromFgis2 != null) {
                    String filePathFF2 = selectedFileFromFgis2.getAbsolutePath(); // Путь к 2 файлу из Аршина
                    registredMetersFF.addAll(new FromFgisParser().parser(filePathFF2));
                    //Парсинг 2 файла из Аршина (1001-2000)
                }

                MergeFiles mergeFiles = new MergeFiles(registredMetersCF, registredMetersFF);
                mergeFiles.merge();
                //Слияние файлов в registredMetersCF
                logMessage("Отчет за месяц содержит " + registredMetersCF.size() + " счетчиков");
                logMessage("Выгрузка из Аршина содержит " + registredMetersFF.size() + " счетчиков");
                logMessage(mergeFiles.erMessage);
                FsaXmlWriter fsaXmlWriter = new FsaXmlWriter(registredMetersCF);
                if (!mergeFiles.hasError) {

                    String dateMonth = registredMetersCF.get(0).getDateVerification().getMonth().name().toLowerCase();
                    int dateYear = registredMetersCF.get(0).getDateVerification().getYear();

                    String fileNameFsa = dateMonth + "_" + dateYear + "_FSA";

                    fsaXmlWriter.create(saveToFsaPath, fileNameFsa);
                    logMessage(fsaXmlWriter.resultMessage);
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




