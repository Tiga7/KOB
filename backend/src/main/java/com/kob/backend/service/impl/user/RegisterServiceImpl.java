package com.kob.backend.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.OrdinaryUser;
import com.kob.backend.service.user.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder Encoder;

    @Override
    public Map<String, String> register(String username, String password, String confirmedPwd) {

        Map<String, String> map = new HashMap<>();

        if (username == null) {
            map.put("message", "用户名不能为空");
            return map;
        }
        username = username.trim();
        if (username.length() == 0) {
            map.put("message", "用户名不能为空");
            return map;
        }
        if (password == null || confirmedPwd == null) {
            map.put("message", "输入密码不能为空");
            return map;
        }
        if (password.length() == 0 || confirmedPwd.length() == 0) {
            map.put("message", "输入密码不能为空");
            return map;
        }
        if (username.length() > 20) {
            map.put("message", "用户名长度不能超过20");
            return map;
        }

        if (!password.equals(confirmedPwd)) {
            map.put("message", "两次输入的密码不相同");
            return map;
        }
        if (password.length() > 32) {
            map.put("message", "密码长度不能超过32");
            return map;
        }

        QueryWrapper<OrdinaryUser> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("username", username);
        OrdinaryUser user = userMapper.selectOne(queryWrapper);
        if (user != null) {
            map.put("message", "用户名已存在");
            return map;
        }

        String encodedPwd = Encoder.encode(password);
        String photo ="somewhere";

        OrdinaryUser ordinaryUser = new OrdinaryUser(null, username, encodedPwd, photo);

        if (userMapper.insert(ordinaryUser) == 0) {
            map.put("message", "register failed");
            return map;
        }

        map.put("message", "register successfully");
        return map;
    }
}
