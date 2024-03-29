package com.kob.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kob.backend.pojo.OrdinaryUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Gan
 */
@Mapper
public interface UserMapper extends BaseMapper<OrdinaryUser> {

}
