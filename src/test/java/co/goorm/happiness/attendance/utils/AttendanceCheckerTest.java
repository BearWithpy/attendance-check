package co.goorm.happiness.attendance.utils;

import co.goorm.happiness.attendance.response.dto.ParticipantDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

class AttendanceCheckerTest {

    private AttendanceChecker attendanceChecker;

    @BeforeEach
    void setUp() {
        attendanceChecker = new AttendanceChecker();
    }

    @Test
    @DisplayName("잘못된 이름 걸러내기")
    void excludeInvalidNameValue() {
        List<ParticipantDto> people = new ArrayList<>();
        ParticipantDto example = ParticipantDto.builder()
                .name("PC")
                .joinTime(OffsetDateTime.parse("2024-01-15T01:30:59Z").toLocalDateTime())
                .leaveTime(OffsetDateTime.parse("2024-01-15T09:20:11Z").toLocalDateTime())
                .id("")
                .build();
        ParticipantDto example2 = ParticipantDto.builder()
                .name("구름관리자")
                .joinTime(OffsetDateTime.parse("2024-01-15T01:30:59Z").toLocalDateTime())
                .leaveTime(OffsetDateTime.parse("2024-01-15T09:20:11Z").toLocalDateTime())
                .id("")
                .build();
        people.add(example);
        people.add(example2);

        Assertions.assertThat(attendanceChecker.attendanceCheck(people).size())
                .isEqualTo(0);

    }

    @Test
    @DisplayName("이름 전처리")
    void preprocessingNameTest() {
        List<ParticipantDto> people = new ArrayList<>();
        ParticipantDto example = ParticipantDto.builder()
                .name("박준수(풀스택 3회차)")
                .joinTime(OffsetDateTime.parse("2024-01-15T01:30:59Z").toLocalDateTime())
                .leaveTime(OffsetDateTime.parse("2024-01-15T09:20:11Z").toLocalDateTime())
                .id("")
                .build();

        people.add(example);


        Assertions.assertThat(attendanceChecker.attendanceCheck(people).get(0).getName())
                .isEqualTo("박준수");

    }

    @Test
    @DisplayName("정상 출결")
    void checkSuccessfulAttendance() {

        List<ParticipantDto> people = new ArrayList<>();
        ParticipantDto example = ParticipantDto.builder()
                .name("김수연")
                .joinTime(OffsetDateTime.parse("2024-01-23T00:58:28Z").toLocalDateTime())
                .leaveTime(OffsetDateTime.parse("2024-01-23T09:50:08Z").toLocalDateTime())
                .id("")
                .build();

//        ParticipantDto example2 = ParticipantDto.builder()
//                .name("김수연")
//                .joinTime(OffsetDateTime.parse("2024-01-23T01:58:28Z").toLocalDateTime())
//                .leaveTime(OffsetDateTime.parse("2024-01-23T04:50:08Z").toLocalDateTime())
//                .id("")
//                .build();

//        people.add(example2);
        people.add(example);


        Assertions.assertThat(attendanceChecker.attendanceCheck(people).get(0).getCheckList())
                .isEqualTo(new Integer[]{1, 1, 1, 1, 1, 1, 1, 1});
    }




    @Test
    @DisplayName("1교시 닌자들")
    void checkFirstTime() {

        List<ParticipantDto> people = new ArrayList<>();
        ParticipantDto example = ParticipantDto.builder()
                .name("박준수")
                .joinTime(OffsetDateTime.parse("2024-01-15T01:09:59Z").toLocalDateTime())
                .leaveTime(OffsetDateTime.parse("2024-01-15T01:10:11Z").toLocalDateTime())
                .id("")
                .build();
        people.add(example);


        Assertions.assertThat(attendanceChecker.attendanceCheck(people).get(0).getCheckList())
                .isEqualTo(new Integer[]{1, 1, 1, 1, 1, 1, 1, 5});
    }



    @Test
    @DisplayName("1교시 지각, 8교시 결석 -> 1일 결석")
    void checkSingleStatus() {

        List<ParticipantDto> people = new ArrayList<>();
        ParticipantDto example = ParticipantDto.builder()
                .name("박준수")
                .joinTime(OffsetDateTime.parse("2024-01-15T01:11:59Z").toLocalDateTime())
                .leaveTime(OffsetDateTime.parse("2024-01-15T01:30:11Z").toLocalDateTime())
                .id("")
                .build();

        ParticipantDto example2 = ParticipantDto.builder()
                .name("박준수")
                .joinTime(OffsetDateTime.parse("2024-01-15T02:14:59Z").toLocalDateTime())
                .leaveTime(OffsetDateTime.parse("2024-01-15T03:19:11Z").toLocalDateTime())
                .id("")
                .build();


        ParticipantDto example3 = ParticipantDto.builder()
                .name("박준수")
                .joinTime(OffsetDateTime.parse("2024-01-15T09:45:59Z").toLocalDateTime())
                .leaveTime(OffsetDateTime.parse("2024-01-15T09:48:11Z").toLocalDateTime())
                .id("")
                .build();
        people.add(example);
        people.add(example2);
        people.add(example3);


        Assertions.assertThat(attendanceChecker.attendanceCheck(people).get(0).getCheckList())
                .isEqualTo(new Integer[]{5, 5, 5, 5, 5, 5, 5, 5});
    }

    @Test
    @DisplayName("체크리스트 로직 테스트 - 왔다갔다한 기록없음")
    void checkStatus() {

        List<ParticipantDto> people = new ArrayList<>();
        ParticipantDto example = ParticipantDto.builder()
                .name("박준수")
                .joinTime(OffsetDateTime.parse("2024-01-15T01:10:59Z").toLocalDateTime())
                .leaveTime(OffsetDateTime.parse("2024-01-15T09:50:11Z").toLocalDateTime())
                .id("")
                .build();

        ParticipantDto example2 = ParticipantDto.builder()
                .name("양재선")
                .joinTime(OffsetDateTime.parse("2024-01-15T01:13:11Z").toLocalDateTime())
                .leaveTime(OffsetDateTime.parse("2024-01-15T09:50:11Z").toLocalDateTime())
                .id("")
                .build();

        ParticipantDto example3 = ParticipantDto.builder()
                .name("이재현98")
                .joinTime(OffsetDateTime.parse("2024-01-15T01:10:11Z").toLocalDateTime())
                .leaveTime(OffsetDateTime.parse("2024-01-15T09:40:11Z").toLocalDateTime())
                .id("")
                .build();

        ParticipantDto example4 = ParticipantDto.builder()
                .name("윤창호")
                .joinTime(OffsetDateTime.parse("2024-01-15T01:12:11Z").toLocalDateTime())
                .leaveTime(OffsetDateTime.parse("2024-01-15T09:40:11Z").toLocalDateTime())
                .id("")
                .build();

        people.add(example);
        people.add(example2);
        people.add(example3);
        people.add(example4);

        // 이름 순 sorting도 고려
        // 박
        Assertions.assertThat(attendanceChecker.attendanceCheck(people).get(0).getCheckList())
                .isEqualTo(new Integer[]{1, 1, 1, 1, 1, 1, 1, 1});

        // 양
        Assertions.assertThat(attendanceChecker.attendanceCheck(people).get(1).getCheckList())
                .isEqualTo(new Integer[]{2, 1, 1, 1, 1, 1, 1, 1});

        // 윤
        Assertions.assertThat(attendanceChecker.attendanceCheck(people).get(2).getCheckList())
                .isEqualTo(new Integer[]{5, 5, 5, 5, 5, 5, 5, 5});

        // 이
        Assertions.assertThat(attendanceChecker.attendanceCheck(people).get(3).getCheckList())
                .isEqualTo(new Integer[]{1, 1, 1, 1, 1, 1, 1, 5});
    }

    @Test
    @DisplayName("체크리스트 로직 테스트 - 왔다갔다한 기록있음")
    void checkStatusMultiple() {

        List<ParticipantDto> people = new ArrayList<>();
        ParticipantDto example = ParticipantDto.builder()
                .name("박준수")
                .joinTime(OffsetDateTime.parse("2024-01-15T01:09:59Z").toLocalDateTime())
                .leaveTime(OffsetDateTime.parse("2024-01-15T03:50:11Z").toLocalDateTime())
                .id("")
                .build();

        ParticipantDto example2 = ParticipantDto.builder()
                .name("박준수")
                .joinTime(OffsetDateTime.parse("2024-01-15T05:12:59Z").toLocalDateTime())
                .leaveTime(OffsetDateTime.parse("2024-01-15T06:50:11Z").toLocalDateTime())
                .id("")
                .build();

        ParticipantDto example3 = ParticipantDto.builder()
                .name("박준수")
                .joinTime(OffsetDateTime.parse("2024-01-15T09:12:59Z").toLocalDateTime())
                .leaveTime(OffsetDateTime.parse("2024-01-15T09:45:11Z").toLocalDateTime())
                .id("")
                .build();


        people.add(example);
        people.add(example2);
        people.add(example3);

        // 박
        Assertions.assertThat(attendanceChecker.attendanceCheck(people).get(0).getCheckList())
                .isEqualTo(new Integer[]{1, 1, 1, 1, 1, 1, 1, 5});
    }

    @Test
    @DisplayName("체크리스트 로직 테스트 - 왔다갔다한 기록있음(여러사람)")
    void checkStatusMultiplePeople() {

        List<ParticipantDto> people = new ArrayList<>();
        ParticipantDto example = ParticipantDto.builder()
                .name("박준수")
                .joinTime(OffsetDateTime.parse("2024-01-15T01:09:59Z").toLocalDateTime())
                .leaveTime(OffsetDateTime.parse("2024-01-15T03:50:11Z").toLocalDateTime())
                .id("")
                .build();

        ParticipantDto example2 = ParticipantDto.builder()
                .name("양재선")
                .joinTime(OffsetDateTime.parse("2024-01-15T01:09:59Z").toLocalDateTime())
                .leaveTime(OffsetDateTime.parse("2024-01-15T08:50:11Z").toLocalDateTime())
                .id("")
                .build();

        ParticipantDto example3 = ParticipantDto.builder()
                .name("박준수")
                .joinTime(OffsetDateTime.parse("2024-01-15T05:12:59Z").toLocalDateTime())
                .leaveTime(OffsetDateTime.parse("2024-01-15T06:50:11Z").toLocalDateTime())
                .id("")
                .build();

        ParticipantDto example4 = ParticipantDto.builder()
                .name("박준수")
                .joinTime(OffsetDateTime.parse("2024-01-15T09:12:59Z").toLocalDateTime())
                .leaveTime(OffsetDateTime.parse("2024-01-15T09:45:11Z").toLocalDateTime())
                .id("")
                .build();

        ParticipantDto example5 = ParticipantDto.builder()
                .name("양재선")
                .joinTime(OffsetDateTime.parse("2024-01-15T08:52:59Z").toLocalDateTime())
                .leaveTime(OffsetDateTime.parse("2024-01-15T09:50:11Z").toLocalDateTime())
                .id("")
                .build();


        people.add(example);
        people.add(example2);
        people.add(example3);
        people.add(example4);
        people.add(example5);

        // 박
        Assertions.assertThat(attendanceChecker.attendanceCheck(people).get(0).getCheckList())
                .isEqualTo(new Integer[]{1, 1, 1, 1, 1, 1, 1, 5});
        // 양
        Assertions.assertThat(attendanceChecker.attendanceCheck(people).get(1).getCheckList())
                .isEqualTo(new Integer[]{1, 1, 1, 1, 1, 1, 1, 1});
    }

}