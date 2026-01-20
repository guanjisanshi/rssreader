package com.neo4work.rssreader.config;

import org.apache.ibatis.session.SqlSession;
import com.neo4work.rssreader.db.neoSqlSessionFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        SqlSession sqlSession = null;
        try {
            sqlSession = neoSqlSessionFactory.getSqlSession();
            
            // 创建user表
            String createUserTable = "CREATE TABLE IF NOT EXISTS user (id INTEGER PRIMARY KEY AUTOINCREMENT, username VARCHAR(50) NOT NULL UNIQUE, password VARCHAR(50) NOT NULL, userIdCode VARCHAR(100) NOT NULL UNIQUE)";
            sqlSession.getConnection().createStatement().execute(createUserTable);
            
            // 创建isread表
            String createIsReadTable = "CREATE TABLE IF NOT EXISTS isread (id VARCHAR(100) PRIMARY KEY, userIdCode VARCHAR(100) NOT NULL, itemLink VARCHAR(255) NOT NULL, star INTEGER DEFAULT 0, isRead INTEGER DEFAULT 0, UNIQUE(userIdCode, itemLink))";
            sqlSession.getConnection().createStatement().execute(createIsReadTable);
            
            System.out.println("Database tables created successfully");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }
}