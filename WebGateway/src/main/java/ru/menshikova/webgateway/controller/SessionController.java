package ru.menshikova.webgateway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    public SessionController(AuthenticationManager authenticationManager) {
    }

    @Operation(security = @SecurityRequirement(name = "basicAuth"))
    @PostMapping("/basic")
    public ResponseEntity<Void> login(HttpServletRequest servletRequest) {
        servletRequest.getSession(true);
        return ResponseEntity.ok().build();
    }

    @Operation(security = @SecurityRequirement(name = "basicAuth", scopes = {"ROLE_USER","ROLE_ADMIN"}))
    @GetMapping("/current")
    public ResponseEntity<Principal> current(Principal principal) {
        return ResponseEntity.ok(principal);
    }

    @Operation(security = @SecurityRequirement(name = "basicAuth"))
    @DeleteMapping
    public ResponseEntity<Void> logout(HttpServletRequest servletRequest) {
        HttpSession session = servletRequest.getSession(false);
        if (session != null) session.invalidate();
        SecurityContextHolder.clearContext();
        return ResponseEntity.noContent().build();
    }
}