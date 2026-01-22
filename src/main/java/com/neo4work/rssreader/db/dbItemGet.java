package com.neo4work.rssreader.db;

import com.neo4work.rssreader.entity.Item;
import com.neo4work.rssreader.mapper.itemMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class dbItemGet {
    private static final Log log = LogFactory.getLog(dbItemGet.class);

    // 根据feedId获取所有文章
    public static List<Item> doGetByItemUrl(String item_url) {
        SqlSession sqlSession = null;
        try {
            sqlSession = neoSqlSessionFactory.getSqlSession(true);
            itemMapper itemMapper = sqlSession.getMapper(itemMapper.class);
            List<Item> itemList = itemMapper.selectItemsByItemUrl(item_url);
            return itemList;
        } catch (Exception e) {
            log.error(e);
            return null;
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }
}