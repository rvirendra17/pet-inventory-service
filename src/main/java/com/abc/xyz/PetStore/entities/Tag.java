package com.abc.xyz.PetStore.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_pk_id", nullable = false)
    private Integer id;


    @Column(name = "tag_id")
    private Integer tagId;

    @Column(name = "name")
    private String name;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id") // it is optional otherwise it will be different name
    private Pet pet;

}
