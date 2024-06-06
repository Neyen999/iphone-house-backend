package com.personal.iphonehouse.services;

import com.personal.iphonehouse.dtos.CategoryDto;
import com.personal.iphonehouse.dtos.ProductDto;
import com.personal.iphonehouse.dtos.ProductSimpleDto;
import com.personal.iphonehouse.dtos.StockDto;
import com.personal.iphonehouse.models.Category;
import com.personal.iphonehouse.models.Product;
import com.personal.iphonehouse.models.Stock;
import com.personal.iphonehouse.repositories.CategoryRepository;
import com.personal.iphonehouse.repositories.ProductRepository;
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
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private StockService stockService;
    @Autowired
    private SaleService saleService;

    @Transactional
    public ProductDto saveProduct(ProductDto request) {
        if (productRepository.existsByNameIgnoreCase(request.getName()))
            throw new RuntimeException("Producto existente");
        Product product = modelMapper.map(request, Product.class);

        productRepository.save(product);

        ProductDto response = modelMapper.map(product, ProductDto.class);
        // ahora guardo el stock inicial
        StockDto stockRequest = new StockDto();
        stockRequest.setIdealStock(request.getIdealStock());
        stockRequest.setInitialStock(request.getInitialStock());
        stockRequest.setInitialRegisterStock(request.getInitialRegisterStock());
        stockRequest.setInitialCounterStock(request.getInitialCounterStock());
        stockRequest.setProduct(new ProductSimpleDto(product.getId(), product.getName(), modelMapper.map(product.getCategory(), CategoryDto.class)));
        stockRequest.setTester(request.isTester());

        stockService.saveStock(stockRequest);

        return response;
    }

    public List<ProductDto> saveRegularAndTesterProduct(List<ProductDto> request) {
        // solo deberia haber 2
        if (request.size() > 2) {
            throw new RuntimeException("Error");
        }

        return request
                .stream()
                .map(this::saveProduct)
                .collect(Collectors.toList());

    }

    public ProductDto getProductById(Integer id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Error"));

        return modelMapper.map(product, ProductDto.class);
    }

    public List<ProductDto> getAllProducts(String search) {
        List<Product> products = productRepository.findBySearchAndIsDeleteFalseAndDateBetween(search);

        // todo:
        //  el frontend solo en el guardar te da la opción de cargar una cantidad de productos que impactan en el stock

        return products.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductDto editProduct(ProductDto request, Integer id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Error"));
        Product testerProduct = null;
        Category category = null;

        if (productRepository.existsByNameIgnoreCase(request.getName())) {
            throw new RuntimeException("Nombre en uso");
        }

        if (!request.getCategory().getId().equals(product.getCategory().getId())) {
            category = categoryRepository.findById(request.getCategory().getId()).orElseThrow(() -> new RuntimeException("Error"));
            product.setCategory(category);
        }

        product.setName(request.getName());
        product.setDateUpdated(new DateUtil().utilDateNow());

        productRepository.save(product);

        if (productRepository.existsByNameIgnoreCase(product.getName() + "(TESTER)")) {
            testerProduct = productRepository
                    .findByNameEqualsIgnoreCase(product.getName() + "(TESTER)").orElseThrow(() -> new RuntimeException("Error"));
        }

        if (testerProduct != null) {
            testerProduct.setName(request.getName());
            testerProduct.setCategory(category);
            testerProduct.setDateUpdated(new DateUtil().utilDateNow());
            productRepository.save(testerProduct);
        }

        return modelMapper.map(product, ProductDto.class);
    }

    @Transactional
    public void deleteProduct(String name) {
        Product product = productRepository.findByNameEqualsIgnoreCase(name).orElseThrow(() -> new RuntimeException("Error"));
        Product productTester = productRepository.findByNameEqualsIgnoreCase(name + "(TESTER)").orElseThrow(() -> new RuntimeException("Error"));


        stockService.deleteStocksByProduct(stockService.stocksByProduct(product));
        stockService.deleteStocksByProduct(stockService.stocksByProduct(productTester));
        // marco como eliminados todos los stocks que tengan ese id de product
        product.setDelete(true);
        productTester.setDelete(true);

        productRepository.save(product);
        productRepository.save(productTester);
    }

    public ProductDto convertToDto(Product product) {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        productDto.setTester(product.getName().contains("(TESTER)"));

        // la cantidad disponible la saco del stock
        StockDto stock = stockService.getStocksByDateTodayAndProduct(product);
        int totalSold = saleService.getTotalSalesByProduct(product);

        if (stock != null)
            productDto.setAvaiableQuantity(stock.getCurrentStock());

        productDto.setTotalSold(totalSold);

        return productDto;
    }
}
