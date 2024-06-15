
package com.personal.iphonehouse.models;

import jakarta.persistence.*;

@Entity
@Table(name = "stores")
public class Store {
    @Id
    private Long id;
    // Otros campos...
}
