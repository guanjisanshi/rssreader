package com.neo4work.rssreader.db;

import com.neo4work.rssreader.entity.User;
import com.neo4work.rssreader.mapper.userMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;

public class dbUserGet {
    private static final Log log = LogFactory.getLog(dbUserGet.class);

    // 根据用户名获取用户信息
    public static User doGetByUsername(String username) {
        SqlSession sqlSession = null;
        try {
            sqlSession = neoSqlSessionFactory.getSqlSession();
            userMapper userMapper = sqlSession.getMapper(userMapper.class);
            User user = userMapper.selectUserByUsername(username);
            return user;
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