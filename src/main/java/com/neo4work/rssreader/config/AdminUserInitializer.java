package com.neo4work.rssreader.config;

import com.neo4work.rssreader.db.neoSqlSessionFactory;
import com.neo4work.rssreader.entity.User;
import com.neo4work.rssreader.mapper.userMapper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminUserInitializer implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        SqlSession sqlSession = null;
        try {
            sqlSession = neoSqlSessionFactory.getSqlSession();
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