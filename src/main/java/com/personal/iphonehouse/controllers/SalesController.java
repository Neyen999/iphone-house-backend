package com.personal.iphonehouse.controllers;

import com.personal.iphonehouse.dtos.SaleDto;
import com.personal.iphonehouse.services.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v${api.version}/sale")
public class SalesController {
    @Autowired
    private SaleService saleService;

    @GetMapping("/sales")
    public ResponseEntity<List<SaleDto>> getSales(@RequestParam(value = "search", defaultValue = "") String search,
                                                  @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date desiredDate) {
        return ResponseEntity.ok(saleService.getSales(search, desiredDate));
    }

    @PostMapping("")
    public ResponseEntity<SaleDto> saveSale(@RequestBody SaleDto request) {
        return ResponseEntity.ok(saleService.saveSale(request));
    }
}
