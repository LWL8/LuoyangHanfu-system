package com.luoyang.hanfu;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashTest {
    @Test
    public void testHash() {
        System.out.println("HASH_RESULT: " + new BCryptPasswordEncoder().encode("123456"));
    }
}