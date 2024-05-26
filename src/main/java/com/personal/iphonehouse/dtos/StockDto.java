package com.personal.iphonehouse.dtos;

import com.personal.iphonehouse.models.Product;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockDto {
    private Integer id;
    private int idealStock;
    private int initialStock; // el general, preguntar si es la suma de todos o tienen separado
    private int initialCounterStock; // el de mostrador
    private int initialRegisterStock; // el de caja
    private int registerSales;
    private int counterSales;
    private Integer counterReposition; // reposición, esto se agrega y quiza se le suma a alguno de los 3 anteriores?
    private Integer registerReposition; // reposición, esto se agrega y quiza se le suma a alguno de los 3 anteriores?
    private Integer finalStock; // stock al final del día, la cantidad que habia menos las ventas
    private boolean tester;
//    private ProductDto product;
    private ProductSimpleDto product;

    // Getter for currentStock
    public int getCurrentStock() {
        int totalSales = registerSales + counterSales;
        int totalRepositions = (counterReposition != null ? counterReposition : 0) + (registerReposition != null ? registerReposition : 0);
        return initialStock - totalSales + totalRepositions;
    }

    // Getter for currentRegisterStock
    public int getCurrentRegisterStock() {
        int registerRepositions = this.registerReposition != null ? this.registerReposition : 0;
        return initialRegisterStock - registerSales + registerRepositions;
    }

    // Getter for currentCounterStock
    public int getCurrentCounterStock() {
        int counterRepositions = this.counterReposition != null ? this.counterReposition : 0;
        return initialCounterStock - counterSales + counterRepositions;
    }
}
