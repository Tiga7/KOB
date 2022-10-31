package com.kob.botrunningsystem.service.utils;

import com.kob.botrunningsystem.utils.BotInterface;
import org.joor.Reflect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
public class Consumer extends Thread {

    private Bot bot;

    private RestTemplate restTemplate;

    @Autowired
    private void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private final static String receiveBotMotUrl = "http://127.0.0.1:3030/pk/receive/bot/move/";

    public void startTimeOut(long timeout, Bot bot) {
        this.bot = bot;
        //开启一个新的线程 执行run方法
        this.start();

        try {
            //该线程阻塞 == 最多等待timeout秒 然后执行后面的代码
            this.join(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //中断当前线程
            this.interrupt();
        }
    }

    private String addUid(String code, String uid) {  // 在code中的Bot类名后添加uid
        int k = code.indexOf(" implements com.kob.botrunningsystem.utils.BotInterface");
        return code.substring(0, k) + uid + code.substring(k);
    }


    @Override
    public void run() {

        UUID uuid = UUID.randomUUID();
        String uid = uuid.toString().substring(0, 8);
        // 类重名的话只会编译一次 所以要使得类名不同 后面要加上一个随机id
        System.out.println(addUid(bot.getBotCode(), uid));

        String className = "com.kob.botrunningsystem.utils.Bot" + uid;
        System.out.println(className);
        //不知道为什么这里不能正常执行 没法正常的反射调用
        BotInterface botInterface = Reflect.compile(
                className,
                addUid(bot.getBotCode(), uid)
        ).create().get();
        Integer direction = botInterface.nextMove(bot.getInput());

        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", bot.getUserId().toString());
        //没法正常返回direction 手动模拟1
        data.add("direction", "1");
        restTemplate.postForObject(receiveBotMotUrl, data, String.class);

//        System.out.println(bot.getUserId() + " bot  move :" + direction);
    }
}
