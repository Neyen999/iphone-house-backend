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
@Table(name = "roles")
public class Role extends EntityBase<Integer> {
    @Column(columnDefinition = "VARCHAR(50)")
    private String name;
}
