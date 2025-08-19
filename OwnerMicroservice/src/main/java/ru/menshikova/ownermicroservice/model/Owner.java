package ru.menshikova.ownermicroservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.*;

@Entity
@Setter
@Getter
@Table(name = "owners")
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private LocalDate birthDate;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Getter
    @Setter
    @ElementCollection
    @CollectionTable(
            name = "owner_cat_ids",
            joinColumns = @JoinColumn(name = "owner_id")
    )
    @Column(name = "cat_id")
    private Set<Long> catIds = new HashSet<>();
}