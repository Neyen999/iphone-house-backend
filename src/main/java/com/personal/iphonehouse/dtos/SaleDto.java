package com.personal.iphonehouse.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaleDto {
    private Integer id;
    private List<ProductSaleDto> productSales;
    private String name;
    private int totalProducts;
    private int totalSoldProducts;
}
