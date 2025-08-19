package ru.menshikova.ownermicroservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.menshikova.ownermicroservice.dto.OwnerDTO;
import ru.menshikova.ownermicroservice.model.Owner;
import ru.menshikova.ownermicroservice.repository.OwnerRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OwnerService {
    private final OwnerRepository ownerRepository;

    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public OwnerDTO toDTO(Owner owner) {
        OwnerDTO ownerDTO = new OwnerDTO();
        ownerDTO.setBirthDate(owner.getBirthDate());
        ownerDTO.setCatIds(owner.getCatIds());
        ownerDTO.setId(owner.getId());
        ownerDTO.setName(owner.getName());
        return ownerDTO;
    }

    public Owner fromDTO(OwnerDTO dto) {
        Owner owner = new Owner();
        owner.setName(dto.getName());
        owner.setBirthDate(dto.getBirthDate());
        owner.setCatIds(dto.getCatIds());
        return owner;
    }

    public Page<OwnerDTO> getAllOwners(int page, int size) {
        Pageable pg = PageRequest.of(page, size);
        Page<Owner> p = ownerRepository.findAll(pg);
        List<OwnerDTO> dtos = p.getContent().stream().map(this::toDTO).collect(Collectors.toList());
        return new PageImpl<>(dtos, pg, p.getTotalElements());
    }

    public Optional<OwnerDTO> getById(Long id) {
        return ownerRepository.findById(id).map(this::toDTO);
    }

    public List<OwnerDTO> getByName(String name) {
        List<Owner> owners = ownerRepository.findByName(name);
        return owners.stream().map(this::toDTO).toList();
    }

    public OwnerDTO create(OwnerDTO dto) {
        Owner owner = fromDTO(dto);
        return toDTO(ownerRepository.save(owner));
    }

    public Optional<OwnerDTO> update(Long id, OwnerDTO dto) {
        return ownerRepository.findById(id).map(existing -> {
            existing.setName(dto.getName());
            existing.setBirthDate(dto.getBirthDate());
            return toDTO(ownerRepository.save(existing));
        });
    }

    public boolean delete(Long id) {
        if (!ownerRepository.existsById(id)) return false;
        ownerRepository.deleteById(id);
        return true;
    }
}
