package com.neo4work.rssreader.util;

import org.springframework.stereotype.Component;
import java.net.URL;

/**
 * Spring Boot运行环境判断工具类
 */
@Component
public class RunEnvDetector
{

    /**
     * 判断当前应用是否以JAR包形式运行
     *
     * @return true: JAR运行  false: 目录运行（IDE/解压文件夹）
     */
    public boolean isRunningInJar()
    {
        try
        {
            // 获取当前类的加载位置
            Class<?> currentClass = getClass();
            URL location = currentClass.getProtectionDomain().getCodeSource().getLocation();

            // 转换为字符串路径
            String path = location.toString();

            // 关键判断：JAR运行时路径包含"jar!"，目录运行时是"file:"开头且无"jar!"
            return path.startsWith("jar:") && path.contains("jar!");
        } catch (Exception e)
        {
            // 异常时默认返回false（兜底逻辑）
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断当前应用是否以目录形式运行
     *
     * @return true: 目录运行  false: JAR运行
     */
    public boolean isRunningInDirectory()
    {
        return !isRunningInJar();
    }
}