package com.personal.iphonehouse.services;

import com.personal.iphonehouse.dtos.ProductSaleDto;
import com.personal.iphonehouse.dtos.SaleDto;
import com.personal.iphonehouse.dtos.StockDto;
import com.personal.iphonehouse.models.Product;
import com.personal.iphonehouse.models.ProductSale;
import com.personal.iphonehouse.models.Sale;
import com.personal.iphonehouse.models.Stock;
import com.personal.iphonehouse.repositories.ProductSaleRepository;
import com.personal.iphonehouse.repositories.SaleRepository;
import com.personal.iphonehouse.utils.DateUtil;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaleService {
    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private StockService stockService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private ProductSaleRepository productSaleRepository;

    @Transactional
    public SaleDto saveSale(SaleDto request) {
        int saleNumber = saleRepository.getSaleOrder();
        Sale sale = modelMapper.map(request, Sale.class);
        sale.setSaleCount(saleNumber + 1);
        // ahora tengo que hacer una lógica donde actualizo el stock en base a la venta de un producto
        // 1- Busco al stock del día de hoy según por product que quiera vender
        // 2- Según la cantidad de registerQuantity o counterQuantity, tengo que sumarle al stock
        // en registerSales o counterSales y guardar.

        impactStockOnSaleOperation(sale, true);

        Sale savedSale = saleRepository.save(sale);

        return this.convertToDto(savedSale);
    }

    public Page<SaleDto> getSales(String search, LocalDate startDate, LocalDate endDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        LocalDateTime startOfDay = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime endOfDay = endDate != null ? endDate.atTime(LocalTime.MAX) : null;

        Page<Sale> salesPage;

        if (startOfDay == null && endOfDay == null) {
            salesPage = saleRepository.findSalesBySearch(search.isEmpty() ? null : search, pageable);
        } else if (startOfDay != null && endOfDay == null) {
            salesPage = saleRepository.findSalesBySearchAndStartDate(search.isEmpty() ? null : search, startOfDay, pageable);
        } else if (startOfDay == null) {
            salesPage = saleRepository.findSalesBySearchAndEndDate(search.isEmpty() ? null : search, endOfDay, pageable);
        } else {
            salesPage = saleRepository.findSalesBySearchAndDateBetween(search.isEmpty() ? null : search, startOfDay, endOfDay, pageable);
        }

        List<SaleDto> salesDtoList = salesPage.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return new PageImpl<>(salesDtoList, pageable, salesPage.getTotalElements());
    }



    public int getTotalSalesByProduct(Product product) {
        List<ProductSale> productSales = productSaleRepository.findByProductAndIsDeleteFalse(product);
        return productSales.stream()
                .mapToInt(productSale -> productSale.getRegisterQuantity() + productSale.getCounterQuantity()).sum();
    }

    @Transactional
    public SaleDto deleteSale(Integer id) {
        Sale sale = saleRepository.findById(id).orElseThrow(() -> new RuntimeException("Error"));

        impactStockOnSaleOperation(sale, false);

        sale.setDelete(true);
        saleRepository.save(sale);

        return modelMapper.map(sale, SaleDto.class);

    }

    public SaleDto convertToDto(Sale sale) {
        SaleDto saleDto = modelMapper.map(sale, SaleDto.class);
        saleDto.setId(sale.getId());

        int totalProducts = 0;
        int totalSoldProducts = 0;
        for (ProductSaleDto productSale : saleDto.getProductSales()) {
            totalProducts += 1;
            totalSoldProducts += productSale.getTotalQuantity();
        }

        saleDto.setTotalProducts(totalProducts);
        saleDto.setTotalSoldProducts(totalSoldProducts);
        return saleDto;
    }

    public void impactStockOnSaleOperation(Sale sale, boolean isAddition) {
        for (ProductSale productSale : sale.getProductSales()) {
            StockDto stock = stockService.getStocksByDateTodayAndProduct(productSale.getProduct());
            // getProduct nunca va a ser tester, siempre va a ser un producto normal.
            if (productSale.getTesterProduct() == null) {
                if (productSale.getCounterQuantity() > 0) {
                    if (isAddition && productSale.getCounterQuantity() > stock.getCurrentCounterStock()) {
                        throw new RuntimeException("Stock insuficiente");
                    }


                    int newCounterSales = isAddition
                            ? stock.getCounterSales() + productSale.getCounterQuantity()
                            : stock.getCounterSales() - productSale.getCounterQuantity();
                    stock.setCounterSales(newCounterSales);
                }

                if (productSale.getRegisterQuantity() > 0) {
                    if (isAddition && productSale.getRegisterQuantity() > stock.getCurrentRegisterStock()) {
                        throw new RuntimeException("Stock insuficiente");
                    }

                    int newRegisterSales = isAddition
                            ? stock.getRegisterSales() + productSale.getRegisterQuantity()
                            : stock.getRegisterSales() - productSale.getRegisterQuantity();
                    stock.setRegisterSales(newRegisterSales);
                }

                stockService.editStock(stock, stock.getId());
                notificationService.sendStockUpdateNotification(productSale.getProduct().getId(), stock.getCurrentStock(), stock.getCurrentRegisterStock(), stock.getCurrentCounterStock());

            } else {
                StockDto testerStock = stockService.getStocksByDateTodayAndProduct(productSale.getTesterProduct());

                if (productSale.getCounterQuantity() > 0) {
                    // Operaciones sobre testerStock y stock dependiendo del parámetro isAddition
                    if (isAddition && productSale.getCounterQuantity() > stock.getCurrentCounterStock()) {
                        throw new RuntimeException("Stock insuficiente");
                    }

                    int newCounterReposition = isAddition
                            ? testerStock.getCounterReposition() + productSale.getCounterQuantity()
                            : testerStock.getCounterReposition() - productSale.getCounterQuantity();
                    testerStock.setCounterReposition(newCounterReposition);

                    int newCounterTransfersToTester = isAddition
                            ? stock.getCounterTransfersToTester() + productSale.getCounterQuantity()
                            : stock.getCounterTransfersToTester() - productSale.getCounterQuantity();
                    stock.setCounterTransfersToTester(newCounterTransfersToTester);
                }

                if (productSale.getRegisterQuantity() > 0) {
                    // Operaciones sobre testerStock y stock dependiendo del parámetro isAddition
                    if (isAddition && productSale.getRegisterQuantity() > stock.getCurrentRegisterStock()) {
                        throw new RuntimeException("Stock insuficiente");
                    }

                    int newRegisterReposition = isAddition
                            ? testerStock.getRegisterReposition() + productSale.getRegisterQuantity()
                            : testerStock.getRegisterReposition() - productSale.getRegisterQuantity();
                    testerStock.setRegisterReposition(newRegisterReposition);

                    int newRegisterTransfersToTester = isAddition
                            ? stock.getRegisterTransfersToTester() + productSale.getRegisterQuantity()
                            : stock.getRegisterTransfersToTester() - productSale.getRegisterQuantity();
                    stock.setRegisterTransfersToTester(newRegisterTransfersToTester);
                }

                stockService.editStock(stock, stock.getId());
                stockService.editStock(testerStock, testerStock.getId());

                notificationService.sendStockUpdateNotification(productSale.getProduct().getId(), stock.getCurrentStock(), stock.getCurrentRegisterStock(), stock.getCurrentCounterStock());
            }
            if (!isAddition) {
                productSale.setDelete(true);
                productSaleRepository.save(productSale);
            }
        }
    }

}
