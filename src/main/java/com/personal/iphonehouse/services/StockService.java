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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

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
        if (stockRepository.existsByProductIdAndDateCreatedAndIsDeleteFalse(request.getProduct().getId(), new DateUtil().utilDateNow())) {
            throw new RuntimeException("Ya hay un stock para esta fecha y para este producto");
        }

        // stock general = stock de caja + stock de mostrador
        if (request.getInitialCounterStock() + request.getInitialRegisterStock() != request.getCurrentStock()) {
            throw new RuntimeException("El stock de caja y el de mostrador no coinciden con el general");
        }

        Stock stock = modelMapper.map(request, Stock.class);

        // el stock final se calcula al final del día, pueden agregarlo manualmente o hay un runner que lo ejecuta.

        stockRepository.save(stock);

        return modelMapper.map(stock, StockDto.class);
    }

    public StockDto getStock(Integer id) {
        Stock stock = stockRepository.findById(id).orElseThrow(() -> new RuntimeException("Error"));

        return modelMapper.map(stock, StockDto.class);
    }

    public List<Stock> getStocksByDate(Date date) {
        return stockRepository.findByDateCreatedAndIsDeleteFalse(date);
    }

    public Page<StockDto> getAllStockByIdAnd(Integer productId,
                                             Integer categoryId,
                                             Date startDate,
                                             Date endDate,
                                             int page,
                                             int size) {
        Pageable pageable = PageRequest.of(page, size);

        Product product = null;
//        Category category = null;

        if (productId != null)
            product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Error"));

//        if (categoryId != null)
//            category = categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Error"));
        if (product != null && startDate != null && endDate != null) {
            return stockRepository.findAllByProductAndIsDeleteFalseAndDateCreatedBetween(product, startDate, endDate, pageable)
                    .map(this::convertToDto);
        } else if (categoryId != null && startDate != null && endDate != null) {
            return stockRepository.findAllByProductCategoryIdAndIsDeleteFalseAndDateCreatedBetween(categoryId, startDate, endDate, pageable)
                    .map(this::convertToDto);
        } else if (categoryId != null) {
            return stockRepository.findAllByProductCategoryIdAndIsDeleteFalse(categoryId, pageable)
                    .map(this::convertToDto);
        } else {
            return Page.empty(pageable);
        }
    }

    @Transactional
    public StockDto editStock(StockDto request, Integer id) {
        Stock stock = stockRepository.findById(id).orElseThrow(() -> new RuntimeException("Error"));
        // dejemos que se pueda editar everything
//        stock.setInitialStock(request.getInitialStock());
        stock.setFinalStock(request.getFinalStock());
        stock.setCounterReposition(request.getCounterReposition());
        stock.setRegisterReposition(request.getRegisterReposition());
        stock.setDateUpdated(new DateUtil().utilDateNow());

        if (request.getCurrentCounterStock() + request.getCurrentRegisterStock() != request.getCurrentStock()) {
            throw new RuntimeException("El stock de caja y el de mostrador no coinciden con el general");
        }

        // TODO: Revisar esta edición, los valores iniciales no se modifican para eso estan las cuentas
        // los valores iniciales no los toco,
//        stock.(request.getGeneralStock());
//        stock.setInitialCounterStock(request.getCurrentCounterStock());
//        stock.setInitialRegisterStock(request.getCurrentRegisterStock());

        stockRepository.save(stock);

        return modelMapper.map(stock, StockDto.class);
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
