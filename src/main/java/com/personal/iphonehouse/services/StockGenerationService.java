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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class StockGenerationService {
    @Autowired
    private StockService stockService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;

    private static final Logger logger = LoggerFactory.getLogger(StockGenerationService.class);


//    @Scheduled(cron = "0 0 8 * * MON-SAT", zone = "America/Argentina/Buenos_Aires")
//    public void generateStockForNewDay() {
//        // 1- obtengo todos los stocks del día anterior
//        // Obtener la fecha de referencia
//        LocalDate today = LocalDate.now(ZoneId.of("America/Argentina/Buenos_Aires"));
//        LocalDate referenceDate = today.minusDays(1);
//
//        // Si hoy es lunes, buscar el último sábado
//        if (today.getDayOfWeek() == DayOfWeek.MONDAY) {
//            referenceDate = today.minusDays(2); // Último sábado
//        }
//        Date referenceDateDate = Date.from(referenceDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
//
//        // Tengo que ver que pasa con los feriados porque estos tipos hacen lo que quieren
//
//        List<Stock> stocks = stockService.getStocksByDate(referenceDateDate);
//        // 2- En base a esos viejos stocks, creo los nuevos.
//        for (Stock stock : stocks) {
//            int initialStock = stock.getFinalStock(); // inicia con lo del día anterior
//            int newInitialCounterStock = stock.getCurrentCounterStock();
//            // el initial stock y el general stock tiene que ser igual a la suma de los stock de caja y mostrador
//            int newInitialRegisterStock = stock.getCurrentRegisterStock();
//            boolean tester = stock.isTester();
//            Product product = productRepository.findById(stock.getProduct().getId()).orElseThrow(() -> new RuntimeException("Error"));
//
//
//            StockDto brandNewStock = new StockDto(null, stock.getIdealStock(),
//                    initialStock,
//                    newInitialCounterStock,
//                    newInitialRegisterStock,
//                    0,
//                    0,
//                    0,
//                    0,
//                    stock.getRegisterTransfersToTester(),
//                    stock.getCounterTransfersToTester(),
//                    null,
//                    tester,
//                    modelMapper.map(product, ProductSimpleDto.class));
//
//            stockService.saveStock(brandNewStock);
//        }
//
//    }
    @Scheduled(cron = "0 0 8 * * MON-SAT", zone = "America/Argentina/Buenos_Aires")
    public void generateStockForNewDay() {
        // Obtener la fecha actual
        LocalDate today = LocalDate.now(ZoneId.of("America/Argentina/Buenos_Aires"));

        // Obtener la última fecha con registros de stock
        Optional<LocalDateTime> lastStockDateOptional = stockService.getLastStockDate();


        logger.info("Encontró algun stock por date? ");
        logger.info(String.valueOf(lastStockDateOptional));

        if (lastStockDateOptional.isEmpty()) {
//          Si no hay stocks previos, salir del método
            return;
        }



//        Date lastStockDate = lastStockDateOptional.get();
//        LocalDate lastStockLocalDate = lastStockDateOptional.get().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//        Date date = Date.from(lastStockLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Obtener los stocks de la última fecha registrada
        List<Stock> stocks = stockService.getStocksByDate(lastStockDateOptional.get());

        // Crear los nuevos stocks basados en los viejos stocks
        for (Stock stock : stocks) {
            int initialStock = stock.getFinalStock() != null ? stock.getFinalStock() : stock.getCurrentStock(); // inicia con lo del día anterior
            int newInitialCounterStock = stock.getCurrentCounterStock();
            int newInitialRegisterStock = stock.getCurrentRegisterStock();
            boolean tester = stock.isTester();
            Product product = productRepository.findById(stock.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Error"));

            StockDto brandNewStock = new StockDto(
                    null, stock.getIdealStock(),
                    initialStock,
                    newInitialCounterStock,
                    newInitialRegisterStock,
                    0,
                    0,
                    0,
                    0,
                    stock.getRegisterTransfersToTester(),
                    stock.getCounterTransfersToTester(),
                    null,
                    tester,
                    modelMapper.map(product, ProductSimpleDto.class)
            );

            stockService.saveStock(brandNewStock);
        }
    }
}
