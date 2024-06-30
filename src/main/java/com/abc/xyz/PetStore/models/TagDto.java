package com.abc.xyz.PetStore.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class TagDto {

    private Integer id;
    private String name;

    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true; // reflexive - an object must equal itself
        if (obj == null || getClass() != obj.getClass())
            return false; // check if the obj instance does not belong to the exact same class 'TagDto'

        TagDto other = (TagDto) obj;
        return Objects.equals(
                name != null ? name.toLowerCase() : null,
                other.getName() != null ? other.getName().toLowerCase() : null
        );

    }

    @Override
    public int hashCode() {
        return Objects.hash(name != null ? name.toLowerCase() : null); // hashcode of 'null' is always 0 across all the Java implementations
    }
}
