package com.project._FALogin.controller;

import com.project._FALogin.dto.LoginRequest;
import com.project._FALogin.dto.VerifyRequest;
import com.project._FALogin.service.AuthService;
import com.project._FALogin.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    @Autowired
    AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        boolean started = authService.startLogin(req.getUsername(), req.getPassword(), req.getChannel());
        if (!started) return ResponseEntity.status(401).body("Invalid credentials");
        return ResponseEntity.ok("2FA code sent");
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody VerifyRequest req) {
        boolean ok = authService.verify(req.getUsername(), req.getCode());
        if (!ok) return ResponseEntity.status(401).body("Invalid code");
        String token = jwtUtil.generateToken(req.getUsername());
        return ResponseEntity.ok(Map.of("token", token));
    }
}