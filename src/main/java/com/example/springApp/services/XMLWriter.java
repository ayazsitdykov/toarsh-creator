package com.example.springApp.services;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.example.springApp.model.IPU;
import com.example.springApp.model.Key;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.util.HashMap;
import java.util.Random;

public class XMLWriter {

    public void toArchWriter(HashMap<Key, IPU> waterMeterList, String fileName, String savePath) {

        File dir = new File(savePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }


        try {
            // Создаем фабрику и построитель документов
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // Создаем новый документ
            Document doc = docBuilder.newDocument();
            doc.setXmlStandalone(true);// <-- Вот эта строка добавляет standalone="yes"

            // Создаем корневой элемент
            Element rootElement = doc.createElementNS(
                    "urn://fgis-arshin.gost.ru/module-verifications/import/2020-06-19", "ns1:application");
//            rootElement.setAttributeNS(
//                    "http://www.w3.org/2001/XMLSchema-instance", "xsi:schemaLocation",
//                    "urn://fgis-arshin.gost.ru/module-verifications/import/2020-06-19");
            rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            doc.appendChild(rootElement);
            for (IPU ipu : waterMeterList.values()) {
                // Создаем элемент result
                Element result = doc.createElement("ns1:result");
                rootElement.appendChild(result);

                // Добавляем miInfo
                Element miInfo = doc.createElement("ns1:miInfo");
                result.appendChild(miInfo);

                Element singleMI = doc.createElement("ns1:singleMI");
                miInfo.appendChild(singleMI);

                Element mitypeNumber = doc.createElement("ns1:mitypeNumber");
                mitypeNumber.appendChild(doc.createTextNode(ipu.getMitypeNumber()));
                singleMI.appendChild(mitypeNumber);

                Element manufactureNum = doc.createElement("ns1:manufactureNum");
                manufactureNum.appendChild(doc.createTextNode(ipu.getManufactureNum()));
                singleMI.appendChild(manufactureNum);

                Element modification = doc.createElement("ns1:modification");
                modification.appendChild(doc.createTextNode(ipu.getModification()));
                singleMI.appendChild(modification);

                // Добавляем signCipher
                Element signCipher = doc.createElement("ns1:signCipher");
                signCipher.appendChild(doc.createTextNode(ipu.getSignCipher()));
                result.appendChild(signCipher);

                // Добавляем miOwner
                Element miOwner = doc.createElement("ns1:miOwner");
                miOwner.appendChild(doc.createTextNode(ipu.getOwner()));
                result.appendChild(miOwner);

                // Добавляем даты
                Element vrfDate = doc.createElement("ns1:vrfDate");
                vrfDate.appendChild(doc.createTextNode(ipu.getVrfDate().toString()));
                result.appendChild(vrfDate);

                Element validDate = doc.createElement("ns1:validDate");
                validDate.appendChild(doc.createTextNode(ipu.getValidDate().toString()));
                result.appendChild(validDate);

                // Добавляем type
                Element type = doc.createElement("ns1:type");
                type.appendChild(doc.createTextNode("2"));
                result.appendChild(type);

                // Добавляем calibration
                Element calibration = doc.createElement("ns1:calibration");
                calibration.appendChild(doc.createTextNode("false"));
                result.appendChild(calibration);

                // Добавляем applicable
                Element applicable = doc.createElement("ns1:applicable");
                result.appendChild(applicable);

                Element signPass = doc.createElement("ns1:signPass");
                signPass.appendChild(doc.createTextNode(String.valueOf(false)));
                applicable.appendChild(signPass);

                Element signMi = doc.createElement("ns1:signMi");
                signMi.appendChild(doc.createTextNode(String.valueOf(true)));
                applicable.appendChild(signMi);

                // Добавляем docTitle
                Element docTitle = doc.createElement("ns1:docTitle");
                docTitle.appendChild(doc.createTextNode(ipu.getDocTitle()));
                result.appendChild(docTitle);

                // Добавляем metrologist
                Element metrologist = doc.createElement("ns1:metrologist");
                metrologist.appendChild(doc.createTextNode(ipu.getMetrologist()));
                result.appendChild(metrologist);

                // Добавляем means
                Element means = doc.createElement("ns1:means");
                result.appendChild(means);

                Element mieta = doc.createElement("ns1:mieta");
                means.appendChild(mieta);

                Element number = doc.createElement("ns1:number");
                number.appendChild(doc.createTextNode(ipu.getNumberUpsz()));
                mieta.appendChild(number);

                Element mis = doc.createElement("ns1:mis");
                means.appendChild(mis);


                //Добавляем mi секундомер
                miCreator(doc, mis, ipu.getTypeNumIntegral(), ipu.getManufactureNumIntegral());
                // Добавляем mi термогигрометр
                miCreator(doc, mis, ipu.getTypeNumIva(), ipu.getManufactureNumIva());
                // Добавляем mi термометр
                miCreator(doc, mis, ipu.getTypeNumTl(), ipu.getManufactureNumTl());

                // Добавляем conditions
                Element conditions = doc.createElement("ns1:conditions");
                result.appendChild(conditions);

                Element temperature = doc.createElement("ns1:temperature");
                temperature.appendChild(doc.createTextNode(ipu.getTemperature()));
                conditions.appendChild(temperature);

                Element pressure = doc.createElement("ns1:pressure");
                pressure.appendChild(doc.createTextNode(ipu.getPressure()));
                conditions.appendChild(pressure);

                Element hymidity = doc.createElement("ns1:hymidity");
                hymidity.appendChild(doc.createTextNode(ipu.getHumidity()));
                conditions.appendChild(hymidity);

                Element other = doc.createElement("ns1:other");
                other.appendChild(doc.createTextNode(ipu.getOther()));
                conditions.appendChild(other);
            }

            // Записываем содержимое в файл
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.STANDALONE, "yes"); // явно указываем standalone
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");


            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(savePath + fileName + ".xml"));

            transformer.transform(source, result);

            System.out.println("XML-файл успешно создан!");
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }


    }

    private void miCreator(Document doc, Element mis, String typeNumber, String manufactureNum) {
        //метод добавления вспомогательных СИ
        Element mi = doc.createElement("ns1:mi");
        mis.appendChild(mi);

        Element typeNum = doc.createElement("ns1:typeNum");
        typeNum.appendChild(doc.createTextNode(typeNumber));
        mi.appendChild(typeNum);

        Element miManufactureNum = doc.createElement("ns1:manufactureNum");
        miManufactureNum.appendChild(doc.createTextNode(manufactureNum));
        mi.appendChild(miManufactureNum);
    }


}
