package com.personal.iphonehouse.controllers;

import com.personal.iphonehouse.dtos.SaleDto;
import com.personal.iphonehouse.services.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v${api.version}/sale")
public class SalesController {
    @Autowired
    private SaleService saleService;

    @GetMapping("/sales")
    public ResponseEntity<Page<SaleDto>> getSales(@RequestParam(value = "search", defaultValue = "", required = false) String search,
                                                  @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                  @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                  @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                  @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ResponseEntity.ok(saleService.getSales(search, startDate, endDate, page, size));
    }

    @PostMapping("")
    public ResponseEntity<SaleDto> saveSale(@RequestBody SaleDto request) {
        return ResponseEntity.ok(saleService.saveSale(request));
    }

    @DeleteMapping("/sales/{id}")
    public ResponseEntity<SaleDto> deleteSale(@PathVariable Integer id) {
        return ResponseEntity.ok(saleService.deleteSale(id));
    }
}
