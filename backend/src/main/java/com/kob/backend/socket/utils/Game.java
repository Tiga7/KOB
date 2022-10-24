package com.kob.backend.socket.utils;

import java.util.Random;

public class Game {

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
    final private static int[] DX = {-1, 0, 1, 0};
    final private static int[] DY = {0, 1, 0, -1};


    public Game(Integer rows, Integer cols, Integer wallsCount) {
        this.rows = rows;
        this.cols = cols;
        this.wallsCount = wallsCount;
        this.g = new int[rows][cols];
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
}
