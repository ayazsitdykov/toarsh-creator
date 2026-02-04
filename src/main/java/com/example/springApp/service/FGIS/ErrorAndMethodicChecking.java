package com.example.springApp.service.FGIS;

import com.example.springApp.model.Equipment;
import com.example.springApp.model.IPU;
import com.example.springApp.model.MeterDescription;
import com.example.springApp.model.MpModel;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class ErrorAndMethodicChecking {

    private final List<MeterDescription> regFifList;
    private final List<String> stopList;
    private final List<MpModel> otherMethodicList;

    private StringBuilder logOut;
    private boolean hasError;

    @PostConstruct
    public void init() {
        this.logOut = new StringBuilder("\n");
        this.hasError = false;
    }

    public StringBuilder check(List<IPU> waterMeterList) {

        waterMeterList.forEach((ipu) -> {
            String manufactureNumber = ipu.getManufactureNum();
            String regNumber = ipu.getMitypeNumber();
            LocalDate vrfDate = ipu.getVrfDate();
            LocalDate validDate = ipu.getValidDate();
            LocalDate nowDate = LocalDate.now();
            ArrayList<Integer> mpi = new ArrayList<>();
            Equipment equipment = ipu.getEquipment();
            MeterDescription meterDescription = regFifList.stream()
                    .filter((meter) -> meter.regNumber().equals(regNumber))
                    .findFirst().orElse(null);

            if (equipment == null) {
                printFaultMessage(manufactureNumber, "Поверитель не найден");
            }

            if (meterDescription == null) {
                printFaultMessage(manufactureNumber,
                        String.format("Рег. номер %s не найден в базе", regNumber));
                return;
            }

            if (stopList.contains(regNumber)) {
                printFaultMessage(manufactureNumber, "не входит в область аккредитации");
                return;
            }

            if (!meterDescription.types().contains(ipu.getModification())) {
                printFaultMessage(manufactureNumber,
                        String.format("Неправильный тип счетчика. Возможные типы: %s \n",
                                meterDescription.types()));
            }

            Object mpiValues = meterDescription.mpi();

            if (mpiValues instanceof Map) {

                if (((Map<?, ?>) mpiValues).values() instanceof List) {
                    if (ipu.isHot()) {
                        @SuppressWarnings("unchecked")
                        Optional<List<Integer>> gvsMpiList = (Optional<List<Integer>>) ((Map<?, ?>) mpiValues)
                                .get("ГВС");
                        checkUsingResource(true, gvsMpiList.isEmpty(), manufactureNumber);

                        mpi.addAll(gvsMpiList.orElseGet((ArrayList::new)));
                    } else {
                        @SuppressWarnings("unchecked")
                        Optional<List<Integer>> hvsMpiList = (Optional<List<Integer>>) ((Map<?, ?>) mpiValues)
                                .get("ХВС");
                        checkUsingResource(false, hvsMpiList.isEmpty(), manufactureNumber);

                        mpi.addAll(hvsMpiList.orElseGet((ArrayList::new)));
                    }

                } else {
                    if (ipu.isHot()) {
                        @SuppressWarnings("unchecked")
                        Optional<Integer> gvsMpi = ((Map<String, Integer>) mpiValues)
                                .get("ГВС").describeConstable();
                        checkUsingResource(true, gvsMpi.isEmpty(), manufactureNumber);

                        mpi.add(gvsMpi.orElseGet(() -> 0));
                    } else {
                        @SuppressWarnings("unchecked")
                        Optional<Integer> hvsMpi = ((Map<String, Integer>) mpiValues)
                                .get("ХВС").describeConstable();
                        checkUsingResource(false, hvsMpi.isEmpty(), manufactureNumber);

                        mpi.add(hvsMpi.orElseGet(() -> 0));
                    }
                }
            }

            if (mpiValues instanceof Integer) {

                Integer mpiValue = (Integer) meterDescription.mpi();
                mpi.add(mpiValue);
            }

            if (mpi.isEmpty()) {
                printFaultMessage(ipu.getManufactureNum(),
                        String.format("Рег номер - %s - не найден в базе", ipu.getMitypeNumber()));
            }

            if (vrfDate.equals(LocalDate.EPOCH) ||
                    validDate.equals(LocalDate.EPOCH)
                    || nowDate.isBefore(vrfDate)
                    || nowDate.minusYears(1).isAfter(vrfDate)) {
                printFaultMessage(manufactureNumber, "Проверьте даты");

                return;
            }

            boolean isDateCorrect = false;
            for (int year : mpi) {
                if (ipu.getVrfDate().plusYears(year).minusDays(1)
                        .equals(ipu.getValidDate())) {
                    isDateCorrect = true;
                    break;
                }
            }

            if (!isDateCorrect && !mpi.isEmpty()) {
                printFaultMessage(manufactureNumber, "Несоответствие дат МПИ");
            }

            otherMethodicList.forEach((mpModel) -> {
                if (mpModel.meters().stream()
                        .anyMatch(regNumber::equals)) {
                    ipu.setDocTitle(mpModel.docTitle());
                }
            });
        });

        if (!hasError) {
            logOut.append("Файл прочитан - ошибок не обнаружено");
        }

        if (hasError) {
            log.error(logOut.toString());
            throw new RuntimeException(logOut.toString());
        }

        return logOut;
    }

    private void printFaultMessage(String number, String message) {
        hasError = true;
        logOut.append("Счетчик с номером ")
                .append(number).append(" - ")
                .append(message).append("\n");
        log.error("Счетчик с номером {} - {}", number, message);

    }

    private void checkUsingResource(boolean isHot, boolean isEmptyValue, String manufactureNum) {
        if (isEmptyValue) {
            String message = isHot ? "Не используется для ГВС" : "Не используется для ХВС";
            printFaultMessage(manufactureNum, message);
        }
    }
}