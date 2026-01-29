package com.example.springApp.service.FGIS;

import com.example.springApp.model.Equipment;
import com.example.springApp.model.IPU;
import com.example.springApp.model.KeyMeter;

import java.util.List;
import java.util.Map;

public class EquipmentWriter {

    public void writingByMetrologist(Map<KeyMeter, IPU> waterMeterList, List<Equipment> eqL) throws Exception {


        for (IPU ipu : waterMeterList.values()) {
            for (Equipment eq : eqL) {
                if (ipu.getMetrologist().equals(eq.getMetrologist())) {
                    ipu.setNumberUpsz(eq.getNumberUpsz());
                    ipu.setTypeNumIntegral(eq.getTypeNumIntegral());
                    ipu.setManufactureNumIntegral(eq.getManufactureNumIntegral());
                    ipu.setTypeNumIva(eq.getTypeNumIva());
                    ipu.setManufactureNumIva(eq.getManufactureNumIva());
                    ipu.setTypeNumTl(eq.getTypeNumTl());
                    ipu.setManufactureNumTl(eq.getManufactureNumTl());
                    break;

                }
            }

            if (ipu.getNumberUpsz() == null) { // Проверяем, записались ли данные по прибору
                System.out.println();
                throw new Exception("Неправильно указан поверитель в строчке с номером счетчика " + ipu.getManufactureNum());
            }


        }
    }
}