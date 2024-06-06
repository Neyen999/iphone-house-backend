package com.personal.iphonehouse.controllers;

import com.personal.iphonehouse.dtos.SaleDto;
import com.personal.iphonehouse.services.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<Page<SaleDto>> getSales(@RequestParam(value = "search", defaultValue = "", required = false) String search,
                                                  @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                                  @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
                                                  @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                  @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ResponseEntity.ok(saleService.getSales(search, startDate, endDate, page, size));
    }

    @PostMapping("")
    public ResponseEntity<SaleDto> saveSale(@RequestBody SaleDto request) {
        return ResponseEntity.ok(saleService.saveSale(request));
    }
}
