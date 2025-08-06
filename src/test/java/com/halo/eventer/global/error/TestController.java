package com.halo.eventer.global.error;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Profile("test")
@RestController
public class TestController {

    @GetMapping("/api/test/method")
    public ResponseEntity<String> getTest() {
        return ResponseEntity.ok("GET success");
    }

    @PostMapping("/api/test/requestBody")
    public ResponseEntity<String> postTest(@RequestBody TestDto testDto) {
        return ResponseEntity.ok("POST success");
    }

    @GetMapping("/api/test/requestParam")
    public ResponseEntity<String> postTestParam(@RequestParam int param) {
        return ResponseEntity.ok("POST success");
    }

    @PostMapping("/test/festival")
    public String securityExceptionHandlingTest(@RequestBody TestDto testDto) {
        return testDto.getName();
    }

    @GetMapping("/test/festival")
    public String securityGetTest() {
        return "festival";
    }
}
