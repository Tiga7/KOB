package com.kob.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Gan
 */

@Controller
@RequestMapping("/pk/")

public class IndexController {

    @RequestMapping("/index")
    public String index() {
        return "pk/index.html";
    }

}