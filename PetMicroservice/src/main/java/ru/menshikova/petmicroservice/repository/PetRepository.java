package ru.menshikova.petmicroservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.menshikova.petmicroservice.model.Color;
import ru.menshikova.petmicroservice.model.Pet;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByName(String name);
    List<Pet> findByBreed(String breed);
    List<Pet> findByOwnerId(Long ownerId);
    Page<Pet> findByColor(Color color, Pageable pageable);
}
