package com.personal.iphonehouse.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v${api.version}/test")
public class TestController {
    @GetMapping("/status")
    public ResponseEntity<?> appStatus() {
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
