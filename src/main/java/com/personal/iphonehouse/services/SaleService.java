package com.personal.iphonehouse.services;

import com.personal.iphonehouse.dtos.ProductSaleDto;
import com.personal.iphonehouse.dtos.SaleDto;
import com.personal.iphonehouse.dtos.StockDto;
import com.personal.iphonehouse.models.Product;
import com.personal.iphonehouse.models.ProductSale;
import com.personal.iphonehouse.models.Sale;
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
    private ProductSaleRepository productSaleRepository;

    @Transactional
    public SaleDto saveSale(SaleDto request) {
        Sale sale = modelMapper.map(request, Sale.class);
        // ahora tengo que hacer una lógica donde actualizo el stock en base a la venta de un producto
        // 1- Busco al stock del día de hoy según por product que quiera vender
        // 2- Según la cantidad de registerQuantity o counterQuantity, tengo que sumarle al stock
        // en registerSales o counterSales y guardar.

        for (ProductSale productSale : sale.getProductSales()) {
            StockDto stock = stockService.getStocksByDateTodayAndProduct(productSale.getProduct());
            // getProduct nunca va a ser tester, siempre va a ser un producto normal.
            if (productSale.getTesterProduct() == null) {

                if (productSale.getCounterQuantity() > 0) {
                    int prevSaleQuantityPlusNew = stock.getCounterSales() + productSale.getCounterQuantity();
                    stock.setCounterSales(prevSaleQuantityPlusNew);
                }

                if (productSale.getRegisterQuantity() > 0) {
                    int prevSaleQuantityPlusNew = stock.getRegisterSales() + productSale.getRegisterQuantity();
                    stock.setRegisterSales(prevSaleQuantityPlusNew);
                }

                stockService.editStock(stock, stock.getId());
            } else {
                StockDto testerStock = stockService.getStocksByDateTodayAndProduct(productSale.getTesterProduct());

                if (productSale.getCounterQuantity() > 0) {
                    // agrego la cantidad como reposicion para tester
                    int prevCounterRepositionValuePlusNew = testerStock.getCounterReposition() + productSale.getCounterQuantity();
                    testerStock.setCounterReposition(prevCounterRepositionValuePlusNew);

                    // agrego las transferencias a tester
                    int prevCounterTransferPlusNew = stock.getCounterTransfersToTester() + productSale.getCounterQuantity();
                    stock.setCounterTransfersToTester(prevCounterTransferPlusNew);
                }

                if (productSale.getRegisterQuantity() > 0) {
                    // agrego la cantidad como reposicion para tester
                    int prevRegisterRepositionValuePlusNew = testerStock.getRegisterReposition() + productSale.getRegisterQuantity();
                    testerStock.setRegisterReposition(prevRegisterRepositionValuePlusNew);

                    // agrego las transferencias a tester
                    int prevRegisterTransferPlusNew = stock.getRegisterTransfersToTester() + productSale.getRegisterQuantity();
                    stock.setRegisterTransfersToTester(prevRegisterTransferPlusNew);
                }

                stockService.editStock(stock, stock.getId());
                stockService.editStock(testerStock, testerStock.getId());
            }

        }

        saleRepository.save(sale);

        return this.convertToDto(sale);
    }

//    public Page<SaleDto> getSales(String search, Date desiredDate, int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//
//        Page<Sale> salesPage;
//        if (desiredDate == null) {
//            // Si no se proporciona fecha, se buscarán todas las ventas ordenadas de más reciente a menos reciente
////            salesPage = saleRepository.findSalesBySearchAndDateBetweenAndTesterSaleFalse(search, null, null, pageable);
//            salesPage = saleRepository.findSalesBySearchAndDateBetweenAndTesterSaleFalse(search, null, pageable);
//        } else {
//            // Si se proporciona una fecha, se buscarán las ventas solo de esa fecha
////            LocalDate localDesiredDate = desiredDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
////            LocalDate startDate = localDesiredDate.minusDays(1);
////            LocalDate endDate = localDesiredDate.plusDays(1);
//
////            Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
////            Date end = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
//
//            salesPage = saleRepository.findSalesBySearchAndDateBetweenAndTesterSaleFalse(search, desiredDate, pageable);
//        }
//
//        List<SaleDto> salesDtoList = salesPage.stream()
//                .map(this::convertToDto)
//                .collect(Collectors.toList());
//
//        return new PageImpl<>(salesDtoList, pageable, salesPage.getTotalElements());
//    }
    public Page<SaleDto> getSales(String search, Date startDate, Date endDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Sale> salesPage = saleRepository.findSalesBySearchAndDateBetweenAndTesterSaleFalse(search, startDate, endDate, pageable);

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

    public SaleDto convertToDto(Sale sale) {
        SaleDto saleDto = modelMapper.map(sale, SaleDto.class);
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
}
