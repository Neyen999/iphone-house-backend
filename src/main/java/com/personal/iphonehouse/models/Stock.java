package com.personal.iphonehouse.models;

import jakarta.persistence.Column;
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
    @Column(columnDefinition = "integer")
    private int idealStock;
    @Column(columnDefinition = "integer")
    private int initialStock; // el general, preguntar si es la suma de todos o tienen separado
    @Column(columnDefinition = "integer")
    private int initialCounterStock; // el de mostrador
    @Column(columnDefinition = "integer")
    private int initialRegisterStock; // el de caja
    @Column(columnDefinition = "integer")
    private int registerSales;
    @Column(columnDefinition = "integer")
    private int counterSales;
    @Column(columnDefinition = "integer")
    private int counterReposition; // reposición, esto se agrega y quiza se le suma a alguno de los 3 anteriores?
    @Column(columnDefinition = "integer")
    private int registerReposition; // reposición, esto se agrega y quiza se le suma a alguno de los 3 anteriores?
    @Column(columnDefinition = "integer")
    private int registerTransfersToTester; // Prestaciones de caja al stock de tester
    @Column(columnDefinition = "integer")
    private int counterTransfersToTester; // Prestaciones de mostrador al stock de tester
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
        int totalRepositions = counterReposition + registerReposition;
        int totalTransfersToTester = registerTransfersToTester + counterTransfersToTester;

        return initialStock - totalSales + totalRepositions - totalTransfersToTester;
    }

    // Getter for currentRegisterStock
    public int getCurrentRegisterStock() {
        int registerRepositions = this.registerReposition;
        return initialRegisterStock - registerSales + registerRepositions - registerTransfersToTester;
    }

    // Getter for currentCounterStock
    public int getCurrentCounterStock() {
        int counterRepositions = this.counterReposition;
        return initialCounterStock - counterSales + counterRepositions - counterTransfersToTester;
    }
}
