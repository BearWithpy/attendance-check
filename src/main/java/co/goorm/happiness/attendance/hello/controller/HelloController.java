package co.goorm.happiness.attendance.hello.controller;


import co.goorm.happiness.attendance.response.AttendanceResponse;
import co.goorm.happiness.attendance.response.dto.AttendanceCheckDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
public class HelloController {

    @GetMapping("/")
    public ResponseEntity<?> helloWorld() {

        return ResponseEntity.ok(new AttendanceResponse<>(200,"HELLO MY FRIEND"));

    }

    @GetMapping("/example")
    public ResponseEntity<?> sendExample() throws JsonProcessingException {

        String exampleData = """
          {
            "status": 200,
            "data": [
                {
                    id: "",
                    name: "김주원",
                    checkList: [5, 5, 5, 5, 5, 5, 5, 5]
                },
                {
                    id: "",
                    name: "송민선",
                    checkList: [5, 5, 5, 5, 5, 5, 5, 5]
                },
                {
                    id: "",
                    name: "심재원",
                    checkList: [5, 5, 5, 5, 5, 5, 5, 5]
                },
                {
                    id: "",
                    name: "이재현",
                    checkList: [5, 5, 5, 5, 5, 5, 5, 5]
                },
                {
                    id: "",
                    name: "정성진",
                    checkList: [5, 5, 5, 5, 5, 5, 5, 5]
                }
            ]
        }
        """;

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // Deserialize into an array of AttendanceCheckDto
        AttendanceCheckDto[] people = objectMapper.readValue(exampleData, AttendanceCheckDto[].class);

        return ResponseEntity.ok(new AttendanceResponse<>(200, people));

    }
}
