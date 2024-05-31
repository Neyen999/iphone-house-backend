package com.personal.iphonehouse.services;

import com.personal.iphonehouse.dtos.ProductDto;
import com.personal.iphonehouse.dtos.ProductSimpleDto;
import com.personal.iphonehouse.dtos.StockDto;
import com.personal.iphonehouse.models.Product;
import com.personal.iphonehouse.models.Stock;
import com.personal.iphonehouse.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class StockGenerationService {
    @Autowired
    private StockService stockService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Scheduled(cron = "0 0 8 * * MON-SAT", zone = "America/Argentina/Buenos_Aires")
    public void generateStockForNewDay() {
        // 1- obtengo todos los stocks del día anterior
        // Obtener la fecha de referencia
        LocalDate today = LocalDate.now(ZoneId.of("America/Argentina/Buenos_Aires"));
        LocalDate referenceDate = today.minusDays(1);

        // Si hoy es lunes, buscar el último sábado
        if (today.getDayOfWeek() == DayOfWeek.MONDAY) {
            referenceDate = today.minusDays(2); // Último sábado
        }
        Date referenceDateDate = Date.from(referenceDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Tengo que ver que pasa con los feriados porque estos tipos hacen lo que quieren

        List<Stock> stocks = stockService.getStocksByDate(referenceDateDate);
        // 2- En base a esos viejos stocks, creo los nuevos.
        for (Stock stock : stocks) {
            int initialStock = stock.getFinalStock(); // inicia con lo del día anterior
            int newInitialCounterStock = stock.getCurrentCounterStock();
            // el initial stock y el general stock tiene que ser igual a la suma de los stock de caja y mostrador
            int newInitialRegisterStock = stock.getCurrentRegisterStock();
            boolean tester = stock.isTester();
            Product product = productRepository.findById(stock.getProduct().getId()).orElseThrow(() -> new RuntimeException("Error"));


            StockDto brandNewStock = new StockDto(null, stock.getIdealStock(),
                    initialStock,
                    newInitialCounterStock,
                    newInitialRegisterStock,
                    0,
                    0,
                    0,
                    0,
                    null,
                    tester,
                    modelMapper.map(product, ProductSimpleDto.class));

            stockService.saveStock(brandNewStock);
        }

    }

}
