package com.kob.backend.socket.utils;

import com.kob.backend.config.utils.JwtUtil;
import io.jsonwebtoken.Claims;


/**
 * 从token中获取id
 */
public class JwtAuthentication {
    public static int getUserId(String token) {
        int userId = -1;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = Integer.parseInt(claims.getSubject());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return userId;
    }
}
