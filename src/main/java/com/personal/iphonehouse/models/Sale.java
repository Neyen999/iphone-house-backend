package com.personal.iphonehouse.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sales")
public class Sale extends EntityBase<Integer> {
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "sale_id")
    private List<ProductSale> productSales;
    private String userName;
    private String userPhoneNumber;
    @Column(columnDefinition = "integer")
    private int userPayment;
    @Column(columnDefinition = "integer")
    private int totalPrice;
    @Column(columnDefinition = "integer")
    private int totalChange;
    private boolean testerSale;
    @Column(columnDefinition = "integer")
    private int saleCount;
}
