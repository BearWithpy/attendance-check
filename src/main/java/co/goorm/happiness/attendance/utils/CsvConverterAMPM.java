package co.goorm.happiness.attendance.utils;

import co.goorm.happiness.attendance.response.dto.ParticipantDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Component
public class CsvConverterAMPM {

    private static final List<String> EXCLUDED_KEYS = Arrays.asList("User Email", "Guest", "In Waiting Room");
    private static final List<String> EXCLUDED_NAMES = Arrays.asList("구름", "코치", "관리자", "구름관리자", "goorm", "Goorm", "GOORM", "PC", "pc", "출결관리자");


    public List<ParticipantDto> fromCsvToJson(List<String> csvList) throws JsonProcessingException {
        csvList.removeIf(String::isEmpty);

        if (csvList.size() <= 1) {
            return List.of();
        }

        String[] columns = csvList.get(0).split(",");

        Map<String, List<ParticipantDto>> participantsMap = new HashMap<>();

        csvList.subList(1, csvList.size())
                .stream()
                .map(row -> row.split(","))
                .filter(row -> row.length == columns.length)
                .forEach(row -> {
                    Map<String, String> rowData = Arrays.stream(columns)
                            .filter(key -> !isExcludedKey(key))
                            .collect(Collectors.toMap(this::translateKey, key -> preprocessName(key, row[Arrays.asList(columns).indexOf(key)])));

                    String name = rowData.get("name");

                    if (!isExcludedName(name)) {
                        ParticipantDto participantDto = ParticipantDto.builder()
                                .name(name)
                                .duration(Integer.parseInt(rowData.get("duration")))
                                .joinTime(LocalDateTime.parse(convertTo24HourFormat(rowData.get("join_time")), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                                .leaveTime(LocalDateTime.parse(convertTo24HourFormat(rowData.get("leave_time")), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                                .build();

                        participantsMap.computeIfAbsent(name, k -> new ArrayList<>()).add(participantDto);
                    }
                });

        List<ParticipantDto> result = participantsMap.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        result.sort(Comparator.comparing(ParticipantDto::getName));

        return result;
    }

    private String preprocessName(String key, String value) {
        if ("name".equals(translateKey(key))) {
            if (value.contains("(")) {
                value = value.substring(0, value.indexOf("("));
            }

            if (value.startsWith("풀")) {
                value = value.substring(value.length() - 3);
            }

            value = value.replaceAll("\\s", "");
        }
        return value;
    }

    private boolean isExcludedName(String name) {
        return EXCLUDED_NAMES.contains(name);
    }

    private boolean isExcludedKey(String key) {
        return EXCLUDED_KEYS.contains(key);
    }

    private String convertTo24HourFormat(String value) {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss a", Locale.ENGLISH));
            return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (Exception e) {
            log.error("24시간 형식으로 변환 실패: {}", value, e);
            return null;
        }
    }


    private String translateKey(String originalKey) {
        return switch (originalKey) {
            case "Name (Original Name)" -> "name";
            case "Join Time" -> "join_time";
            case "Leave Time" -> "leave_time";
            case "Duration (Minutes)" -> "duration";
            default -> originalKey;
        };
    }
}

