package com.neo4work.rssreader.db;

import com.neo4work.rssreader.entity.IsRead;
import com.neo4work.rssreader.mapper.isReadMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;

public class dbIsReadCommit {
    private static final Log log = LogFactory.getLog(dbIsReadCommit.class);

    // 插入已读状态
    public static int doCommit(IsRead isRead) {
        SqlSession sqlSession = null;
        try {
            sqlSession = neoSqlSessionFactory.getSqlSession(true);
            isReadMapper isReadMapper = sqlSession.getMapper(isReadMapper.class);
            int count = isReadMapper.insertIsRead(isRead);
            sqlSession.commit();
            return count;
        } catch (Exception e) {
            sqlSession.rollback();
            log.error(e);
            return -1;
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }
    
    // 更新已读状态
    public static int doUpdate(IsRead isRead) {
        SqlSession sqlSession = null;
        try {
            sqlSession = neoSqlSessionFactory.getSqlSession(true);
            isReadMapper isReadMapper = sqlSession.getMapper(isReadMapper.class);
            int count = isReadMapper.updateIsRead(isRead);
            sqlSession.commit();
            return count;
        } catch (Exception e) {
            sqlSession.rollback();
            log.error(e);
            return -1;
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }
}