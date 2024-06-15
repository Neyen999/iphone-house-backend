package com.personal.iphonehouse.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_sales")
public class ProductSale extends EntityBase<Integer> {
    @ManyToOne
    private Product product;
    // Producto de donde se ha sustraído el stock, en caso de que product sea tester
    @ManyToOne(optional = true)
    private Product testerProduct;
    private int registerQuantity;
    private int counterQuantity;
}
