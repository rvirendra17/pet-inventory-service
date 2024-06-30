package com.abc.xyz.PetStore.entities;

import com.abc.xyz.PetStore.converters.StringCollectionConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "PET")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_pk_id", nullable = false)
    private Integer id;

    @Column(name = "pet_id")
    private Integer petId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_pk_id")
    private Category category;

    @Column(nullable = false, name = "name")
    private String name;


    /*

    converting the collection of string to a delimited string to persist in a column.
    The collection size and growth in future should be evaluated so that column size
    should not impact functionality.

    */

    @Convert(converter = StringCollectionConverter.class)
    @Column(nullable = false, name = "photo_urls")
    private Set<String> photoUrls = new LinkedHashSet<>();

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    private Set<Tag> tags = new HashSet<>();

    @Column(name = "status")
    private String status;

}
