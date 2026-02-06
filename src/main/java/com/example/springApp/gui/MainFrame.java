package com.example.springApp.gui;

import com.example.springApp.model.IPU;
import com.example.springApp.model.RegistredMeter;
import com.example.springApp.parser.ExcelParser;
import com.example.springApp.service.FGIS.ErrorAndMethodicChecking;
import com.example.springApp.service.FGIS.FgisExcelWriter;
import com.example.springApp.service.FGIS.FgisXmlWriter;
import com.example.springApp.service.FGIS.MetrologyFileExtractor;
import com.example.springApp.service.FGIS.ParamCreator;
import com.example.springApp.service.FSA.CompletedFileExtractor;
import com.example.springApp.service.FSA.FgisFileExtractor;
import com.example.springApp.service.FSA.FsaXmlWriter;
import com.example.springApp.service.FSA.MergeFiles;
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
    private ExcelParser excelParser;
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
    @Autowired
    private FgisXmlWriter fgisXmlWriter;
    @Autowired
    private FgisExcelWriter fgisExcelWriter;
    @Autowired
    private CompletedFileExtractor completedFileExtractor;
    @Autowired
    private FgisFileExtractor fgisFileExtractor;
    @Autowired
    private FsaXmlWriter fsaXmlWriter;
    @Autowired
    private MergeFiles mergeFiles;

    private List<RegistredMeter> registredMetersCF;
    private List<RegistredMeter> registredMetersFF;

    private File selectedFileToFgis;
    private File selectedFileFromFgis1;
    private File addFileFromFgis;
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
        fsaUI.getAddFromFgisFileButton().addActionListener(this::addFileFromFgis);
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

        registredMetersCF = completedFileExtractor
                .transfer(excelParser.parse(selectedCompiledFile.getAbsolutePath()).get(0));

    }

    private File getSelectedFile() {
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Excel files (*.xls, *.xlsx)", "xls", "xlsx"));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            logMessage("Выбран файл: " + selectedFile.getName());
            return selectedFile;
        }
        return null;
    }

    private String getSavePath(File selectedFile, String actionName) {
        if (selectedFile == null) {
            return "";
        }
        String savePath = selectedFile.getParent() + "\\" + actionName + "." + getFormattedCurrentTime();
        logMessage("Результат сохранится в: " + savePath);
        return savePath;
    }

    private void selectFileFromFgis1(ActionEvent e) {

        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Excel files (*.xls, *.xlsx)", "xls", "xlsx"));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedFileFromFgis1 = fileChooser.getSelectedFile();
            registredMetersFF = fgisFileExtractor
                    .transfer(excelParser.parse(selectedFileFromFgis1.getAbsolutePath()).get(0));
            logMessage("Выбран 1 файл: " + selectedFileFromFgis1.getName());
            fsaUI.getAddFromFgisFileButton().setEnabled(true); //активируем кнопку выбора 2 файла

            if (selectedCompiledFile != null) {
                fsaUI.getToFsaButton().setEnabled(true);
            }
        }
    }

    private void addFileFromFgis(ActionEvent e) {

        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Excel files (*.xls, *.xlsx)", "xls", "xlsx"));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            addFileFromFgis = fileChooser.getSelectedFile();

            registredMetersFF.addAll(fgisFileExtractor
                    .transfer(excelParser.parse(addFileFromFgis.getAbsolutePath()).get(0)));
            logMessage("Добавлен файл: " + addFileFromFgis.getName());
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
        logMessage("\n" + "Обработка...");

        SwingWorker<Void, String> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                publish("Чтение файла...");
                String filePath = selectedFileToFgis.getAbsolutePath();
                String fileName = filePath.substring(filePath.lastIndexOf('\\') + 1);

                List<IPU> waterMeterList = metrologyFileExtractor.transfer(excelParser.parse(filePath).get(0));

                if (waterMeterList == null) {
                    logMessage("Файл пуст");
                }
                if (waterMeterList != null) {
                    publish("Проверка ошибок...");

                    String errorCheck = errorAndMethodicChecking.check(waterMeterList).toString();
                    logMessage(errorCheck);
                    Thread.sleep(200);

                    paramCreator.create(waterMeterList);

                    logMessage("Прочитан файл, содержащий " + waterMeterList.size() + " счетчиков");

                    publish("Запись файлов...");
                    fgisXmlWriter.write(waterMeterList, fileName, saveToFgisPath);
                    logMessage(fgisXmlWriter.xmlResult);
                    fgisExcelWriter.exelCreator(waterMeterList, fileName, saveToFgisPath);
                    logMessage(fgisExcelWriter.excelResult);

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
            protected Void doInBackground() {
                publish("Чтение файла...");

                mergeFiles.merge(registredMetersCF, registredMetersFF);

                logMessage("Отчет за месяц содержит " + registredMetersCF.size() + " счетчиков");
                logMessage("Выгрузка из Аршина содержит " + registredMetersFF.size() + " счетчиков");

                String dateMonth = registredMetersCF.get(0).getDateVerification().getMonth().name().toLowerCase();
                int dateYear = registredMetersCF.get(0).getDateVerification().getYear();

                String fileNameFsa = dateMonth + "_" + dateYear + "_FSA";

                fsaXmlWriter.create(registredMetersCF, saveToFsaPath, fileNameFsa);
                logMessage(fsaXmlWriter.resultMessage);


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
            fgisUI.getLogArea().append("[" + java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                    + "] " + message + "\n");
            // Auto-scroll to bottom
            fgisUI.getLogArea().setCaretPosition(fgisUI.getLogArea().getDocument().getLength());
        });
    }

    private String getFormattedCurrentTime() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + "\\";
    }
}