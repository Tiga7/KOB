package com.kob.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sound.midi.Soundbank;

@SpringBootTest
class BackendApplicationTests {

    @Test
    void contextLoads() {
        BCryptPasswordEncoder PasswordEncoder = new BCryptPasswordEncoder();

        String encode = PasswordEncoder.encode("user");
        System.out.println(encode);

//        System.out.println(PasswordEncoder.matches(a,encode));
    }


}
