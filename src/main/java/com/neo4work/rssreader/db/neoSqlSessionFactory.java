package com.neo4work.rssreader.db;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

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
            // 检查是否以JAR包形式运行
            boolean isJar = isRunningInJar();
            System.out.println("当前运行模式: " + (isJar ? "JAR包" : "目录"));
            
            // 确保数据库文件存在
            ensureDatabaseFileExists(isJar);
            
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
     * 判断当前应用是否以JAR包形式运行
     * 
     * @return true: JAR运行  false: 目录运行
     */
    private static boolean isRunningInJar() {
        try {
            // 获取当前类的加载位置
            Class<?> currentClass = neoSqlSessionFactory.class;
            URL location = currentClass.getProtectionDomain().getCodeSource().getLocation();
            
            // 转换为字符串路径
            String path = location.toString();
            
            // 关键判断：JAR运行时路径以"jar:"开头
            return path.startsWith("jar:");
        } catch (Exception e) {
            // 异常时默认返回false
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 确保数据库文件存在，如果不存在则创建
     * 
     * @param isJar 是否以JAR包形式运行
     */
    private static void ensureDatabaseFileExists(boolean isJar) {
        try {
            // 获取当前目录
            String currentDir = System.getProperty("user.dir");
            System.out.println("当前工作目录: " + currentDir);
            
            // 数据库文件对象
            java.io.File dbFile = new java.io.File("rss.db");
            System.out.println("数据库文件路径: " + dbFile.getAbsolutePath());
            
            if (!dbFile.exists()) {
                if (isJar) {
                    // 如果是JAR包运行，尝试从JAR包中提取rss.db文件
                    System.out.println("JAR包运行，尝试从JAR包中提取rss.db文件");
                    extractDbFileFromJar(dbFile);
                } else {
                    // 如果是目录运行，创建新的数据库文件
                    System.out.println("目录运行，创建新的数据库文件");
                    createNewDatabaseFile(dbFile);
                }
            } else {
                System.out.println("数据库文件已存在");
            }
        } catch (Exception e) {
            System.out.println("检查或创建数据库文件失败: " + e.getMessage());
            e.printStackTrace();
            
            // 即使失败，也尝试创建新的数据库文件作为兜底
            try {
                createNewDatabaseFile(new java.io.File("rss.db"));
            } catch (Exception e2) {
                System.out.println("兜底创建数据库文件失败: " + e2.getMessage());
                e2.printStackTrace();
            }
        }
    }
    
    /**
     * 从JAR包中提取rss.db文件到当前工作目录
     * 
     * @param dbFile 目标数据库文件
     */
    private static void extractDbFileFromJar(java.io.File dbFile) throws Exception {
        try {
            // 获取JAR包中的rss.db资源
            InputStream inputStream = neoSqlSessionFactory.class.getClassLoader().getResourceAsStream("rss.db");
            
            if (inputStream == null) {
                System.out.println("JAR包中没有找到rss.db文件，创建新的数据库文件");
                createNewDatabaseFile(dbFile);
                return;
            }
            
            // 创建输出流，将文件写入当前工作目录
            java.io.FileOutputStream outputStream = new java.io.FileOutputStream(dbFile);
            
            // 复制文件内容
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            
            // 关闭流
            outputStream.close();
            inputStream.close();
            
            System.out.println("从JAR包中提取rss.db文件成功");
        } catch (Exception e) {
            System.out.println("从JAR包中提取rss.db文件失败: " + e.getMessage());
            e.printStackTrace();
            
            // 提取失败时，创建新的数据库文件
            createNewDatabaseFile(dbFile);
        }
    }
    
    /**
     * 创建新的数据库文件
     * 
     * @param dbFile 目标数据库文件
     */
    private static void createNewDatabaseFile(java.io.File dbFile) throws Exception {
        try {
            // 创建新的数据库文件
            java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath());
            conn.close();
            
            System.out.println("创建新的数据库文件成功");
        } catch (Exception e) {
            System.out.println("创建新的数据库文件失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
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
