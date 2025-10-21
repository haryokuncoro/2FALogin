package com.project._FALogin.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
    private String channel; // "email" or "sms"
}