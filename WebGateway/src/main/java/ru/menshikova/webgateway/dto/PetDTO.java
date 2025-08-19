package ru.menshikova.webgateway.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
public class PetDTO {
    private Long id;
    private String name;
    private LocalDate birthDate;
    private String breed;
    private Integer tailLength;
    private Color color;
    private Set<Long> friends = new HashSet<>();
    private Long ownerId;
}