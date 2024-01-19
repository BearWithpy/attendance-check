package co.goorm.happiness.attendance.response;

import java.time.LocalDate;
import java.time.LocalTime;

public record AttendanceResponse<T>(int status, int size, LocalDate date, T data) {

}
