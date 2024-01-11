package co.goorm.happiness.attendance.hello.controller;


import co.goorm.happiness.attendance.response.AttendanceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
public class HelloController {

    @GetMapping("/")
    public ResponseEntity<?> helloWorld() {

        return ResponseEntity.ok(new AttendanceResponse<>(200,"HELLO MY FRIEND"));

    }
}
