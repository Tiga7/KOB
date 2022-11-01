package com.kob.backend.service.rank;

import com.alibaba.fastjson.JSONObject;

public interface RankListService {
    JSONObject getRankList(Integer page);
}
