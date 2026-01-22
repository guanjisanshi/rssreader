package com.neo4work.rssreader.db;

import com.neo4work.rssreader.entity.User;
import com.neo4work.rssreader.mapper.userMapper;
import org.apache.ibatis.session.SqlSession;

public class InsertAdminUser {
    public static void main(String[] args) {
        SqlSession sqlSession = null;
        try {
            sqlSession = neoSqlSessionFactory.getSqlSession(true);
            userMapper userMapper = sqlSession.getMapper(userMapper.class);
            
            // 检查admin用户是否已经存在
            User existingUser = userMapper.selectUserByUsername("admin");
            if (existingUser == null) {
                // 创建admin用户
                User adminUser = new User("admin", "admin", "admin123456");
                int result = userMapper.insertUser(adminUser);
                sqlSession.commit();
                System.out.println("Admin user inserted successfully: " + result);
            } else {
                System.out.println("Admin user already exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (sqlSession != null) {
                sqlSession.rollback();
            }
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }
}