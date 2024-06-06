package com.personal.iphonehouse.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductSaleDto {
    private Integer id;
    private ProductSimpleDto product;
    private ProductSimpleDto testerProduct;
    private int totalQuantity;
    private int registerQuantity;
    private int counterQuantity;

    public int getTotalQuantity() {
        return registerQuantity + counterQuantity;
    }
}
