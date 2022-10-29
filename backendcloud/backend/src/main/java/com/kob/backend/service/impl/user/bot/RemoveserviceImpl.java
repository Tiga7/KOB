package com.kob.backend.service.impl.user.bot;

import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.OrdinaryUser;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.bot.RemoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RemoveserviceImpl implements RemoveService {

    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> removeBot(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        var loginUser=(UserDetailsImpl) authenticationToken.getPrincipal();
        OrdinaryUser user = loginUser.getUser();


        Map<String,String> map =new HashMap<>();
        if (data.get("bot_id")==null||data.get("bot_id").length()==0)
        {
            map.put("message","botId不能为空");
            return map;
        }
        int botId = Integer.parseInt(data.get("bot_id"));

        Bot bot =botMapper.selectById(botId);
        if (bot==null)
        {
            map.put("message","bot不存在");
            return map;
        }

        if (!bot.getUserId().equals(user.getId()))
        {
            map.put("message","这个不是你的bot,没有权限删除");
            return map;
        }


        botMapper.deleteById(bot.getId());
        map.put("message","删除bot成功");

        return map;
    }
}
