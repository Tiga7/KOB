package com.kob.backend.service.impl.rank;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.BotRecord;
import com.kob.backend.pojo.OrdinaryUser;
import com.kob.backend.service.rank.RankListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.List;

@Service
public class RankListServiceImpl implements RankListService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public JSONObject getRankList(Integer page) {
        int pageSize = 10;
        IPage<OrdinaryUser> list = new Page<>(page, pageSize);
        QueryWrapper<OrdinaryUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("rating");
        List<OrdinaryUser> users = userMapper.selectPage(list, queryWrapper).getRecords();
        JSONObject resp = new JSONObject();
        resp.put("users", users);
        for (OrdinaryUser user : users)
            user.setPassword("");
        resp.put("total_count", userMapper.selectCount(null));

        return resp;
    }
}
