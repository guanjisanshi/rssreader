package com.neo4work.rssreader.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PropFiles
{
    private static final Log log = LogFactory.getLog(PropFiles.class);

    public static List<String> getPropFiles()
    {
        List<File> propertiesFiles = new ArrayList<>();
        List<String> propertiesFileString=new ArrayList<>();
        try
        {
            // 获取resources目录的URL
            URL resourcesUrl = PropFiles.class.getClassLoader().getResource("");
            if (resourcesUrl == null) {
                log.error("无法获取resources目录");
                return new ArrayList<>();
            }

            // 创建File对象
            File resourcesDir = new File(resourcesUrl.getFile());
            if (!resourcesDir.exists() || !resourcesDir.isDirectory()) {
                log.error("resources目录不存在或不是目录");
                return new ArrayList<>();
            }

            // 获取目录下的所有文件
            File[] files = resourcesDir.listFiles();
            if (files == null) {
                log.error("无法获取resources目录下的文件列表");
                return new ArrayList<>();
            }

            // 筛选出properties文件

            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".properties")) {
                    propertiesFiles.add(file);
                }
            }

            // 输出结果

            System.out.println("resources一级目录下的properties文件：");
            if (propertiesFiles.isEmpty()) {
                System.out.println("没有找到properties文件");
            } else {
                for (File file : propertiesFiles) {
                    System.out.println("- " + file.getName());
                    propertiesFileString.add(file.getName());

                }
            }
        } catch (Exception e) {
            log.error("获取资源目录失败");
        }
        return propertiesFileString;
    }
}
