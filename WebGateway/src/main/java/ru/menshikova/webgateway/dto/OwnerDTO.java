package ru.menshikova.webgateway.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class OwnerDTO {
    private Long id;
    private String name;
    private LocalDate birthDate;
    private Set<Long> catIds = new HashSet<>();
}