package co.goorm.happiness.attendance.hello.controller;


import co.goorm.happiness.attendance.response.AttendanceResponse;
import co.goorm.happiness.attendance.response.dto.AttendanceCheckDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Slf4j
@RestController
@RequiredArgsConstructor
public class HelloController {

    @GetMapping("/")
    public ResponseEntity<?> helloWorld() {

        return ResponseEntity.ok(new AttendanceResponse<>(200, 0, LocalDate.now(), "HELLO MY FRIEND"));

    }

    @CrossOrigin(origins = "*")
    @GetMapping("/example")
    public ResponseEntity<?> sendExample(){
        List<AttendanceCheckDto> people = new ArrayList<>();
        Integer[] example1;
        Integer[] example2;

        List<Integer> list = Arrays.asList(5, 5, 5, 5, 5, 5, 5, 5);
        example1 = list.toArray(new Integer[0]);

        list = Arrays.asList(2, 1, 1, 1, 1, 1, 1, 1);
        example2 = list.toArray(new Integer[0]);

        people.add(AttendanceCheckDto.builder()
                .id("")
                .name("양재선")
                .checkList(example2)
                .build());

        people.add(AttendanceCheckDto.builder()
                .id("")
                .name("박준수")
                .checkList(example2)
                .build());

        people.add(AttendanceCheckDto.builder()
                .id("")
                .name("김수연")
                .checkList(example2)
                .build());

        people.add(AttendanceCheckDto.builder()
                .id("")
                .name("최윤석")
                .checkList(example2)
                .build());

        people.add(AttendanceCheckDto.builder()
                .id("")
                .name("윤창호")
                .checkList(example1)
                .build());

        people.add(AttendanceCheckDto.builder()
                .id("")
                .name("이재현98")
                .checkList(example1)
                .build());

        people.add(AttendanceCheckDto.builder()
                .id("")
                .name("박준수")
                .checkList(example2)
                .build());

        people.add(AttendanceCheckDto.builder()
                .id("")
                .name("박준수")
                .checkList(example2)
                .build());

        people.add(AttendanceCheckDto.builder()
                .id("")
                .name("박준수")
                .checkList(example2)
                .build());

        people.add(AttendanceCheckDto.builder()
                .id("")
                .name("박준수")
                .checkList(example2)
                .build());

        people.add(AttendanceCheckDto.builder()
                .id("")
                .name("박준수")
                .checkList(example2)
                .build());

        people.add(AttendanceCheckDto.builder()
                .id("")
                .name("박준수")
                .checkList(example2)
                .build());

        people.add(AttendanceCheckDto.builder()
                .id("")
                .name("박준수")
                .checkList(example2)
                .build());

        people.add(AttendanceCheckDto.builder()
                .id("")
                .name("박준수")
                .checkList(example2)
                .build());

        people.add(AttendanceCheckDto.builder()
                .id("")
                .name("박준수")
                .checkList(example2)
                .build());

        people.add(AttendanceCheckDto.builder()
                .id("")
                .name("박준수")
                .checkList(example2)
                .build());

        people.add(AttendanceCheckDto.builder()
                .id("")
                .name("박준수")
                .checkList(example2)
                .build());

        people.add(AttendanceCheckDto.builder()
                .id("")
                .name("박준수")
                .checkList(example2)
                .build());

        people.add(AttendanceCheckDto.builder()
                .id("")
                .name("박준수")
                .checkList(example2)
                .build());

        people.add(AttendanceCheckDto.builder()
                .id("")
                .name("박준수")
                .checkList(example2)
                .build());

        people.add(AttendanceCheckDto.builder()
                .id("")
                .name("박준수")
                .checkList(example2)
                .build());

        people.add(AttendanceCheckDto.builder()
                .id("")
                .name("박준수")
                .checkList(example2)
                .build());

        people.add(AttendanceCheckDto.builder()
                .id("")
                .name("박준수")
                .checkList(example2)
                .build());

        people.add(AttendanceCheckDto.builder()
                .id("")
                .name("박준수")
                .checkList(example2)
                .build());

        people.add(AttendanceCheckDto.builder()
                .id("")
                .name("박준수")
                .checkList(example2)
                .build());

        people.add(AttendanceCheckDto.builder()
                .id("")
                .name("박준수")
                .checkList(example2)
                .build());

        people.add(AttendanceCheckDto.builder()
                .id("")
                .name("박준수")
                .checkList(example2)
                .build());

        people.add(AttendanceCheckDto.builder()
                .id("")
                .name("박준수")
                .checkList(example2)
                .build());

        people.add(AttendanceCheckDto.builder()
                .id("")
                .name("박준수")
                .checkList(example2)
                .build());

        people.add(AttendanceCheckDto.builder()
                .id("")
                .name("박준수")
                .checkList(example2)
                .build());

        people.add(AttendanceCheckDto.builder()
                .id("")
                .name("박준수")
                .checkList(example2)
                .build());


        people.add(AttendanceCheckDto.builder()
                .id("")
                .name("박준수")
                .checkList(example2)
                .build());

        people.add(AttendanceCheckDto.builder()
                .id("")
                .name("박준수")
                .checkList(example2)
                .build());

        people.add(AttendanceCheckDto.builder()
                .id("")
                .name("박준수")
                .checkList(example2)
                .build());

        long maxLastModified = people.stream()
                .mapToLong(AttendanceCheckDto::getLastModified)
                .max()
                .orElse(System.currentTimeMillis());


        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.maxAge(20, TimeUnit.SECONDS))
                .lastModified(maxLastModified)
                .body(people);

    }
}
