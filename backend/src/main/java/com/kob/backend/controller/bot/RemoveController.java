package com.kob.backend.controller.bot;

import com.kob.backend.service.user.bot.RemoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RemoveController {
    @Autowired
    private RemoveService removeService;

    @RequestMapping("/user/bot/remove")
    public Map<String,String> removeBot(@RequestParam Map<String,String> data)
    {
        return removeService.removeBot(data);
    }
}
