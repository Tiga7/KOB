package com.kob.backend.service.impl.record;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.mapper.BotRecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.BotRecord;
import com.kob.backend.pojo.OrdinaryUser;
import com.kob.backend.service.record.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class RecordServiceImpl implements RecordService {
    @Autowired
    private BotRecordMapper botRecordMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public JSONObject getRecordList(Integer page) {

        IPage<BotRecord> list = new Page<>(page, 10);
        QueryWrapper<BotRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        List<BotRecord> records = botRecordMapper.selectPage(list, queryWrapper).getRecords();
        JSONObject resp = new JSONObject();

        List<JSONObject> items = new LinkedList<>();

        for (BotRecord record : records) {
            OrdinaryUser userA = userMapper.selectById(record.getAId());
            OrdinaryUser userB = userMapper.selectById(record.getBId());
            JSONObject item = new JSONObject();
            item.put("a_username", userA.getUsername());
            item.put("a_photo", userA.getPhoto());
            item.put("b_username", userB.getUsername());
            item.put("b_photo", userB.getPhoto());
            String res = "平局";
            if ("A".equals(record.getLoser()))res = "B胜";
            else if ("B".equals(record.getLoser())) res ="A胜";
            item.put("result",res);
            item.put("record", record);
            items.add(item);
        }
        resp.put("data", items);

        resp.put("total_count", botRecordMapper.selectCount(null));

        return resp;
    }
}
