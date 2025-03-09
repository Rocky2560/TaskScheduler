package com.example.demo.JWT;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

@Component
public class SecretKeyGenerator {

    public String generateSecretKey() {
        // Generate a secure random 256-bit key
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[32]; // 256 bits
        secureRandom.nextBytes(key);

        // Encode it in Base64
        return Base64.getEncoder().encodeToString(key);
    }
}

