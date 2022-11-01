package com.kob.backend.controller.rank;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.service.rank.RankListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

@RestController
public class RankListController {

    @Autowired
    private RankListService rankListService;

    @GetMapping("/rank/ranklist/")
    public JSONObject getRankList(@RequestParam Map<String, String> data) {
        Integer page = Integer.parseInt(Objects.requireNonNull(data.get("page")));
        return rankListService.getRankList(page);
    }
}
