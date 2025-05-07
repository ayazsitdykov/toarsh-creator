package com.example.springApp.wmservice;

import com.example.springApp.model.Equipment;
import com.example.springApp.model.IPU;
import com.example.springApp.model.Key;

import java.util.HashMap;
import java.util.List;

public class EquipmentWriter {

    public void writingByMetrologist(HashMap<Key, IPU> waterMeterList, List<Equipment> eqL) {


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


        }
    }
}