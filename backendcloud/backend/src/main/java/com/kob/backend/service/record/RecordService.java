package com.kob.backend.service.record;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.pojo.BotRecord;

import java.util.List;

public interface RecordService {
    JSONObject getRecordList(Integer page);
}
