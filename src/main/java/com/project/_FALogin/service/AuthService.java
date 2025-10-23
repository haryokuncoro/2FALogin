package com.project._FALogin.service;

import com.project._FALogin.config.SecurityConfig;
import com.project._FALogin.dto.LoginRequest;
import com.project._FALogin.dto.RegisterRequest;
import com.project._FALogin.entity.AppUser;
import com.project._FALogin.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service @Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final TwoFactorService twoFactorService;
    private final EmailService emailService;
    private final SmsService smsService;
    private final SecurityConfig securityConfig;

    public boolean startLogin(String username, String password, LoginRequest.Channel channel) {
        var userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) return false;
        AppUser user = userOpt.get();
        if (!securityConfig.passwordEncoder().matches(password, user.getPassword())) return false;

        String code = twoFactorService.generateCode(username);
        String message = "Your 2FA code is: " + code + " (valid 5 minutes)";

        if (channel.equals(LoginRequest.Channel.SMS) && user.getPhone()!=null) {
            smsService.sendWhatsapp(user.getPhone(), message);
        } else {
            try {
                emailService.sendEmail(
                        user.getUsername(),
                        "Your 2FA Verification Code",
                        "Your verification code is: " + code
                );
            } catch (IOException e) {
                log.error("fail to send email, username={}", username, e);
                return false;
            }
        }
        return true;
    }

    public boolean verify(String username, String code) {
        return twoFactorService.verifyCode(username, code);
    }

    public AppUser register(RegisterRequest req){
        if (userRepository.findByUsername(req.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        String fullName = Optional.ofNullable(req.getFullname()).orElse(req.getUsername());
        AppUser user = AppUser.builder()
                .username(req.getUsername())
                .password(securityConfig.passwordEncoder().encode(req.getPassword()))
                .fullname(fullName)
                .phone(req.getPhoneNumber())
                .enabled(true)
                .build();
        return userRepository.save(user);
    }
}