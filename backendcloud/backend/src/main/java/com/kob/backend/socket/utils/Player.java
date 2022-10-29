package com.kob.backend.socket.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private Integer id;
    private Integer sx;
    private Integer sy;
    /**
     * 记录每一个步骤
     */
    private List<Integer> steps;

    private boolean checkTailIncreasing(int step) {//检验当前回合蛇的长度是否增加
        if (step <= 10) return true;
        return step % 3 == 1;
    }

    public String getStepString(){
        StringBuilder res = new StringBuilder();
        for (int i: steps)
        {
            res.append(i);
        }
        return res.toString();
    }

    public List<Cell> getCells() {

        List<Cell> res = new ArrayList<>();

        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};

        int x = sx;
        int y = sy;

        int step = 0;
        res.add(new Cell(x,y));
        for (int d: steps) {
            x+=dx[d];
            y+=dy[d];
            res.add(new Cell(x,y));
            if(!checkTailIncreasing(++step))
            {
                //把蛇头删去
                res.remove(0);
            }
        }
        return  res;
    }
}
