package com.example.thirdbackend;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Hello", description = "간단한 인사 API")
public class HelloController {

    @Operation(summary = "인사 메시지 반환", description = "Hello Spring Boot! 메시지를 반환합니다.")
    @GetMapping("/hello")
    public String hello() {
        return "Hello Spring Boot!";
    }

}


