package com.personal.iphonehouse.controllers;

import com.personal.iphonehouse.repositories.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping(value = "/api/v${api.version}/test")
public class TestController {
    @Autowired
    private StockRepository stockRepository;
    @GetMapping("/status")
    public ResponseEntity<?> appStatus() {
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/checkExists")
    public ResponseEntity<Boolean> checkValue() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        return ResponseEntity.ok(stockRepository.existsByProductIdAndDateCreatedAndIsDeleteFalse(2, startOfDay, endOfDay));
    }
}
