package com.personal.iphonehouse.controllers;

import com.personal.iphonehouse.dtos.StockDto;
import com.personal.iphonehouse.services.StockGenerationService;
import com.personal.iphonehouse.services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v${api.version}/stock")
public class StockController {
    @Autowired
    private StockService stockService;
    @Autowired
    private StockGenerationService stockGenerationService;

    @GetMapping("/stocks/{id}")
    public ResponseEntity<StockDto> getStock(@PathVariable Integer id) {
        return ResponseEntity.ok(stockService.getStock(id));
    }

    @GetMapping("/stocks")
    public ResponseEntity<Page<StockDto>> getAllStocks(@RequestParam(value = "search", defaultValue = "") String search,
                                                       @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date desiredDate,
                                                       @RequestParam(value = "page", defaultValue = "0") int page,
                                                       @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(stockService.getAllStockByIdAnd(search, desiredDate, page, size));
    }

    @PostMapping("/stockGeneration")
    public void saveStock() {
        stockGenerationService.generateStockForNewDay();
    }

    @PutMapping("/stocks/{id}")
    public ResponseEntity<StockDto> editStock(@RequestBody StockDto request,
                                              @PathVariable Integer id) {
        return ResponseEntity.ok(stockService.editStock(request, id));
    }
}
