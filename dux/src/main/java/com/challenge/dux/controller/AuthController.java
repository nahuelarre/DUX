package com.challenge.dux.controller;

import com.challenge.dux.model.dto.auth.AuthRequest;
import com.challenge.dux.model.dto.auth.AuthResponse;
import com.challenge.dux.security.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        if ("test".equals(request.getUsername()) && "12345".equals(request.getPassword())) {
            String token = jwtTokenProvider.generateToken(request.getUsername());
            return ResponseEntity.ok(new AuthResponse(token));
        }
        return ResponseEntity.status(401).body(new AuthResponse("Invalid credentials"));
    }
}
