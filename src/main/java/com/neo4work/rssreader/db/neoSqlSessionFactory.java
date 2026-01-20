package com.neo4work.rssreader.db;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * MyBatis工具类：创建并提供SQLite的SqlSessionFactory和SqlSession实例
 */
public class neoSqlSessionFactory
{
    // 单例SqlSessionFactory（静态变量保证全局唯一）
    private static SqlSessionFactory sqlSessionFactory;

    // 静态代码块：项目启动时初始化SqlSessionFactory
    static
    {
        try
        {
            // 1. 读取MyBatis配置文件
            String configPath = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(configPath);

            // 2. 构建SqlSessionFactory（核心步骤）
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

            // 3. 关闭输入流
            inputStream.close();
        } catch (IOException e)
        {
            // 初始化失败时抛出运行时异常
            throw new RuntimeException("创建SQLite的SqlSessionFactory失败", e);
        }
    }

    /**
     * 获取SqlSessionFactory实例（对外暴露）
     */
    public static SqlSessionFactory getSqlSessionFactory()
    {
        return sqlSessionFactory;
    }

    /**
     * 获取SqlSession实例（手动提交事务，适合增删改）
     */
    public static SqlSession getSqlSession()
    {
        return sqlSessionFactory.openSession(false);
    }

    /**
     * 重载方法：指定是否自动提交事务（true适合查询）
     */
    public static SqlSession getSqlSession(boolean autoCommit)
    {
        return sqlSessionFactory.openSession(autoCommit);
    }
}
