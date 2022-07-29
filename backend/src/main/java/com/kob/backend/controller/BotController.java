package com.kob.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Gan
 */
@RestController
@RequestMapping("/pk/")
public class BotController {

    @RequestMapping("/info")
    public Map<String,String> getBotInfo(){
        Map<String,String> map =new HashMap<>();
        map.put("name","一号选手");
        map.put("rating","10010");
        return map;
    }
}
