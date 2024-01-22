package co.goorm.happiness.attendance.utils;

import co.goorm.happiness.attendance.model.AttendanceStatus;
import co.goorm.happiness.attendance.response.dto.AttendanceCheckDto;
import co.goorm.happiness.attendance.response.dto.ParticipantDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Slf4j
@Component
public class AttendanceChecker {
    private static final List<String> EXCLUDED_NAMES = Arrays.asList("구름", "코치", "관리자", "구름관리자", "goorm", "Goorm", "GOORM", "PC", "pc", "pc접속", "출결관리자");

    private static final List<LocalDateTimeRange> CLASS_SESSIONS = Arrays.asList(
            new LocalDateTimeRange(LocalTime.of(10, 11), LocalTime.of(10, 50)),
            new LocalDateTimeRange(LocalTime.of(11, 10), LocalTime.of(11, 50)),
            new LocalDateTimeRange(LocalTime.of(12, 10), LocalTime.of(12, 50)),
            new LocalDateTimeRange(LocalTime.of(14, 10), LocalTime.of(14, 50)),
            new LocalDateTimeRange(LocalTime.of(15, 10), LocalTime.of(15, 50)),
            new LocalDateTimeRange(LocalTime.of(16, 10), LocalTime.of(16, 50)),
            new LocalDateTimeRange(LocalTime.of(17, 10), LocalTime.of(17, 50)),
            new LocalDateTimeRange(LocalTime.of(18, 10), LocalTime.of(18, 49))
    );

    public List<AttendanceCheckDto> attendanceCheck(List<ParticipantDto> data) {

        List<AttendanceCheckDto> result = new ArrayList<>();
        Map<String, Integer[]> processedRecords = new HashMap<>();


        for (ParticipantDto participant : data) {
            String name = participant.getName();
            String id = participant.getId();
            Integer[] checkList;


            if (EXCLUDED_NAMES.contains(preprocessName(name)) || EXCLUDED_NAMES.contains(name)) {
                log.info("Excluded name: {}", name);
                continue;
            }


            // id 값이 빈값으로 와용.....;;;;;;
//            String participantKey = name + id;

            if (processedRecords.containsKey(name)) {
                log.info("나왔던 이름입니다.");
                checkList = processedRecords.get(name);
            } else {
                checkList = new Integer[8];
                Arrays.fill(checkList, -99);
            }

            for (int i = 0; i < CLASS_SESSIONS.size(); i++) {
                LocalDateTimeRange classSession = CLASS_SESSIONS.get(i);

                // 한국 시차 적용 - UTC의 경우
                LocalDateTime adjustedJoinTime = participant.getJoinTime().plusHours(9);
                LocalDateTime adjustedLeaveTime = participant.getLeaveTime().plusHours(9);

                // 한국
//                LocalDateTime adjustedJoinTime = participant.getJoinTime().plusHours(0);
//                LocalDateTime adjustedLeaveTime = participant.getLeaveTime().plusHours(0);

                if (checkList[i] == -99) {
                    if (adjustedJoinTime.toLocalTime().isBefore(classSession.getStartTime()) &&
                            adjustedLeaveTime.toLocalTime().isAfter(classSession.getEndTime())) {
                        checkList[i] = AttendanceStatus.ATTENDANCE.getCode();
                    } else if (adjustedJoinTime.toLocalTime().isAfter(classSession.getStartTime()) &&
                            adjustedJoinTime.toLocalTime().isBefore(classSession.getEndTime()) &&
                            adjustedLeaveTime.toLocalTime().isAfter(classSession.getEndTime())) {
                        checkList[i] = AttendanceStatus.LATE.getCode();

                        if (i > 0 && Objects.equals(checkList[i - 1], AttendanceStatus.LATE.getCode())) {
                            checkList[i - 1] = AttendanceStatus.ABSENT.getCode();
                        }
                    }
                }

                // 1교시 스크린샷 후, 바로 나가기...
                if (i == 0 &&
                        adjustedJoinTime.toLocalTime().isBefore(LocalTime.of(10, 10)) &&
                        adjustedLeaveTime.toLocalTime().isAfter(LocalTime.of(10, 10))) {
                    checkList[i] = AttendanceStatus.ATTENDANCE.getCode();
                }
            }


            // 이미 처리된 참가자에 대한 정보를 맵에 저장
            processedRecords.put(name, checkList);

            // 결과 리스트에 추가하기 전에 이미 존재하는 경우 수정
            boolean updated = false;
            for (AttendanceCheckDto dto : result) {
                if (dto.getName().equals(preprocessName(name))) {
                    dto.setCheckList(checkList);
                    updated = true;
                    break;
                }
            }

            if (!updated) {
                result.add(AttendanceCheckDto.builder()
                        .name(preprocessName(name))
                        .checkList(checkList)
                        .build());
            }
        }


        for (AttendanceCheckDto dto : result) {
            Integer[] checkList = dto.getCheckList();

            for (int i = 0; i < checkList.length; i++) {
                if (checkList[i] == -99) {
                    checkList[i] = 5;
                }
            }
            log.info("{}{}{}{}{}{}{}{}", checkList[0], checkList[1], checkList[2], checkList[3], checkList[4], checkList[5], checkList[6], checkList[7]);


            // 지금의 룰... 2교시부터 7교시까지는....
            for (int i = 1; i < checkList.length - 1; i++) {
                if (checkList[i] != 1) {
                    checkList[i] = 1;
                }
            }
            // 1교시 결석도....
            if (checkList[0] == 5) {
                checkList[0] = 2;
            }
            // 8교시 지각도....
            if (checkList[7] == 2) {
                checkList[7] = 1;
            }

            // 추가 결석 로직
//            long countOf5 = Arrays.stream(checkList).filter(value -> value == 5).count();
//            if (countOf5 >= 5) {
//                Arrays.fill(checkList, 5);
//            }

//            if (checkList[0] == 5 && checkList[checkList.length - 1] == 5) {
//                Arrays.fill(checkList, 5);
//            }


            if (checkList[0] == 2 && checkList[checkList.length - 1] == 5) {
                Arrays.fill(checkList, 5);
            }
            log.info(">>>>>>>>>>{}{}{}{}{}{}{}{}", checkList[0], checkList[1], checkList[2], checkList[3], checkList[4], checkList[5], checkList[6], checkList[7]);


        }

        result.sort(Comparator.comparing(AttendanceCheckDto::getName));
        return result;
    }


    private String preprocessName(String value) {
        if (value.contains("(")) {
            value = value.substring(0, value.indexOf("("));
        }

        if (value.startsWith("풀")) {
            value = value.substring(value.length() - 3);
        }

        value = value.replaceAll("\\s", "");

        return value;
    }

    @Data
    @AllArgsConstructor
    @Builder
    private static class LocalDateTimeRange {
        private final LocalTime startTime;
        private final LocalTime endTime;
    }
}