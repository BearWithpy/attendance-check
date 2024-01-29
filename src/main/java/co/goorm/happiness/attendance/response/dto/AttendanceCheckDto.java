package co.goorm.happiness.attendance.response.dto;

import co.goorm.happiness.attendance.model.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AttendanceCheckDto {

    private String id;

    private String name;


    private Integer[] checkList = new Integer[8];

    private long lastModified;

    public AttendanceCheckDto(String id, String name, Integer[] checkList, String dateString) {
        this.id = id;
        this.name = name;
        this.checkList = checkList;

        LocalDate date = LocalDate.parse(dateString);
        LocalDateTime dateTime = date.atStartOfDay();
        this.lastModified = dateTime.toEpochSecond(ZoneOffset.UTC) * 1000;
    }


}
