package com.kob.backend.service.impl.user.bot;

import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.OrdinaryUser;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.bot.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UpdateServiceImpl implements UpdateService {

    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> updateBot(Map<String, String> data) {

        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        OrdinaryUser user = loginUser.getUser();

        Map<String, String> map = new HashMap<>();

        if (data.get("bot_id")==null || data.get("bot_id").length()==0)
        {
            map.put("message","bot_id不能为空");
            return map;
        }
        int botId = Integer.parseInt(data.get("bot_id"));

        String title = data.get("title");
        String description = data.get("description");
        String content = data.get("content");

        if (title == null || title.length() == 0) {
            map.put("message", "标题不能为空");
            return map;
        }
        if (title.length() > 100) {
            map.put("message", "标题长度不能超过100");
            return map;
        }
        if (description == null || description.length() == 0) {
            description = "这个用户很懒,没有留下描述~";
        }
        if (description.length() > 300) {
            map.put("message", "bot的描述不能大于300");
            return map;
        }
        if (content==null || content.length()==0)
        {
            map.put("message","代码不能为空");
            return map;
        }
        if (content.length() > 10000) {
            map.put("message", "代码长度不能超过10000");
            return map;
        }

        Bot bot = botMapper.selectById(botId);
        if (bot == null) {
            map.put("message", "Bot不存在");
            return map;
        }
        if (!bot.getUserId().equals(user.getId())) {
            map.put("message", "没有权限修改bot");
            return map;
        }
        Bot newBot = new Bot(bot.getId(),
                user.getId(),
                title,
                description,
                content,
                bot.getRating(),
                bot.getCreateTime(),
                new Date());
        botMapper.updateById(newBot);
        map.put("message", "修改Bot成功");

        return map;
    }
}
