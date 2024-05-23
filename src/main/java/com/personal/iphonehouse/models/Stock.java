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
@Table(name = "stocks")
public class Stock extends EntityBase<Integer> {
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
    @ManyToOne
    private Product product;

    /*
    * Todos los dias por la mañana se corre un runner que crea un nuevo stock para cada producto
    * pero con la fecha actual,
    * la cantidad final del dia anterior es la cantidad inicial de hoy
    * counterStock y registerStock quedan con los mismos valores del final del dia anterior a menos que alguien los modifique
    * reposición inicia en 0
    * */
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
