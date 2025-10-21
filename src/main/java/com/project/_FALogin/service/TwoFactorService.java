package com.project._FALogin.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TwoFactorService {
    record CodeEntry(String code, Instant expiresAt) {}

    private final Map<String, CodeEntry> store = new ConcurrentHashMap<>();
    private final Random random = new Random();

    @Value("${twofactor.code.ttl:300000}")
    private long ttlMs;

    public String generateCode(String username) {
        int n = 100000 + random.nextInt(900000);
        String code = String.valueOf(n);
        store.put(username, new CodeEntry(code, Instant.now().plusMillis(ttlMs)));
        return code;
    }

    public boolean verifyCode(String username, String code) {
        var entry = store.get(username);
        if (entry == null) return false;
        if (Instant.now().isAfter(entry.expiresAt())) {
            store.remove(username);
            return false;
        }
        boolean ok = entry.code().equals(code);
        if (ok) store.remove(username);
        return ok;
    }
}