package com.example.springApp.FSAservice;

import com.example.springApp.model.RegistredMeter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FsaXmlWriter {
    List<RegistredMeter> registredMeters;

    public FsaXmlWriter(List<RegistredMeter> registredMeters) {
        this.registredMeters = registredMeters;
    }

    public void create(String filePath, String fileName) {

        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            // Создаем документ XML
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            doc.setXmlStandalone(false);// <-- Вот эта строка добавляет standalone="no"


            // Создаем корневой элемент
            Element messageElement = doc.createElement("Message");
            messageElement.setAttribute("xsi:noNamespaceSchemaLocation", "schema.xsd");
            messageElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            doc.appendChild(messageElement);

            // Создаем элемент VerificationMeasuringInstrumentData
            Element dataElement = doc.createElement("VerificationMeasuringInstrumentData");
            messageElement.appendChild(dataElement);



            for (int i = 0; i < registredMeters.size(); i++) {
                RegistredMeter meter = registredMeters.get(i);
                dataElement.appendChild(createInstrumentElement(doc, meter));
            }

            // Добавляем элемент SaveMethod
            Element saveMethodElement = doc.createElement("SaveMethod");
            saveMethodElement.appendChild(doc.createTextNode("2"));
            messageElement.appendChild(saveMethodElement);

            // Записываем XML в файл
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filePath + fileName + ".xml"));

            transformer.transform(source, result);

            System.out.println("XML файл успешно создан!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Element createInstrumentElement(Document doc, RegistredMeter meter) {
        Element instrumentElement = doc.createElement("VerificationMeasuringInstrument");

        // NumberVerification (10-значный номер)
        String number = meter.getNumberVerification();
        instrumentElement.appendChild(createElementWithText(doc, "NumberVerification", number));

        // DateVerification (дата в пределах последнего года)
        LocalDate verificationDate = meter.getDateVerification();
        instrumentElement.appendChild(createElementWithText(doc, "DateVerification",
                verificationDate.format(DateTimeFormatter.ISO_DATE)));

        // DateEndVerification (дата через 1-6 лет от DateVerification)
        LocalDate endDate = meter.getDateEndVerification();
        instrumentElement.appendChild(createElementWithText(doc, "DateEndVerification",
                endDate.format(DateTimeFormatter.ISO_DATE)));

        // TypeMeasuringInstrument (случайный тип из списка)
        String instrumentType = meter.getTypeMeasuringInstrument();
        instrumentElement.appendChild(createElementWithText(doc, "TypeMeasuringInstrument", instrumentType));

        // ApprovedEmployee
        Element employeeElement = doc.createElement("ApprovedEmployee");
        instrumentElement.appendChild(employeeElement);

        // Name
        Element nameElement = doc.createElement("Name");
        employeeElement.appendChild(nameElement);

        nameElement.appendChild(createElementWithText(doc, "Last",
                meter.getLast()));
        nameElement.appendChild(createElementWithText(doc, "First",
                meter.getFirst()));
        nameElement.appendChild(createElementWithText(doc, "Middle",
                meter.getMiddle()));

        // SNILS (11 цифр)
        String snils = meter.getSnils();
        employeeElement.appendChild(createElementWithText(doc, "SNILS", snils));

        // ResultVerification (всегда 1)
        instrumentElement.appendChild(createElementWithText(doc,"ResultVerification", String.valueOf(meter.isResultVerification())));

        return instrumentElement;
    }

    private static Element createElementWithText(Document doc, String name, String text) {
        Element element = doc.createElement(name);
        element.appendChild(doc.createTextNode(text));
        return element;
    }
}
