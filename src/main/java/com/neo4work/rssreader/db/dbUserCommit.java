package com.neo4work.rssreader.db;

import com.neo4work.rssreader.entity.User;
import com.neo4work.rssreader.mapper.userMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;

public class dbUserCommit {
    private static final Log log = LogFactory.getLog(dbUserCommit.class);

    // 插入用户
    public static int doCommit(User user) {
        SqlSession sqlSession = null;
        try {
            sqlSession = neoSqlSessionFactory.getSqlSession();
            userMapper userMapper = sqlSession.getMapper(userMapper.class);
            int count = userMapper.insertUser(user);
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