package com.neo4work.rssreader.db;

import com.neo4work.rssreader.entity.IsRead;
import com.neo4work.rssreader.mapper.isReadMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;

public class dbIsReadGet {
    private static final Log log = LogFactory.getLog(dbIsReadGet.class);

    // 根据用户ID和文章链接获取已读状态
    public static IsRead doGetByUserIdAndItemLink(String userIdCode, String itemLink) {
        SqlSession sqlSession = null;
        try {
            sqlSession = neoSqlSessionFactory.getSqlSession();
            isReadMapper isReadMapper = sqlSession.getMapper(isReadMapper.class);
            IsRead isRead = isReadMapper.selectIsReadByUserIdAndItemLink(userIdCode, itemLink);
            return isRead;
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