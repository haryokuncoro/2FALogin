package com.project._FALogin.service;

import com.project._FALogin.entity.AppUser;
import com.project._FALogin.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final TwoFactorService twoFactorService;
    private final EmailService emailService;
    private final SmsService smsService;

    public boolean startLogin(String username, String password, String channel) {
        var userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) return false;
        AppUser user = userOpt.get();
        // TODO: use BCrypt check in production
        if (!user.getPassword().equals(password)) return false;

        String code = twoFactorService.generateCode(username);
        String message = "Your 2FA code is: " + code + " (valid 5 minutes)";

        if ("sms".equalsIgnoreCase(channel) && user.getPhone()!=null) {
            smsService.sendSms(user.getPhone(), message);
        } else {
            emailService.sendSimpleEmail(user.getUsername(), "Your 2FA code", message);
        }
        return true;
    }

    public boolean verify(String username, String code) {
        return twoFactorService.verifyCode(username, code);
    }
}