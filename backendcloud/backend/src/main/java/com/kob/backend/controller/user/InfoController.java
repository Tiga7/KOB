package com.kob.backend.controller.user;

import com.kob.backend.service.user.account.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class InfoController {
    @Autowired
    private InfoService service;

    @RequestMapping("/user/getInfo/")
    public Map<String,String> getInfo(){
        return service.getInfo();
    }
}
