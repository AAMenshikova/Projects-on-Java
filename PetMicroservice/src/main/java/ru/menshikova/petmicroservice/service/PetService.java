package ru.menshikova.petmicroservice.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.menshikova.petmicroservice.dto.PetDTO;
import ru.menshikova.petmicroservice.model.Color;
import ru.menshikova.petmicroservice.model.Pet;
import ru.menshikova.petmicroservice.repository.PetRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PetService {
    private final PetRepository petRepository;

    @Autowired
    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public PetDTO toDTO(Pet pet){
        PetDTO petDTO = new PetDTO();
        petDTO.setBirthDate(pet.getBirthDate());
        petDTO.setBreed(pet.getBreed());
        petDTO.setColor(pet.getColor());
        petDTO.setName(pet.getName());
        petDTO.setOwnerId(pet.getOwnerId());
        petDTO.setId(pet.getId());
        petDTO.setFriends(pet.getFriends().stream().map(Pet::getId).collect(Collectors.toSet()));
        petDTO.setTailLength(pet.getTailLength());
        return petDTO;
    }

    public Pet fromDTO(PetDTO dto) {
        Pet pet = new Pet();
        pet.setName(dto.getName());
        pet.setBirthDate(dto.getBirthDate());
        pet.setBreed(dto.getBreed());
        pet.setColor(dto.getColor());
        pet.setTailLength(dto.getTailLength());
        pet.setOwnerId(dto.getOwnerId());
        if (dto.getFriends() != null) {
            Set<Pet> friends = dto.getFriends().stream()
                    .map(fid -> petRepository.findById(fid)
                            .orElseThrow(() -> new EntityNotFoundException(
                                    "Friend pet not found with id=" + fid)))
                    .collect(Collectors.toSet());
            pet.setFriends(friends);
        }
        return pet;
    }

    public Page<PetDTO> getAllPets(int page, int size) {
        Pageable pg = PageRequest.of(page, size);
        Page<Pet> p = petRepository.findAll(pg);
        List<PetDTO> dtos = p.getContent().stream().map(this::toDTO).collect(Collectors.toList());
        return new PageImpl<>(dtos, pg, p.getTotalElements());
    }

    public Optional<PetDTO> getById(Long id) {
        return petRepository.findById(id).map(this::toDTO);
    }

    public List<PetDTO> getByName(String name) {
        List<Pet> pets = petRepository.findByName(name);
        return pets.stream().map(this::toDTO).toList();
    }

    public List<PetDTO> getByBreed(String breed) {
        List<Pet> pets = petRepository.findByBreed(breed);
        return pets.stream().map(this::toDTO).toList();
    }

    public List<PetDTO> getByOwnerId(Long id) {
        List<Pet> pets = petRepository.findByOwnerId(id);
        return pets.stream().map(this::toDTO).toList();
    }

    public Page<PetDTO> getByColor(Color c, int page, int size) {
        Pageable pg = PageRequest.of(page, size);
        Page<Pet> p = petRepository.findByColor(c, pg);
        List<PetDTO> dtos = p.getContent().stream().map(this::toDTO).toList();
        return new PageImpl<>(dtos, pg, p.getTotalElements());
    }

    public PetDTO create(PetDTO dto) {
        Pet pet = fromDTO(dto);
        Pet saved = petRepository.save(pet);
        return toDTO(saved);
    }

    public Optional<PetDTO> update(Long id, PetDTO dto) {
        return petRepository.findById(id)
                .map(existing -> {
                    existing.setName(dto.getName());
                    existing.setBirthDate(dto.getBirthDate());
                    existing.setBreed(dto.getBreed());
                    existing.setColor(dto.getColor());
                    existing.setTailLength(dto.getTailLength());
                    Pet updated = petRepository.save(existing);
                    return toDTO(updated);
                });
    }

    public boolean delete(Long id) {
        if (!petRepository.existsById(id)) {
            return false;
        }
        petRepository.deleteById(id);
        return true;
    }
}
