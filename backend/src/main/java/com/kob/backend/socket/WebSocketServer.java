package com.kob.backend.socket;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.OrdinaryUser;
import com.kob.backend.socket.utils.Game;
import com.kob.backend.socket.utils.JwtAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Gan
 */
@Component
@ServerEndpoint("/websocket/{token}")
public class WebSocketServer {


    /**
     * 连接池
     */
    final private static ConcurrentHashMap<Integer, WebSocketServer> USERS = new ConcurrentHashMap<>();

    /**
     * 匹配池
     */
    final private static CopyOnWriteArrayList<OrdinaryUser> MATCH_POOL = new CopyOnWriteArrayList<>();

    private OrdinaryUser user;
    private Session session = null;

    //不是spring的单例模式 不能直接注入

    private static UserMapper userMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        //建立连接
        this.session = session;
        System.out.println("建立连接!");
        Integer userId = JwtAuthentication.getUserId(token);
        this.user = userMapper.selectById(userId);
        if (this.user != null) {
            USERS.put(userId, this);
        } else {
            this.session.close();
        }
//        System.out.println(USERS);
    }

    @OnClose
    public void onClose() {
        //关闭链接
        System.out.println("关闭连接!");
        if (USERS != null) {
            USERS.remove(this.user.getId());
            MATCH_POOL.remove(this.user);
        }
    }

    private void startMatching() {

        System.out.println("开始匹配");
        MATCH_POOL.add(this.user);

        while (MATCH_POOL.size() >= 2) {
            Iterator<OrdinaryUser> it = MATCH_POOL.iterator();
            OrdinaryUser a = it.next(), b = it.next();

            MATCH_POOL.remove(a);
            MATCH_POOL.remove(b);

            Game game = new Game(13,14,20);
            game.createMap();

            JSONObject respA = new JSONObject();
            respA.put("event", "start-matching");
            respA.put("opponent_username", b.getUsername());
            respA.put("opponent_photo", b.getPhoto());
            respA.put("game_map",game.getG());
            USERS.get(a.getId()).sendMessage(respA.toJSONString());

            JSONObject respB = new JSONObject();
            respB.put("event", "start-matching");
            respB.put("opponent_username", a.getUsername());
            respB.put("opponent_photo", a.getPhoto());
            respB.put("game_map",game.getG());
            USERS.get(b.getId()).sendMessage(respB.toJSONString());
        }


    }

    private void stopMatching() {
        System.out.println("结束匹配");
        MATCH_POOL.remove(this.user);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        //从Client接收信息
        //一般当做路由
        System.out.println("接收到信息!");

        //将前端返回的数据解析
        JSONObject data = JSONObject.parseObject(message);
        String event = data.getString("event");
        if ("start-matching".equals(event)) {
            startMatching();
        } else if ("stop-matching".equals(event)) {
            stopMatching();
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public void sendMessage(String message) {
        synchronized (this.session) {
            try {
                this.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
