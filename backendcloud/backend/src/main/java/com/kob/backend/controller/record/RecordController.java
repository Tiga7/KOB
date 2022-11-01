package com.kob.backend.controller.record;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.service.record.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

@RestController
public class RecordController {

    @Autowired
    private RecordService recordService;

    @GetMapping("/record/getlist/")
    JSONObject getRecordList(@RequestParam Map<String, String> data) {

        Integer page = Integer.parseInt(Objects.requireNonNull(data.get("page")));
        return recordService.getRecordList(page);
    }
}
