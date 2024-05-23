package com.personal.iphonehouse.services;

import com.personal.iphonehouse.dtos.CategoryDto;
import com.personal.iphonehouse.dtos.ProductDto;
import com.personal.iphonehouse.models.Product;
import com.personal.iphonehouse.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public ProductDto saveProduct(ProductDto request) {
        if (productRepository.existsByNameIgnoreCase(request.getName()))
            throw new RuntimeException("Producto existente");
        Product product = modelMapper.map(request, Product.class);

        return modelMapper.map(product, ProductDto.class);
    }

    public ProductDto getProductById(Integer id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Error"));

        return modelMapper.map(product, ProductDto.class);
    }

    public List<ProductDto> getAllProducts(String category) {
        List<Product> products;

        if (category == null || category.isEmpty()) {
            products = productRepository.findAllAndIsDeleteFalse();
        } else {
            products = productRepository.findByDeletedFalseAndCategoryName(category);
        }

        // todo:
        //  el frontend solo en el guardar te da la opción de cargar una cantidad de productos que impactan en el stock

        return products.stream()
                .map(product -> new ProductDto(product.getId(),
                        product.getName(),
                        modelMapper.map(product.getCategory(), CategoryDto.class), product.getPrice()))
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductDto editProduct(ProductDto request, Integer id) {
        // todo:
        //  solo editamos precio, imagen, descripción etc, el frontend no tiene que poder dar la opción de agregar
        // lo desarrollamos despues
        return new ProductDto();
    }

    @Transactional
    public void deleteProduct(Integer id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Error"));

        product.setDelete(true);

        productRepository.save(product);
    }
}
