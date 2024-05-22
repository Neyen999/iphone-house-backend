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
//    private Category category;
    private int price;
//    private File file;
}
