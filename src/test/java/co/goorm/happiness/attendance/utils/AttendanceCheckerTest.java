package co.goorm.happiness.attendance.utils;

import co.goorm.happiness.attendance.response.dto.AttendanceCheckDto;
import co.goorm.happiness.attendance.response.dto.ParticipantDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AttendanceCheckerTest {

    private AttendanceChecker attendanceChecker;

    @BeforeEach
    void setUp() {
        attendanceChecker = new AttendanceChecker();
    }

    @Test
    @DisplayName("체크리스트 로직 테스트")
    void checkStatus() {

        List<ParticipantDto> people = new ArrayList<>();
        ParticipantDto example = ParticipantDto.builder()
                .name("박준수")
                .joinTime(OffsetDateTime.parse("2024-01-15T01:09:11Z").toLocalDateTime())
                .leaveTime(OffsetDateTime.parse("2024-01-15T09:50:11Z").toLocalDateTime())
                .id("")
                .build();
        people.add(example);

        Assertions.assertThat(attendanceChecker.attendanceCheck(people).get(0).getCheckList())
                .isEqualTo(new Integer[]{1, 1, 1, 1, 1, 1, 1, 1});

    }

}