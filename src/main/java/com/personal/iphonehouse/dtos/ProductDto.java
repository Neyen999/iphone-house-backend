package com.personal.iphonehouse.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Integer id;
    private String name;
    private CategoryDto category;
    private int idealStock;
    private int initialStock; // el general, preguntar si es la suma de todos o tienen separado
    private int initialCounterStock; // el de mostrador
    private int initialRegisterStock; // el de caja
    private boolean tester;
    private int availableQuantity;
    private int totalSold;
//    private StockDto stock;
}
