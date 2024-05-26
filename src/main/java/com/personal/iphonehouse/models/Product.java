package com.personal.iphonehouse.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product extends EntityBase<Integer> {
    private String name;
    @ManyToOne
    private Category category;
    private Integer price;
//    private File file;
}
