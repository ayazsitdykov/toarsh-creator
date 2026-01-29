package com.example.springApp.service.FGIS;

import com.example.springApp.model.IPU;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.List;

@Slf4j
public class FgisXmlWriter {

    public String xmlResult;

    public void toArchWriter(List<IPU> waterMeterList, String filePath, String savePath) {

        String fileName = filePath.substring(0, filePath.lastIndexOf('.')); //убираем расширение из названия

        File dir = new File(savePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            doc.setXmlStandalone(true);

            Element rootElement = doc.createElementNS(
                    "urn://fgis-arshin.gost.ru/module-verifications/import/2020-06-19",
                    "ns1:application");

            rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            doc.appendChild(rootElement);
            for (IPU ipu : waterMeterList) {
                Element result = doc.createElement("ns1:result");
                rootElement.appendChild(result);

                Element miInfo = doc.createElement("ns1:miInfo");
                result.appendChild(miInfo);

                Element singleMI = doc.createElement("ns1:singleMI");
                miInfo.appendChild(singleMI);

                createElement(doc, "ns1:mitypeNumber", ipu.getMitypeNumber(), singleMI);
                createElement(doc, "ns1:manufactureNum", ipu.getManufactureNum(), singleMI);
                createElement(doc, "ns1:modification", ipu.getModification(), singleMI);
                createElement(doc, "ns1:signCipher", IPU.signCipher, result);
                createElement(doc, "ns1:miOwner", ipu.getOwner(), result);
                createElement(doc, "ns1:vrfDate", ipu.getVrfDate().toString(), result);
                createElement(doc, "ns1:validDate", ipu.getValidDate().toString(), result);
                createElement(doc, "ns1:type", "2", result);
                createElement(doc, "ns1:calibration", "false", result);

                Element applicable = doc.createElement("ns1:applicable");
                result.appendChild(applicable);

                createElement(doc, "ns1:signPass", booleanToString(ipu.isSignPass()), applicable);
                createElement(doc, "ns1:signMi", booleanToString(ipu.isSignMi()), applicable);
                createElement(doc, "ns1:docTitle", ipu.getDocTitle(), result);
                createElement(doc, "ns1:metrologist", ipu.getEquipment().getAbbreviatedName(), result);

                Element means = doc.createElement("ns1:means");
                result.appendChild(means);

                Element mieta = doc.createElement("ns1:mieta");
                means.appendChild(mieta);

                createElement(doc, "ns1:number", ipu.getEquipment().getNumberUpsz(), mieta);
                ;

                Element mis = doc.createElement("ns1:mis");
                means.appendChild(mis);

                miCreator(doc, mis, ipu.getEquipment().getTypeNumIntegral(),
                        ipu.getEquipment().getManufactureNumIntegral());
                miCreator(doc, mis, ipu.getEquipment().getTypeNumIva(),
                        ipu.getEquipment().getManufactureNumIva());
                miCreator(doc, mis, ipu.getEquipment().getTypeNumTl(),
                        ipu.getEquipment().getManufactureNumTl());

                Element conditions = doc.createElement("ns1:conditions");
                result.appendChild(conditions);

                createElement(doc, "ns1:temperature", ipu.getParams().temperature(), conditions);
                createElement(doc, "ns1:pressure", ipu.getParams().pressure(), conditions);
                createElement(doc, "ns1:hymidity", ipu.getParams().humidity(), conditions);
                createElement(doc, "ns1:other", ipu.getParams().other(), conditions);

            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(savePath + fileName + ".xml"));

            transformer.transform(source, result);

            xmlResult = "Создан файл \"" + fileName + ".xml\"";

        } catch (ParserConfigurationException | TransformerException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void miCreator(Document doc, Element mis, String typeNumber, String manufactureNum) {

        Element mi = doc.createElement("ns1:mi");
        mis.appendChild(mi);

        Element typeNum = doc.createElement("ns1:typeNum");
        typeNum.appendChild(doc.createTextNode(typeNumber));
        mi.appendChild(typeNum);

        Element miManufactureNum = doc.createElement("ns1:manufactureNum");
        miManufactureNum.appendChild(doc.createTextNode(manufactureNum));
        mi.appendChild(miManufactureNum);
    }

    private String booleanToString(boolean bool) {
        return bool ? "true" : "false";
    }

    private void createElement(Document doc, String tagName, String textNode, Element parentElement) {
        Element element = doc.createElement(tagName);
        element.appendChild(doc.createTextNode(textNode));
        parentElement.appendChild(element);
    }
}