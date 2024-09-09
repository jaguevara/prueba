package com.jag.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jag.demo.util.JwtUtil;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@RestController
public class AuthController {

    @Value("${user.username}")
    private String username;

    @Value("${user.password}")
    private String password;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authRequest) throws Exception {
        if (authRequest.getUsername().equals(username) && authRequest.getPassword().equals(password)) {
            final String jwt = jwtUtil.generateToken(authRequest.getUsername());
            return ResponseEntity.ok(new AuthResponse(jwt));
        } else {
            throw new Exception("Incorrect username or password");
        }
    }
}

class AuthRequest {
    @NotNull(message = "Nombre de usuario no puede ser vacio")
    @Getter @Setter 
    private String username;
    @NotNull(message = "Password no puede ser vacio")
    @Getter @Setter 
    private String password;

    // getters y setters
}

class AuthResponse {
    private final String jwt;

    public AuthResponse(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }
}

