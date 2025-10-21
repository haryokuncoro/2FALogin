package com.project._FALogin.service;

import com.project._FALogin.config.SecurityConfig;
import com.project._FALogin.dto.LoginRequest;
import com.project._FALogin.entity.AppUser;
import com.project._FALogin.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final TwoFactorService twoFactorService;
    private final EmailService emailService;
    private final SmsService smsService;
    private final SecurityConfig securityConfig;

    public boolean startLogin(String username, String password, String channel) {
        var userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) return false;
        AppUser user = userOpt.get();
        if (!securityConfig.passwordEncoder().matches(password, user.getPassword())) return false;


        String code = twoFactorService.generateCode(username);
        String message = "Your 2FA code is: " + code + " (valid 5 minutes)";

        if ("sms".equalsIgnoreCase(channel) && user.getPhone()!=null) {
            smsService.sendSms(user.getPhone(), message);
        } else {
            try {
                emailService.sendEmail(
                        user.getUsername(),
                        "Your 2FA Verification Code",
                        "Your verification code is: " + code
                );
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public boolean verify(String username, String code) {
        return twoFactorService.verifyCode(username, code);
    }

    public AppUser register(LoginRequest req){
        if (userRepository.findByUsername(req.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        AppUser user = AppUser.builder()
                .username(req.getUsername())
                .password(securityConfig.passwordEncoder().encode(req.getPassword()))
                .fullname(req.getUsername()) // optional: can add fullname field in DTO
                .enabled(true)
                .build();
        return userRepository.save(user);
    }
}