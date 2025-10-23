package com.project._FALogin.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String fullname;
    private String phoneNumber;
    private String password;
}
