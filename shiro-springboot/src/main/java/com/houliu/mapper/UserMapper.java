package com.houliu.mapper;

import com.houliu.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author houliu
 * @create 2020-02-14 20:53
 */
public interface UserMapper extends Mapper<User> {

    @Select("SELECT id,name,pwd,perms,salt FROM user WHERE name = #{name}")
    User queryUserByName(@Param("name") String name);

    @Select("SELECT perms FROM user WHERE name = #{username}")
    String queryPermsByName(String username);
}
