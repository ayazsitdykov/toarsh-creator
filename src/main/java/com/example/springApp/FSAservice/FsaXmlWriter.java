package com.example.springApp.FSAservice;

import com.example.springApp.model.RegistredMeter;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FsaXmlWriter {
    private List<RegistredMeter> registredMeters;
    public String resultMessage;

    public FsaXmlWriter(List<RegistredMeter> registredMeters) {
        this.registredMeters = registredMeters;
    }

    public void create(String filePath, String fileName) {




        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }


        for (int j = 0; j < registredMeters.size(); j += 1000) { // Создаем несколько файлов размером до 1000 счетчиков
            int end = Math.min(j + 1000, registredMeters.size());
            List<RegistredMeter> registredMeterList = new ArrayList<>(registredMeters.subList(j, end));

            try {

                // Создаем корневой элемент

                Namespace xsiNamespace = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
                Element messageElement = new Element("Message");
                messageElement.addNamespaceDeclaration(xsiNamespace);

                messageElement.setAttribute("noNamespaceSchemaLocation", "schema.xsd", xsiNamespace);


                Element instrumentData = new Element("VerificationMeasuringInstrumentData");


                for (int i = 0; i < registredMeterList.size(); i++) {
                    RegistredMeter meter = registredMeterList.get(i);
                    instrumentData.addContent(createInstrumentElement(meter));
                }

                // Добавляем все в корневой элемент
                messageElement.addContent(instrumentData);

                // Добавляем SaveMethod
                Element saveMethod = new Element("SaveMethod");
                saveMethod.setText("2");
                messageElement.addContent(saveMethod);

                // Создаем документ
                Document doc = new Document(messageElement);

                // Настраиваем формат вывода
                XMLOutputter xmlOutput = new XMLOutputter();
                xmlOutput.setFormat(Format.getPrettyFormat());

                try (FileWriter writer = new FileWriter(filePath + fileName + "_" + (j+1) + "-" + (end) + ".xml")) {
                    xmlOutput.output(doc, writer);
                    resultMessage = "XML файл " + fileName + " успешно создан!";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private Element createInstrumentElement(RegistredMeter meter) {
        Element instrument = new Element("VerificationMeasuringInstrument");

        Element number = new Element("NumberVerification");
        number.setText(meter.getNumberVerification());
        instrument.addContent(number);

        LocalDate verificationDate = meter.getDateVerification();
        Element dateVerification = new Element("DateVerification");
        dateVerification.setText(verificationDate.format(DateTimeFormatter.ISO_DATE));
        instrument.addContent(dateVerification);

        LocalDate endDate = meter.getDateEndVerification();
        Element dateEndVerification = new Element("DateEndVerification");
        dateEndVerification.setText(endDate.format(DateTimeFormatter.ISO_DATE));
        instrument.addContent(dateEndVerification);

        // Тип счетчика
        Element type = new Element("TypeMeasuringInstrument");
        String instrumentType = meter.getTypeMeasuringInstrument();
        type.setText(instrumentType);
        instrument.addContent(type);

        // Ответственный сотрудник
        Element employee = new Element("ApprovedEmployee");

        Element name = new Element("Name");
        Element last = new Element("Last");
        last.setText(meter.getLast());
        Element first = new Element("First");
        first.setText(meter.getFirst());
        Element middle = new Element("Middle");
        middle.setText(meter.getMiddle());

        name.addContent(last);
        name.addContent(first);
        name.addContent(middle);
        employee.addContent(name);

        // СНИЛС
        Element snils = new Element("SNILS");
        snils.setText(meter.getSnils());
        employee.addContent(snils);
        instrument.addContent(employee);

        // Результат проверки
        Element result = new Element("ResultVerification");
        result.setText(String.valueOf(meter.getResultVerification()));
        instrument.addContent(result);

        return instrument;


    }


}
