package com.kob.backend.socket.utils;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.pojo.BotRecord;
import com.kob.backend.socket.WebSocketServer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Game extends Thread {

    private final Integer rows;
    private final Integer cols;
    /**
     * 地图内障碍物总数
     */
    private final Integer wallsCount;
    /**
     * 地图
     */
    private final int[][] g;

    /**
     * 方向向量
     */
    private final static int[] DX = {-1, 0, 1, 0};
    private final static int[] DY = {0, 1, 0, -1};

    private final Player playerA, playerB;

    private Integer nextStepA = null;
    private Integer nextStepB = null;

    private ReentrantLock lock = new ReentrantLock();

    private String status = "playing";// playing -> finished
    private String loser = "all"; //all:平局  A:A输    B:B输

    private BotRecord botRecord;

    public Game(Integer rows, Integer cols, Integer wallsCount, int playerAid, int playerBid) {
        this.rows = rows;
        this.cols = cols;
        this.wallsCount = wallsCount;
        this.g = new int[rows][cols];
        playerA = new Player(playerAid, this.rows - 2, 1, new ArrayList<>());
        playerB = new Player(playerBid, 1, this.cols - 2, new ArrayList<>());
    }

    public Player getPlayerA() {
        return playerA;
    }

    public Player getPlayerB() {
        return playerB;
    }

    public void setNextStepA(Integer nextStepA) {
        lock.lock();
        try {
            this.nextStepA = nextStepA;
        } finally {
            lock.unlock();
        }


    }

    public void setNextStepB(Integer nextStepB) {
        lock.lock();
        try {
            this.nextStepB = nextStepB;
        } finally {
            lock.unlock();
        }

    }

    public int[][] getG() {
        return g;
    }

    /**
     * 输入起点丶终点坐标,判断地图是否连通
     */
    private boolean checkConnectivity(int sx, int sy, int tx, int ty) {
        if (sx == tx && sy == ty) return true;
        g[sx][sy] = 1;

        for (int i = 0; i < 4; i++) {
            int x = sx + DX[i], y = sy + DY[i];
            if (x >= 0 && x < this.rows && y >= 0 && y < this.cols && g[x][y] == 0) {
                if (checkConnectivity(x, y, tx, ty)) {
                    g[sx][sy] = 0;
                    return true;
                }
            }
        }

        g[sx][sy] = 0;
        return false;
    }

    private boolean drawG() {//画地图
        //初始化 0为空 1为墙
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                g[i][j] = 0;
            }
        }

        //给四周画墙
        for (int i = 0; i < this.rows; i++) {
            g[i][0] = g[i][this.cols - 1] = 1;
        }
        for (int i = 0; i < this.cols; i++) {
            g[0][i] = g[this.rows - 1][i] = 1;
        }

        //随机对称创造障碍物
        Random rand = new Random();
        for (int i = 0; i < this.wallsCount / 2; i++) {
            for (int j = 0; j < 1000; j++) {
                int r = rand.nextInt(this.rows);
                int c = rand.nextInt(this.cols);

                if (g[r][c] == 1 || g[this.rows - 1 - r][this.cols - 1 - c] == 1) continue;
                if (r == this.rows - 2 && c == 1 || c == this.cols - 2 && r == 1) continue;

                g[r][c] = g[this.rows - 1 - r][this.cols - 1 - c] = 1;
                break;
            }
        }
        return checkConnectivity(this.rows - 2, 1, 1, this.cols - 2);
    }

    public void createMap() {
        for (int i = 0; i < 1000; i++) {
            if (drawG()) break;
        }
    }


    private boolean nextStep() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
        for (int i = 0; i < 25; i++) {
            try {
                Thread.sleep(200);
                lock.lock();
                try {
//                    nextStepA=1;
                    if (nextStepA != null && nextStepB != null) {
                        playerA.getSteps().add(nextStepA);
                        playerB.getSteps().add(nextStepB);
                        return true;
                    }
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;

    }

    private boolean checkValid(List<Cell> cellsA, List<Cell> cellsB) {
        //判断蛇的身体是否重合
        int len = cellsA.size();

        Cell cell = cellsA.get(len - 1);

        if (g[cell.getX()][cell.getY()] == 1) return false;

        for (int i = 0; i < len; i++) {
            if (cellsA.get(i).getX() == cell.getX() && cellsA.get(i).getY() == cell.getY())
                return false;
        }
        for (int i = 0; i < len; i++) {
            if (cellsB.get(i).getX() == cell.getX() && cellsB.get(i).getY() == cell.getY())
                return false;
        }
        return true;
    }

    private void judge() {//判断玩家下一步操作是否合法

        List<Cell> cellsA = playerA.getCells();
        List<Cell> cellsB = playerB.getCells();
        boolean validA = checkValid(cellsA, cellsB);
        boolean validB = checkValid(cellsB, cellsA);
        if (!validA && !validB) {
            loser = "all";
        } else if (!validA) {
            loser = "A";
        } else {
            loser = "B";
        }

    }

    private void sendAllMessage(String message) {
        if (WebSocketServer.USERS.get(playerA.getId()) != null) {
            WebSocketServer.USERS.get(playerA.getId()).sendMessage(message);
        }
        if (WebSocketServer.USERS.get(playerB.getId()) != null) {
            WebSocketServer.USERS.get(playerB.getId()).sendMessage(message);
        }
    }

    private void sendMove() {//向Client 传递移动信息
        lock.lock();
        try {
            JSONObject resp = new JSONObject();
            resp.put("event", "move");
            resp.put("a_direction", nextStepA);
            resp.put("b_direction", nextStepB);
            sendAllMessage(resp.toJSONString());
            nextStepA = nextStepB = null;
        } finally {
            lock.unlock();
        }

    }

    private String getMapString() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                res.append(g[i][j]);
            }
        }
        return res.toString();
    }

    private void saveRecordToDB() {
        BotRecord botRecord = new BotRecord(
                null,
                playerA.getId(),
                playerA.getSx(),
                playerA.getSy(),
                playerB.getId(),
                playerB.getSx(),
                playerB.getSy(),
                playerA.getStepString(),
                playerB.getStepString(),
                getMapString(),
                loser,
                new Date()
        );
        WebSocketServer.botRecordMapper.insert(botRecord);
    }


    private void sendResult() {//向Client 发送结果

        JSONObject resp = new JSONObject();
        resp.put("event", "result");
        resp.put("loser", loser);
        saveRecordToDB();
        sendAllMessage(resp.toJSONString());
    }

    /**
     * 新线程的入口函数
     */
    @Override
    public void run() {
        //循环的次数比地图大
        for (int i = 0; i < 1000; i++) {
            if (nextStep()) {

                judge();
                if (status.equals("playing")) {
                    sendMove();
                } else {
                    sendResult();
                    break;
                }

            } else {
                status = "finished";
                lock.lock();
                try {
                    if (nextStepA == null && nextStepB == null) {
                        loser = "all";
                    } else if (nextStepA == null) {
                        loser = "A";
                    } else {
                        loser = "B";
                    }
                } finally {
                    lock.unlock();
                }

                sendResult();
                break;
            }
        }
    }
}
