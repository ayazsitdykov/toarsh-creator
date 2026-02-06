package com.example.springApp.service.FSA;

import com.example.springApp.model.RegistredMeter;
import lombok.extern.slf4j.Slf4j;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class FsaXmlWriter {

    private static final int MAX_ELEMENTS_PER_FILE = 1000;
    public String resultMessage;

    public void create(List<RegistredMeter> registeredMeters,
                       String filePath, String fileName) {

        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        for (int j = 0; j < registeredMeters.size(); j += MAX_ELEMENTS_PER_FILE) {
            int end = Math.min(j + MAX_ELEMENTS_PER_FILE, registeredMeters.size());
            List<RegistredMeter> registredMeterList = new ArrayList<>(registeredMeters.subList(j, end));

            try {

                Namespace xsiNamespace = Namespace.getNamespace("xsi",
                        "http://www.w3.org/2001/XMLSchema-instance");
                Element messageElement = new Element("Message");
                messageElement.addNamespaceDeclaration(xsiNamespace);
                messageElement.setAttribute("noNamespaceSchemaLocation", "schema.xsd", xsiNamespace);
                Element instrumentData = new Element("VerificationMeasuringInstrumentData");

                for (RegistredMeter meter : registredMeterList) {
                    instrumentData.addContent(createInstrumentElement(meter));
                }
                messageElement.addContent(instrumentData);

                Element saveMethod = new Element("SaveMethod");
                saveMethod.setText("2");
                messageElement.addContent(saveMethod);

                Document doc = new Document(messageElement);

                XMLOutputter xmlOutput = new XMLOutputter();
                xmlOutput.setFormat(Format.getPrettyFormat());

                try (FileWriter writer = new FileWriter(filePath + fileName + "_"
                        + (j + 1) + "-" + (end) + ".xml")) {
                    xmlOutput.output(doc, writer);
                    resultMessage = "XML файл " + fileName + " успешно создан!";
                }
            } catch (IOException e) {
                log.error("Ошибка при создании XML файла {}", fileName);
                throw new RuntimeException(e);
            }
        }
    }

    private Element createInstrumentElement(RegistredMeter meter) {
        Element instrument = new Element("VerificationMeasuringInstrument");

        Element number = new Element("NumberVerification");
        number.setText(meter.getNumberVerification());
        instrument.addContent(number);

        createDate("DateVerification", meter.getDateVerification(), instrument);
        createDate("DateEndVerification", meter.getDateEndVerification(), instrument);

        Element type = new Element("TypeMeasuringInstrument");
        String instrumentType = meter.getTypeMeasuringInstrument();
        type.setText(instrumentType);
        instrument.addContent(type);

        Element employee = getElement(meter);
        instrument.addContent(employee);

        Element result = new Element("ResultVerification");
        result.setText(String.valueOf(meter.getResultVerification()));
        instrument.addContent(result);

        return instrument;
    }

    private static @NotNull Element getElement(RegistredMeter meter) {
        Element employee = new Element("ApprovedEmployees");

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

        Element snils = new Element("SNILS");
        snils.setText(meter.getSnils());
        employee.addContent(snils);
        return employee;
    }

    private void createDate(String elementName, LocalDate date, Element parentElement) {
        Element element = new Element(elementName);
        element.setText(date.format(DateTimeFormatter.ISO_DATE));
        parentElement.addContent(element);
    }
}