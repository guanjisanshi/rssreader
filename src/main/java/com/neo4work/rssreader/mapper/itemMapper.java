package com.neo4work.rssreader.mapper;

import com.neo4work.rssreader.entity.Item;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface itemMapper
{

    @Insert({
            "<script>",
            "insert into item (item_url,title,subtitle,pubdate,content,link) VALUES",
            "<foreach collection='items' item='item' separator=','>",
            "(#{item.item_url},#{item.title},#{item.subtitle},#{item.pubDate},#{item.content},#{item.link})",
            "</foreach>",
            "</script>"
    })
    int batchInsertItem(@Param("items") List<Item> items);
    
    // 根据feed_id获取所有文章
    @Select("SELECT * FROM item WHERE item_url = #{item_url} ORDER BY pubdate DESC")
    List<Item> selectItemsByItemUrl(@Param("item_url") String item_url);
}
