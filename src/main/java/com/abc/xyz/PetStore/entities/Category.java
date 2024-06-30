package com.abc.xyz.PetStore.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_pk_id", nullable = false)
    private Integer id;

    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "name")
    private String name;

}
