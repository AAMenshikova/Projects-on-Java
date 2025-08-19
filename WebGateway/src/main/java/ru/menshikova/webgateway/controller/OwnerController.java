package ru.menshikova.webgateway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.menshikova.webgateway.config.RabbitConfig;
import ru.menshikova.webgateway.dto.OwnerDTO;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/owners")
public class OwnerController {

    @Autowired
    private RabbitTemplate rt;

    @Operation(security = @SecurityRequirement(name = "basicAuth", scopes = {"ROLE_ADMIN"}))
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public Page<OwnerDTO> listOwners(Pageable pageable) {
        Map<String, Integer> req = Map.of(
                "page", pageable.getPageNumber(),
                "size", pageable.getPageSize()
        );
        @SuppressWarnings("unchecked")
        Page<OwnerDTO> page = (Page<OwnerDTO>) rt.convertSendAndReceive(
                RabbitConfig.OWNER_LIST_QUEUE, req
        );
        return page;
    }

    @Operation(security = @SecurityRequirement(name = "basicAuth", scopes = {"ROLE_ADMIN","ROLE_USER"}))
    @PreAuthorize("hasRole('ADMIN') or @sec.IsCurrent(authentication, #id)")
    @GetMapping("/{id}")
    public ResponseEntity<OwnerDTO> getById(@PathVariable Long id) {
        OwnerDTO dto = (OwnerDTO) rt.convertSendAndReceive(
                RabbitConfig.OWNER_GET_QUEUE, id
        );
        return dto != null
                ? ResponseEntity.ok(dto)
                : ResponseEntity.notFound().build();
    }

    @Operation(security = @SecurityRequirement(name = "basicAuth", scopes = {"ROLE_ADMIN"}))
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(params = "name")
    public ResponseEntity<List<OwnerDTO>> listOwnersByName(@RequestParam String name) {
        @SuppressWarnings("unchecked")
        List<OwnerDTO> list = (List<OwnerDTO>) rt.convertSendAndReceive(
                RabbitConfig.OWNER_FIND_BY_NAME_QUEUE, name
        );
        return list.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(list);
    }

    @Operation(security = @SecurityRequirement(name = "basicAuth", scopes = {}))
    @PostMapping
    public ResponseEntity<OwnerDTO> create(@RequestBody @Valid OwnerDTO dto) {
        OwnerDTO created = (OwnerDTO) rt.convertSendAndReceive(
                RabbitConfig.OWNER_CREATE_QUEUE, dto
        );
        return ResponseEntity
                .created(URI.create("/api/owners/" + created.getId()))
                .body(created);
    }

    @Operation(security = @SecurityRequirement(name = "basicAuth", scopes = {"ROLE_ADMIN","ROLE_USER"}))
    @PreAuthorize("hasRole('ADMIN') or @sec.IsCurrent(authentication, #id)")
    @PutMapping("/{id}")
    public ResponseEntity<OwnerDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid OwnerDTO dto
    ) {
        Map<String, Object> req = new HashMap<>();
        req.put("id", id);
        req.put("dto", dto);
        OwnerDTO updated = (OwnerDTO) rt.convertSendAndReceive(
                RabbitConfig.OWNER_UPDATE_QUEUE, req
        );
        return updated != null
                ? ResponseEntity.ok(updated)
                : ResponseEntity.notFound().build();
    }

    @Operation(security = @SecurityRequirement(name = "basicAuth", scopes = {"ROLE_ADMIN","ROLE_USER"}))
    @PreAuthorize("hasRole('ADMIN') or @sec.IsCurrent(authentication, #id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Boolean ok = (Boolean) rt.convertSendAndReceive(
                RabbitConfig.OWNER_DELETE_QUEUE, id
        );
        return Boolean.TRUE.equals(ok)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}