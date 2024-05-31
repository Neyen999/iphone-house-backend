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
        LocalDate today = LocalDate.now(ZoneId.of("America/Argentina/Buenos_Aires"));
//        Date now = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());

        for (ProductSale productSale : sale.getProductSales()) {
            StockDto stock = stockService.getStocksByDateTodayAndProduct(productSale.getProduct());
            if (productSale.getCounterQuantity() > 0) {
                int prevSaleQuantityPlusNew = stock.getCounterSales() + productSale.getCounterQuantity();
                stock.setCounterSales(prevSaleQuantityPlusNew);
            }

            if (productSale.getRegisterQuantity() > 0) {
                int prevSaleQuantityPlusNew = stock.getRegisterSales() + productSale.getRegisterQuantity();
                stock.setRegisterSales(prevSaleQuantityPlusNew);
            }

            stockService.editStock(stock, stock.getId());
        }

        saleRepository.save(sale);

        return this.convertToDto(sale);
    }

    public List<SaleDto> getSales(String search, Date desiredDate) {
//        Pageable pageable = PageRequest.of(page, size);

        if (desiredDate == null) {
            desiredDate = new DateUtil().utilDateNow(); // Usa la fecha actual si no se proporciona una
        }

        LocalDate localDesiredDate = desiredDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate startDate = localDesiredDate.minusDays(1);
        LocalDate endDate = localDesiredDate.plusDays(1);

        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<Sale> salesPage = saleRepository.findSalesBySearchAndDateBetween(search,
                start,
                end);

        return salesPage.stream()
                .map(this::convertToDto).collect(Collectors.toList());
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
