package com.neo4work.rssreader.mapper;

import com.neo4work.rssreader.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface userMapper {
    @Insert("insert into user (username,password,userIdCode) VALUES(#{user.username},#{user.password},#{user.userIdCode})")
    int insertUser(@Param ("user")User user);

    @Select("select * from user where username = #{username}")
    User selectUserByUsername(@Param("username") String username);
}