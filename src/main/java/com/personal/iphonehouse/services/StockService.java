package com.personal.iphonehouse.services;

import com.personal.iphonehouse.dtos.StockDto;
import com.personal.iphonehouse.models.Category;
import com.personal.iphonehouse.models.Product;
import com.personal.iphonehouse.models.Stock;
import com.personal.iphonehouse.repositories.CategoryRepository;
import com.personal.iphonehouse.repositories.ProductRepository;
import com.personal.iphonehouse.repositories.StockRepository;
import com.personal.iphonehouse.utils.DateUtil;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class StockService {
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public StockDto saveStock(StockDto request) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        if (stockRepository.existsByProductIdAndDateCreatedAndIsDeleteFalse(
                request.getProduct().getId(), startOfDay, endOfDay)) {
            throw new RuntimeException("Ya hay un stock para esta fecha y para este producto");
        }

        // stock general = stock de caja + stock de mostrador
        if (!request.isTester() && (request.getInitialCounterStock() + request.getInitialRegisterStock() != request.getCurrentStock())) {
            throw new RuntimeException("El stock de caja y el de mostrador no coinciden con el general");
        }

        Stock stock = modelMapper.map(request, Stock.class);

        // el stock final se calcula al final del día, pueden agregarlo manualmente o hay un runner que lo ejecuta.

        Stock savedStock = stockRepository.save(stock);

        return modelMapper.map(savedStock, StockDto.class);
    }

    public StockDto getStock(Integer id) {
        Stock stock = stockRepository.findById(id).orElseThrow(() -> new RuntimeException("Error"));

        return modelMapper.map(stock, StockDto.class);
    }

    public List<Stock> getStocksByDate(LocalDateTime dateTime) {
        LocalDate date = dateTime.toLocalDate();
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return stockRepository.findByDateCreatedAndIsDeleteFalse(startOfDay, endOfDay);
    }

    public StockDto getStocksByDateTodayAndProduct(Product product) {
        LocalDate today = LocalDate.now(ZoneId.of("America/Argentina/Buenos_Aires"));
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        Stock stock = stockRepository.findByDateCreatedBetweenAndProductAndIsDeleteFalse(startOfDay, endOfDay, product);

        return stock != null ? convertToDto(stock) : null;
    }


    public Page<StockDto> getAllStock(String search, LocalDate desiredDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        LocalDate date = desiredDate != null ? desiredDate : LocalDate.now(ZoneId.of("America/Argentina/Buenos_Aires"));
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        Page<Stock> stockPage = stockRepository.searchStock(search, startOfDay, endOfDay, pageable);

        List<StockDto> stockDtos = stockPage.getContent().stream()
                .map(this::convertToDto)
                .toList();

        return new PageImpl<>(stockDtos, pageable, stockPage.getTotalElements());
    }


    @Transactional
    public StockDto editStock(StockDto request, Integer id) {
        Stock stock = stockRepository.findById(id).orElseThrow(() -> new RuntimeException("Error"));
        // dejemos que se pueda editar everything
//        stock.setInitialStock(request.getInitialStock());
        stock.setFinalStock(request.getFinalStock());
        stock.setCounterReposition(request.getCounterReposition());
        stock.setRegisterReposition(request.getRegisterReposition());
        stock.setDateUpdated(LocalDateTime.now());
        stock.setRegisterSales(request.getRegisterSales());
        stock.setCounterSales(request.getCounterSales());

        if (request.getCurrentCounterStock() + request.getCurrentRegisterStock() != request.getCurrentStock()) {
            throw new RuntimeException("El stock de caja y el de mostrador no coinciden con el general");
        }

        stockRepository.save(stock);

        return modelMapper.map(stock, StockDto.class);
    }

    public Optional<LocalDateTime> getLastStockDate() {
        return stockRepository.findTopByIsDeleteFalseOrderByDateCreatedDesc()
                .map(Stock::getDateCreated);
    }

    public List<Stock> stocksByProduct(Product product) {
        return stockRepository.findByProduct(product);
    }

    @Transactional
    public void deleteStocksByProduct(List<Stock> stocks) {
        for (Stock stock : stocks) {
            stock.setDelete(true);
            stockRepository.save(stock);
        }
    }

    public StockDto convertToDto(Stock stock) {
        return modelMapper.map(stock, StockDto.class);
    }
}
