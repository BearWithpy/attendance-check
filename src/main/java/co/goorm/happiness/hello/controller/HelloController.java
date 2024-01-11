package co.goorm.happiness.hello.controller;


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
        try{


            return ResponseEntity.ok(new AttendanceResponse<>(200,"HELLO MY FRIEND"));
        } catch (Exception e) {
            log.error("An unexpected error occurred", e);
            return ResponseEntity.status(500).body("An unexpected error occurred");
        }
    }
}
