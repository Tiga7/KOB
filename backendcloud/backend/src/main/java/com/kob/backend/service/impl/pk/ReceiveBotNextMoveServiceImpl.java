package com.kob.backend.service.impl.pk;

import com.kob.backend.service.pk.ReceiveBotNextMoveService;
import com.kob.backend.socket.WebSocketServer;
import com.kob.backend.socket.utils.Game;
import org.springframework.stereotype.Service;

@Service
public class ReceiveBotNextMoveServiceImpl implements ReceiveBotNextMoveService {
    @Override
    public String receiveBotNextMove(Integer userId, Integer direction) {
        System.out.println("receive bot next move : " + userId + " => " + direction);

        if (WebSocketServer.USERS.get(userId) != null) {
            Game game = WebSocketServer.USERS.get(userId).game;
            if (game != null) {
                if (game.getPlayerA().getId().equals(userId)) {
                    game.setNextStepA(direction);

                } else if (game.getPlayerB().getId().equals(userId)) {
                    game.setNextStepB(direction);

                }
            }
        }


        return "receive next move success";
    }

}
