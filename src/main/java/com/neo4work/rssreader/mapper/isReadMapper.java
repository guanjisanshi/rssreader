package com.neo4work.rssreader.mapper;

import com.neo4work.rssreader.entity.IsRead;
import org.apache.ibatis.annotations.*;

public interface isReadMapper {
    @Insert("insert into isread (userIdCode,itemLink,star,isRead) VALUES(#{isRead.userIdCode},#{isRead.itemLink},#{isRead.star},#{isRead.isRead})")
    int insertIsRead(@Param ("isRead")IsRead isRead);

    @Select("select * from isread where userIdCode = #{userIdCode} and itemLink = #{itemLink}")
    IsRead selectIsReadByUserIdAndItemLink(@Param("userIdCode") String userIdCode, @Param("itemLink") String itemLink);
    
    @Update("update isread set isRead = #{isRead.isRead} where userIdCode = #{isRead.userIdCode} and itemLink = #{isRead.itemLink}")
    int updateIsRead(@Param ("isRead")IsRead isRead);
}