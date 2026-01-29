package com.example.springApp;

import com.example.springApp.model.MeterDescription;
import com.example.springApp.model.MpModel;
import com.example.springApp.parser.JsonParser;
import com.example.springApp.parser.TxtParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        JsonParser<MeterDescription> rFParser = new JsonParser<>(MeterDescription.class, new ObjectMapper());
        List<MeterDescription> meters = rFParser.parse("regFif.json");
        List<Integer> mpi = new ArrayList<>();
        for (MeterDescription meter : meters) {
            if (!meter.isMpiObject()) {
                mpi.add(meter.getMpiAsInteger());
            } else {
                Object o = meter.getMpiAsMap().get("ХВС");
                if (o instanceof Integer) {
                    mpi.add((int) o);
                } else if (o != null) {
                    List<Integer> integers = (ArrayList<Integer>) o;
                    mpi.addAll(integers);
                }
            }
        }
        System.out.println(mpi.size());

        JsonParser<MpModel> mParser = new JsonParser<>(MpModel.class, new ObjectMapper());
        List<MpModel> mp = mParser.parse("mpOther.json");
        mp.forEach(System.out::println);

        TxtParser tParser = new TxtParser();
        System.out.println(tParser.parseToList("forbidden.txt"));

    }
}

