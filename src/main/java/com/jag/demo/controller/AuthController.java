package com.jag.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.jag.demo.entity.User;
import com.jag.demo.repository.UserRepository;
import com.jag.demo.util.JwtUtil;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody AuthRequest authRequest) throws Exception {
        // username = "admin";
        // password = "password123";
        System.out.println("q hay");
        log.info("AuthRequest: {} ", authRequest);
        Optional<User> userFromDb = Optional
                .ofNullable(userRepository.findByEmail(authRequest.getEmail()).orElse(null));
        log.info("userFromDb: {} ", userFromDb);
        if (userFromDb.isEmpty()) {
            log.info("vacio");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            log.info("estamos aca adentro del else");
            User user = (User) userFromDb.get();
            log.info("user {}", user);

            // Verificar la contraseña usando BCryptPasswordEncoder
            if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
                log.info("Contraseña incorrecta");
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            // Autenticación exitosa, generar el token JWT
            final String jwt = jwtUtil.generateToken(user.getEmail());
            user.setJWT(jwt);
            user.setPassword("");
            return ResponseEntity.ok(user);
        }
    }
}

@ToString
@Getter
@Setter
class AuthRequest {
    @NotNull(message = "Nombre de usuario no puede ser vacio")
    private String email;
    @NotNull(message = "Password no puede ser vacio")
    private String password;
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
