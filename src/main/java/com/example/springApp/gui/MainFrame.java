package com.example.springApp.gui;

import com.example.springApp.model.IPU;
import com.example.springApp.parser.ExcelParser;
import com.example.springApp.service.FGIS.ErrorAndMethodicChecking;
import com.example.springApp.service.FGIS.FgisExcelWriter;
import com.example.springApp.service.FGIS.FgisXmlWriter;
import com.example.springApp.service.FGIS.MetrologyFileExtractor;
import com.example.springApp.service.FGIS.ParamCreator;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Component
public class MainFrame extends JFrame {

    @Autowired
    private MetrologyFileExtractor metrologyFileExtractor;
    @Autowired
    private ErrorAndMethodicChecking errorAndMethodicChecking;
    @Autowired
    private ParamCreator paramCreator;
    @Autowired
    private JFileChooser fileChooser;
    @Autowired
    private InitFgisUI fgisUI;
    @Autowired
    private InitFsaUI fsaUI;

    private File selectedFileToFgis;
    private File selectedFileFromFgis1;
    private File selectedFileFromFgis2;
    private File selectedCompiledFile;

    private String saveToFgisPath;
    private String saveToFsaPath;


    public MainFrame() {
        setTitle("Автозагрузка поверок ООО \"КБС\"");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 500);
        ImageIcon icon = new ImageIcon(Objects.requireNonNull
                (MainFrame.class.getResource("/Images/kbs.png")));
        setIconImage(icon.getImage());

        setLocationRelativeTo(null);
    }

    @PostConstruct
    public void init() {
        fgisUI.init();
        fsaUI.init();

        fgisUI.getSelectRawFileButton().addActionListener(this::selectFileToFgis);
        fgisUI.getSaveButtonFgis().addActionListener(this::selectSaveLocationToFgis);
        fgisUI.getToFgisButton().addActionListener(this::toFgisAction);
        JPanel mainPanel = fgisUI.getMainPanel();

        fsaUI.getSelectCompiledFileButton().addActionListener(this::selectCompiledFile);
        fsaUI.getSelectFromFgisFileButton1().addActionListener(this::selectFileFromFgis1);
        fsaUI.getSelectFromFgisFileButton2().addActionListener(this::selectFileFromFgis2);
        fsaUI.getSaveButtonFsa().addActionListener(this::selectSaveLocationToFsa);
        fsaUI.getToFsaButton().addActionListener(this::toFsaAction);
        mainPanel.add(fsaUI.getToFsaPanel(), BorderLayout.EAST);

        add(fgisUI.getMainPanel());
    }

    private void selectFileToFgis(ActionEvent e) {

        selectedFileToFgis = getSelectedFile();
        saveToFgisPath = getSavePath(selectedFileToFgis, "Запись в Аршин");

        fgisUI.getSaveButtonFgis().setEnabled(true);
        fgisUI.getToFgisButton().setEnabled(true);
    }

    private void selectCompiledFile(ActionEvent e) {

        selectedCompiledFile = getSelectedFile();
        saveToFsaPath = getSavePath(selectedCompiledFile, "Запись в Росаккредитацию");

        fsaUI.getSaveButtonFsa().setEnabled(true);

        if (selectedFileFromFgis1 != null) {
            fsaUI.getToFsaButton().setEnabled(true);
        }
    }

    private File getSelectedFile() {
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Excel files (*.xls, *.xlsx)", "xls", "xlsx"));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    private String getSavePath(File selectedFile, String actionName) {
        if (selectedFile == null) {
            return "";
        }
        String savePath = selectedFile.getParent() + "\\" + actionName + "\\" + getFormattedCurrentTime();
        logMessage("Результат сохранится в: " + savePath);
        return savePath;
    }

    private void selectFileFromFgis1(ActionEvent e) {

        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Excel files (*.xls, *.xlsx)", "xls", "xlsx"));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedFileFromFgis1 = fileChooser.getSelectedFile();
            logMessage("Выбран 1 файл: " + selectedFileFromFgis1.getName());
            fsaUI.getSelectFromFgisFileButton2().setEnabled(true); //активируем кнопку выбора 2 файла

            if (selectedCompiledFile != null) {
                fsaUI.getToFsaButton().setEnabled(true);
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
        saveToFgisPath = selectSaveLocation("Запись в Аршин");
    }

    private void selectSaveLocationToFsa(ActionEvent e) {
        saveToFsaPath = selectSaveLocation("Запись в Росаккредитацию");
    }

    private String selectSaveLocation(String actionName) {
        String saveLocation = "";
        JFileChooser dirChooser = new JFileChooser();
        dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        dirChooser.setDialogTitle("Выберите папку для сохранения результатов");

        if (dirChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            saveLocation = dirChooser.getSelectedFile().toPath() + "\\" + actionName
                    + getFormattedCurrentTime() + "\\";
            logMessage("Выбрана папка для сохранения: " + saveLocation);
        }
        return saveLocation;
    }

    private void toFgisAction(ActionEvent e) {
        if (selectedFileToFgis == null) {
            logMessage("Файл не выбран");
            return;
        }
        if (saveToFgisPath.isEmpty()) {
            logMessage("Путь для сохранения не выбран");
            return;
        }

        fgisUI.getProgressBar().setVisible(true);
        fgisUI.getProgressBar().setIndeterminate(true);
        logMessage("Обработка...");

        SwingWorker<Void, String> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                publish("Чтение файла...");
                String filePath = selectedFileToFgis.getAbsolutePath();
                String fileName = filePath.substring(filePath.lastIndexOf('\\') + 1);

                List<IPU> waterMeterList = metrologyFileExtractor.transfer(new ExcelParser().parse(filePath).get(0));

                if (waterMeterList != null) {
                    publish("Проверка ошибок...");

                    String errorCheck = errorAndMethodicChecking.check(waterMeterList).toString();

                    logMessage(errorCheck);
                    paramCreator.create(waterMeterList);

                    logMessage("Прочитан файл, содержащий " + waterMeterList.size() + " счетчиков");

                    publish("Запись файлов...");
                    FgisXmlWriter xmlWriter = new FgisXmlWriter();
                    xmlWriter.toArchWriter(waterMeterList, fileName, saveToFgisPath);
                    logMessage(xmlWriter.xmlResult);
                    FgisExcelWriter excelWriter = new FgisExcelWriter();
                    excelWriter.exelCreator(waterMeterList, fileName, saveToFgisPath);
                    logMessage(excelWriter.excelResult);

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
                    fgisUI.getStatusLabel().setText("Processing completed");
                } catch (InterruptedException | ExecutionException ex) {
                    logMessage("ERROR: " + ex.getMessage());
                    fgisUI.getStatusLabel().setText("Ошибка");
                } finally {
                    fgisUI.getProgressBar().setVisible(false);
                }
            }
        };

        worker.execute();
    }

    private void toFsaAction(ActionEvent e) {

        fgisUI.getProgressBar().setVisible(true);
        fgisUI.getProgressBar().setIndeterminate(true);
        logMessage("Обработка...");

        SwingWorker<Void, String> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                publish("Чтение файла...");
                String filePathCF = selectedCompiledFile.getAbsolutePath(); // Путь к итоговому файлу за месяц
                String filePathFF1 = selectedFileFromFgis1.getAbsolutePath(); // Путь к 1 файлу из Аршина


                //  List<RegistredMeter> registredMetersCF = new CompletedFileExtractor().transfer(filePathCF);
                // Парсинг итогового файла
                //  List<RegistredMeter> registredMetersFF = new FgisFileExtractor().parser(filePathFF1);
                // Парсинг 1 файла из Аршина(1-1000)

                if (selectedFileFromFgis2 != null) {
                    String filePathFF2 = selectedFileFromFgis2.getAbsolutePath(); // Путь к 2 файлу из Аршина
                    // registredMetersFF.addAll(new FgisFileExtractor().parser(filePathFF2));
                    //Парсинг 2 файла из Аршина (1001-2000)
                }

//                MergeFiles mergeFiles = new MergeFiles(registredMetersCF, registredMetersFF);
//                mergeFiles.merge();
//                //Слияние файлов в registredMetersCF
//                logMessage("Отчет за месяц содержит " + registredMetersCF.size() + " счетчиков");
//                logMessage("Выгрузка из Аршина содержит " + registredMetersFF.size() + " счетчиков");
//               // logMessage(mergeFiles.erMessage);
//                FsaXmlWriter fsaXmlWriter = new FsaXmlWriter(registredMetersCF);
////                if (!mergeFiles.hasError) {
//
//                    String dateMonth = registredMetersCF.get(0).getDateVerification().getMonth().name().toLowerCase();
//                    int dateYear = registredMetersCF.get(0).getDateVerification().getYear();
//
//                    String fileNameFsa = dateMonth + "_" + dateYear + "_FSA";
//
//                    fsaXmlWriter.create(saveToFsaPath, fileNameFsa);
//                    logMessage(fsaXmlWriter.resultMessage);
//                }


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
                    fgisUI.getStatusLabel().setText("Processing completed");
                } catch (InterruptedException | ExecutionException ex) {
                    logMessage("ERROR: " + ex.getMessage());
                    fgisUI.getStatusLabel().setText("Ошибка");
                } finally {
                    fgisUI.getProgressBar().setVisible(false);

                }
            }
        };

        worker.execute();
    }

    private void logMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            // logArea.setForeground(Color.black);
            fgisUI.getLogArea().append("[" + java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "] " + message + "\n");
            // Auto-scroll to bottom
            fgisUI.getLogArea().setCaretPosition(fgisUI.getLogArea().getDocument().getLength());
        });
    }

    private String getFormattedCurrentTime() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + "\\";
    }

}