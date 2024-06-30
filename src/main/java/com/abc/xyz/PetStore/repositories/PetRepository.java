package com.abc.xyz.PetStore.repositories;

import com.abc.xyz.PetStore.entities.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PetRepository extends JpaRepository<Pet, Integer> {
    Optional<Pet> findByName(String name);

    Optional<List<Pet>> findByStatusIn(Set<String> status);

    Optional<Pet> findByPetId(Integer petId);

    void deleteByPetId(Integer petId);
}
