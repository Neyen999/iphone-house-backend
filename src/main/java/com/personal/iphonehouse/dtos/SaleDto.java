package com.personal.iphonehouse.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaleDto {
    private Integer id;
    private List<ProductSaleDto> productSales;
    private int totalProducts;
    private int totalSoldProducts;
    private String userName;
    private String userPhoneNumber;
    private int userPayment;
    private int totalPrice;
    private int totalChange;
    private boolean testerSale;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDateTime dateCreated;
    private int saleCount;
}
