package com.kob.backend.socket;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.mapper.BotRecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.OrdinaryUser;
import com.kob.backend.socket.utils.Game;
import com.kob.backend.socket.utils.JwtAuthentication;
import com.kob.backend.socket.utils.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Gan
 */
@Component
@ServerEndpoint("/websocket/{token}")
public class WebSocketServer {
    /**
     * 连接池
     */
    final public static ConcurrentHashMap<Integer, WebSocketServer> USERS = new ConcurrentHashMap<>();

    private OrdinaryUser user;
    private Session session = null;

    //不是spring的单例模式 不能直接注入

    private static UserMapper userMapper;

    public static BotRecordMapper botRecordMapper;

    private static BotMapper botMapper;

    public Game game = null;

    /**
     * 发送请求
     */
    public static RestTemplate restTemplate;

    private static final String addPlayerUrl = "http://127.0.0.1:3031/player/add/";
    private static final String removePlayerUrl = "http://127.0.0.1:3031/player/remove/";

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }

    @Autowired
    public void setRecordMapper(BotRecordMapper botRecordMapper) {
        WebSocketServer.botRecordMapper = botRecordMapper;
    }

    @Autowired
    public void setBotMapper(BotMapper botMapper) {
        WebSocketServer.botMapper = botMapper;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        WebSocketServer.restTemplate = restTemplate;
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
    }

    @OnClose
    public void onClose() {
        //关闭链接
        System.out.println("关闭连接!");
        if (USERS != null) {
            USERS.remove(this.user.getId());
        }
    }

    public static void startGame(Integer aId, Integer aBotId, Integer bId, Integer bBotId) {
        OrdinaryUser a = userMapper.selectById(aId);
        OrdinaryUser b = userMapper.selectById(bId);

        Bot botA = botMapper.selectById(aBotId);
        Bot botB = botMapper.selectById(bBotId);

        Game game = new Game(
                13,
                14,
                20,
                a.getId(),
                b.getId(),
                botA,
                botB
        );
        game.createMap();

        if (USERS.get(a.getId()) != null) {
            USERS.get(a.getId()).game = game;
        }
        if (USERS.get(b.getId()) != null) {
            USERS.get(b.getId()).game = game;
        }


        game.start();

        JSONObject respGame = new JSONObject();
        respGame.put("a_id", game.getPlayerA().getId());
        respGame.put("a_sx", game.getPlayerA().getSx());
        respGame.put("a_sy", game.getPlayerA().getSy());
        respGame.put("b_id", game.getPlayerB().getId());
        respGame.put("b_sx", game.getPlayerB().getSx());
        respGame.put("b_sy", game.getPlayerB().getSy());
        respGame.put("map", game.getG());


        JSONObject respA = new JSONObject();
        respA.put("event", "start-matching");
        respA.put("opponent_username", b.getUsername());
        respA.put("opponent_photo", b.getPhoto());
        respA.put("game", respGame);
        if (USERS.get(a.getId()) != null) {
            USERS.get(a.getId()).sendMessage(respA.toJSONString());
        }


        JSONObject respB = new JSONObject();
        respB.put("event", "start-matching");
        respB.put("opponent_username", a.getUsername());
        respB.put("opponent_photo", a.getPhoto());
        respB.put("game", respGame);
        if (USERS.get(b.getId()) != null) {
            USERS.get(b.getId()).sendMessage(respB.toJSONString());
        }

    }

    private void startMatching(Integer botId) {
        System.out.println("开始匹配");
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", user.getId().toString());
        data.add("rating", user.getRating().toString());
        data.add("bot_id", botId.toString());
        //url   参数  返回值的class
        restTemplate.postForObject(addPlayerUrl, data, String.class);

    }

    private void stopMatching() {
        System.out.println("结束匹配");

        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", user.getId().toString());
        //url   参数  返回值的class
        restTemplate.postForObject(removePlayerUrl, data, String.class);
    }

    private void move(int direction) {
        if (game.getPlayerA().getId().equals(user.getId())) {
            if (game.getPlayerA().getBotId().equals(-1)) {//人工操作
                game.setNextStepA(direction);
            }
        } else if (game.getPlayerB().getId().equals(user.getId())) {
            if (game.getPlayerB().getBotId().equals(-1)) {
                game.setNextStepB(direction);
            }
        }
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
            startMatching(Integer.parseInt(data.getString("bot_id")));
        } else if ("stop-matching".equals(event)) {
            stopMatching();
        } else if ("move".equals(event)) {
            move(data.getInteger("direction"));
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    /**
     * 发送json串
     */
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
