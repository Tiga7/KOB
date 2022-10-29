package com.kob.matchingsystem.service.utils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class MatchingPool extends Thread {

    private static List<Player> players = new ArrayList<>();

    private ReentrantLock lock = new ReentrantLock();

    private static RestTemplate restTemplate;

    private final static String startGameUrl = "http://127.0.0.1:3030/pk/start/game/";

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        MatchingPool.restTemplate = restTemplate;
    }

    public void addPlayer(Integer playerId, Integer rating) {
        lock.lock();
        try {
            players.add(new Player(playerId, rating, 0));
        } finally {
            lock.unlock();
        }

    }

    public void removePlayer(Integer playerId) {
        lock.lock();
        try {
            //可以换一种数据结构 改进一下
            List<Player> newPlayers = new ArrayList<>();
            for (Player player : players) {
                if (!player.getUserId().equals(playerId)) {
                    newPlayers.add(player);
                }
            }
            players = newPlayers;
        } finally {
            lock.unlock();
        }
    }

    private void increaseWaitingTime() {
        for (Player player : players) {
            player.setWaitingTime(player.getWaitingTime() + 1);
        }
    }

    private void matchPlayer() {//尝试匹配所有玩家

        System.out.println("matching Players: " + players);
        //标记是否被匹配
        boolean[] used = new boolean[players.size()];
        for (int i = 0; i < players.size(); i++) {
            if (used[i]) continue;
            for (int j = i + 1; j < players.size(); j++) {
                if (used[j]) continue;
                Player a = players.get(i);
                Player b = players.get(j);
                if (checkMatched(a, b)) {
                    //可以匹配
                    used[i] = used[j] = true;
                    sendResult(a, b);
                    break;
                }
            }
        }

        lock.lock();
        try {
            //可以换一种数据结构 改进一下
            List<Player> newPlayers = new ArrayList<>();
            for (int i = 0; i < players.size(); i++) {
                if (!used[i])
                    newPlayers.add(players.get(i));
            }
            players = newPlayers;
        } finally {
            lock.unlock();
        }
    }

    private boolean checkMatched(Player a, Player b) {//判断玩家是否匹配
        int deltaRating = Math.abs(a.getRating() - b.getRating());
        int waitingTime = Math.min(a.getWaitingTime(), b.getWaitingTime());
        return deltaRating <= waitingTime * 10;

    }

    private void sendResult(Player a, Player b) {//返回匹配结果

        System.out.println("send result: " + a + " " + b);
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("a_id", a.getUserId().toString());
        data.add("b_id", b.getUserId().toString());
        restTemplate.postForObject(startGameUrl, data, String.class);

    }

    @Override
    public void run() {

        while (true) {
            try {
                Thread.sleep(1000);
                lock.lock();
                try {
                    increaseWaitingTime();
                    matchPlayer();
                } finally {
                    lock.unlock();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
