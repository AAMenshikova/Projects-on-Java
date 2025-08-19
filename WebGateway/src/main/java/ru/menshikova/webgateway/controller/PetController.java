package ru.menshikova.webgateway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import ru.menshikova.webgateway.config.RabbitConfig;
import ru.menshikova.webgateway.dto.PetDTO;
import ru.menshikova.webgateway.dto.Color;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/api/pets")
public class PetController {

    @Autowired
    private RabbitTemplate rt;

    @Operation(security = @SecurityRequirement(name = "basicAuth", scopes = {"ROLE_USER","ROLE_ADMIN"}))
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public Page<PetDTO> listPets(Pageable pageable) {
        Map<String,Object> req = new HashMap<>();
        req.put("page",  pageable.getPageNumber());
        req.put("size",  pageable.getPageSize());
        req.put("sort",  null);
        @SuppressWarnings("unchecked")
        Page<PetDTO> page = (Page<PetDTO>) rt.convertSendAndReceive(
                RabbitConfig.PET_LIST_QUEUE, req);
        return page;
    }

    @Operation(security = @SecurityRequirement(name = "basicAuth", scopes = {"ROLE_USER","ROLE_ADMIN"}))
    @PreAuthorize("isAuthenticated()")
    @GetMapping(params = "sortPetsByName")
    public ResponseEntity<Page<PetDTO>> sortPetsByName(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name"));
        Map<String,Object> req = new HashMap<>();
        req.put("page",  pageable.getPageNumber());
        req.put("size",  pageable.getPageSize());
        req.put("sort",  "name");
        @SuppressWarnings("unchecked")
        Page<PetDTO> result = (Page<PetDTO>) rt.convertSendAndReceive(
                RabbitConfig.PET_LIST_QUEUE, req);
        return result.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(result);
    }

    @Operation(security = @SecurityRequirement(name = "basicAuth", scopes = {"ROLE_USER","ROLE_ADMIN"}))
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<PetDTO> getPetById(@PathVariable Long id) {
        PetDTO dto = (PetDTO) rt.convertSendAndReceive(
                RabbitConfig.PET_GET_QUEUE, id);
        return dto != null
                ? ResponseEntity.ok(dto)
                : ResponseEntity.notFound().build();
    }

    @Operation(security = @SecurityRequirement(name = "basicAuth", scopes = {"ROLE_USER","ROLE_ADMIN"}))
    @PreAuthorize("isAuthenticated()")
    @GetMapping(params = "name")
    public ResponseEntity<List<PetDTO>> listPetsByName(@RequestParam String name) {
        @SuppressWarnings("unchecked")
        List<PetDTO> list = (List<PetDTO>) rt.convertSendAndReceive(
                RabbitConfig.PET_FIND_NAME_QUEUE, name);
        return list.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(list);
    }

    @Operation(security = @SecurityRequirement(name = "basicAuth", scopes = {"ROLE_USER","ROLE_ADMIN"}))
    @PreAuthorize("isAuthenticated()")
    @GetMapping(params = "breed")
    public ResponseEntity<List<PetDTO>> listPetsByBreed(@RequestParam String breed) {
        @SuppressWarnings("unchecked")
        List<PetDTO> list = (List<PetDTO>) rt.convertSendAndReceive(
                RabbitConfig.PET_FIND_BREED_QUEUE, breed);
        return list.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(list);
    }

    @Operation(security = @SecurityRequirement(name = "basicAuth", scopes = {"ROLE_USER","ROLE_ADMIN"}))
    @PreAuthorize("isAuthenticated()")
    @GetMapping(params = "ownerId")
    public ResponseEntity<List<PetDTO>> listPetsByOwnerId(@RequestParam Long ownerId) {
        @SuppressWarnings("unchecked")
        List<PetDTO> list = (List<PetDTO>) rt.convertSendAndReceive(
                RabbitConfig.PET_FIND_OWNER_QUEUE, ownerId);
        return list.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(list);
    }

    @Operation(security = @SecurityRequirement(name = "basicAuth", scopes = {"ROLE_USER","ROLE_ADMIN"}))
    @PreAuthorize("isAuthenticated()")
    @GetMapping(params = "color")
    public ResponseEntity<Page<PetDTO>> listPetsByColor(
            @RequestParam Color color, Pageable pageable) {
        Map<String,Object> req = new HashMap<>();
        req.put("color", color.name());
        req.put("page",  pageable.getPageNumber());
        req.put("size",  pageable.getPageSize());
        @SuppressWarnings("unchecked")
        Page<PetDTO> page = (Page<PetDTO>) rt.convertSendAndReceive(
                RabbitConfig.PET_FIND_COLOR_QUEUE, req);
        return page.hasContent()
                ? ResponseEntity.ok(page)
                : ResponseEntity.notFound().build();
    }

    @Operation(security = @SecurityRequirement(name = "basicAuth", scopes = {"ROLE_USER","ROLE_ADMIN"}))
    @PreAuthorize("hasRole('ADMIN') or @sec.isCurrent(authentication, #dto.ownerId)")
    @PostMapping
    public ResponseEntity<PetDTO> createPet(@RequestBody PetDTO dto) {
        PetDTO created = (PetDTO) rt.convertSendAndReceive(
                RabbitConfig.PET_CREATE_QUEUE, dto);
        return ResponseEntity
                .created(URI.create("/api/pets/" + created.getId()))
                .body(created);
    }

    @Operation(security = @SecurityRequirement(name = "basicAuth", scopes = {"ROLE_USER","ROLE_ADMIN"}))
    @PreAuthorize("hasRole('ADMIN') or @sec.isPetOwner(authentication, #id)")
    @PutMapping("/{id}")
    public ResponseEntity<PetDTO> updatePet(
            @PathVariable Long id,
            @RequestBody PetDTO dto) {
        dto.setId(id);
        PetDTO updated = (PetDTO) rt.convertSendAndReceive(
                RabbitConfig.PET_UPDATE_QUEUE, dto);
        return updated != null
                ? ResponseEntity.ok(updated)
                : ResponseEntity.notFound().build();
    }

    @Operation(security = @SecurityRequirement(name = "basicAuth", scopes = {"ROLE_USER","ROLE_ADMIN"}))
    @PreAuthorize("hasRole('ADMIN') or @sec.isPetOwner(authentication, #id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        Boolean deleted = (Boolean) rt.convertSendAndReceive(
                RabbitConfig.PET_DELETE_QUEUE, id);
        return Boolean.TRUE.equals(deleted)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}