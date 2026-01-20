package com.neo4work.rssreader.mapper;

import com.neo4work.rssreader.entity.Feed;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface feedMapper
{
    @Insert("insert into feed (title,url) VALUES(#{feed.title},#{feed.url})")
    int insertFeed(@Param ("feed")Feed feed);

    @Select("select * from feed")
    List<Feed> selectFeedAll();
    
    // 根据url获取feed信息
    @Select("select * from feed where url = #{url}")
    Feed selectFeedByUrl(@Param("url") String url);
    
    // 根据url删除feed
    @Delete("delete from feed where url = #{url}")
    int deleteFeedByUrl(@Param("url") String url);
}
