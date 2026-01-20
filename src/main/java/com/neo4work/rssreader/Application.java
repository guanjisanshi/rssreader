package com.neo4work.rssreader;

import com.neo4work.rssreader.util.RunEnvDetector;
import jakarta.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Application implements CommandLineRunner
{
    @Resource
    private RunEnvDetector runEnvDetector;
    
    public static void main(String[] args)
    {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception
    {
        boolean isJar = runEnvDetector.isRunningInJar();
        boolean isDir = runEnvDetector.isRunningInDirectory();

        // 输出结果
        System.out.println("是否以JAR包运行：" + isJar);
        System.out.println("是否以目录运行：" + isDir);
    }
}
