package com.kob.backend.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Gan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

@TableName("ordinary_user")
public class OrdinaryUser {

    @TableId(type = IdType.AUTO)//主键自增
    private Integer id;
    private String username;
    private String password;
    private String photo;
    private Integer rating;

}
