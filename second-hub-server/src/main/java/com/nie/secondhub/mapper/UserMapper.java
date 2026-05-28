package com.nie.secondhub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nie.secondhub.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}