package com.example.springApp.service.FGIS;

import com.example.springApp.model.Equipment;
import com.example.springApp.model.IPU;
import com.example.springApp.model.MeterDescription;
import com.example.springApp.model.MpModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class ErrorAndMethodicChecking {

    private final List<MeterDescription> regFifList;
    private final List<String> stopList;
    private final List<MpModel> otherMethodicList;

    private StringBuilder logOut;
    private boolean hasError;

    public StringBuilder check(List<IPU> waterMeterList) {

        hasError = false;
        logOut = new StringBuilder("\n");

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
                printFaultMessage(manufactureNumber, "Указанный поверитель не числится в списке сотрудников");
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
                        String.format("Неправильный тип счетчика. Возможные типы: %s",
                                meterDescription.types()));
            }

            Object mpiValues = meterDescription.mpi();

            if (mpiValues instanceof Map<?, ?> rawMap) {
                // Определяем тип по первому значению
                boolean isListType = rawMap.values().stream()
                        .findFirst()
                        .map(val -> val instanceof List<?>)
                        .orElse(false);

                String key = ipu.isHot() ? "ГВС" : "ХВС";
                boolean isHot = ipu.isHot();

                Object value = rawMap.get(key);

                if (isListType) {
                    // Обработка списка
                    Optional<List<Integer>> list = safelyExtractIntegerList(value);
                    checkUsingResource(isHot, list.isEmpty(), manufactureNumber);
                    mpi.addAll(list.orElseGet(ArrayList::new));
                } else {
                    // Обработка одиночного значения
                    Optional<Integer> number = safelyExtractInteger(value);
                    checkUsingResource(isHot, number.isEmpty(), manufactureNumber);
                    mpi.add(number.orElse(0));
                }
            } else if (mpiValues instanceof Integer intValue) {
                mpi.add(intValue);
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
            logOut.append("Ошибок не обнаружено");
        }

        if (hasError) {
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

    private Optional<List<Integer>> safelyExtractIntegerList(Object value) {
        // Проверяем Optional
        if (value instanceof Optional<?> optional && optional.isPresent()) {
            value = optional.get();
        }

        // Проверяем List
        if (value instanceof List<?> list) {
            try {
                List<Integer> result = list.stream()
                        .map(this::toInteger)  // Конвертируем каждый элемент
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                return Optional.of(result);
            } catch (Exception e) {
                return Optional.empty();
            }
        }

        return Optional.empty();
    }

    private Integer toInteger(Object obj) {
        if (obj instanceof Integer) return (Integer) obj;
        if (obj instanceof Number) return ((Number) obj).intValue();
        if (obj instanceof String) {
            try {
                return Integer.parseInt((String) obj);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private Optional<Integer> safelyExtractInteger(Object value) {
        if (value == null) {
            return Optional.empty();
        }

        return Optional.of((Integer) value);
    }
}