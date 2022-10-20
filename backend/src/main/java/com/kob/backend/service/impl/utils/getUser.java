package com.kob.backend.service.impl.utils;

import com.kob.backend.pojo.OrdinaryUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class getUser {

        public OrdinaryUser getUserFromJwt(){
            UsernamePasswordAuthenticationToken authenticationToken =
                    (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            //会进数据库查询
            UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
            return loginUser.getUser();
        }
}
