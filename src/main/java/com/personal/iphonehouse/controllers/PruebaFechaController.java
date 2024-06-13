
package com.personal.iphonehouse.controllers;

import com.personal.iphonehouse.dtos.PruebaFechaDto;
import com.personal.iphonehouse.models.PruebaFecha;
import com.personal.iphonehouse.services.PruebaFechaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/v${api.version}/test")
public class PruebaFechaController {

    @Autowired
    private PruebaFechaService service;

    @GetMapping("/pruebaFechas")
    public List<PruebaFechaDto> getPruebaFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        // Convertir LocalDate a LocalDateTime (inicio del día y fin del día)
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        return service.findByDateRange(startDateTime, endDateTime);
    }

    @GetMapping("/{id}")
    public PruebaFecha getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping("/pruebaFecha")
    public PruebaFechaDto create() {
        return service.save();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }
}
