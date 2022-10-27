package com.kob.backend.socket.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Gan
 * 标记蛇的身体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cell {
    private Integer x;
    private Integer y;
}
