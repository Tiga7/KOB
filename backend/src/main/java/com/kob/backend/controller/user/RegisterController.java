package com.kob.backend.controller.user;

import com.kob.backend.service.user.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RegisterController {

    @Autowired
    private RegisterService service;

    @PostMapping("/user/register/")
    public Map<String, String> register(@RequestParam Map<String, String> map) {
        return service.register(map.get("username"), map.get("password"), map.get("confirmedPwd"));
    }
}




