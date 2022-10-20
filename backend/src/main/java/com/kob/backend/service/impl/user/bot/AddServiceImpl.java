package com.kob.backend.service.impl.user.bot;

import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.OrdinaryUser;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.impl.utils.getUser;
import com.kob.backend.service.user.bot.AddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AddServiceImpl implements AddService {

    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> addBot(Map<String, String> data) {

        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        var loginUser = (UserDetailsImpl)authenticationToken.getPrincipal();
        OrdinaryUser user =loginUser.getUser();

        String title = data.get("title");
        String description = data.get("description");
        String content = data.get("content");

        Map<String,String> map =new HashMap<>();

        if (title==null||title.length()==0){
            map.put("message","标题不能为空");
            return map;
        }

        if (title.length()>100){
            map.put("message","标题长度不能超过100");
            return map;
        }

        if(description==null||description.length()==0)
        {
            description="这个用户很懒,没有留下描述~";
        }
        if (description.length()>300)
        {
            map.put("message","bot的描述不能大于300");
            return map;
        }

        if (content.length()>10000){
            map.put("message","代码长度不能超过10000");
            return map;
        }

        Date now =new Date();
        Bot bot = new Bot(null,user.getId(),title,description,content,1500,now,now);

        botMapper.insert(bot);

        map.put("message","添加Bot成功");
        return map;
    }
}
