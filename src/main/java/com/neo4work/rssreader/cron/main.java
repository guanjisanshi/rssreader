package com.neo4work.rssreader.cron;

import com.neo4work.rssreader.db.dbFeedGet;
import com.neo4work.rssreader.entity.Feed;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class main
{
    // 简单的UID生成器实现，基于时间戳和序列号
    private static final AtomicLong sequence = new AtomicLong(0);
    private static long lastTimeMillis = System.currentTimeMillis();
    
    public static void main(String[] args)
    {
        List<Feed> feedList=new ArrayList<Feed>();
        feedList= dbFeedGet.doGet();
        if(feedList.size()!=0)
        {
            for(Feed feed:feedList)
            {
                String url=feed.getUrl();
                String result = http.get(url);
                //System.out.println(result);
                if(RSSType.isRSS(result))
                {
                    RSSType.parseXML(url,result,"content");
                }
                if(ATOMType.isATOM(result))
                {
                    ATOMType.parseXML(url,result,"content");
                }
                if(JSONType.isJSON(result))
                {
                    JSONType.parseJSON(url,result,"content");
                }
            }
        }
    }
    
    // 生成唯一ID的静态方法，供其他类调用
    // 基于时间戳+序列号的简单实现
    public static synchronized Long generateUid() {
        long currentTime = System.currentTimeMillis();
        
        // 如果当前时间与上次时间相同，则增加序列号
        if (currentTime == lastTimeMillis) {
            // 序列号循环使用，避免溢出
            long seq = sequence.incrementAndGet() & 0xFFFF; // 使用16位序列号
            if (seq == 0) {
                // 序列号溢出，等待下一个毫秒
                while (currentTime <= lastTimeMillis) {
                    currentTime = System.currentTimeMillis();
                }
            }
            lastTimeMillis = currentTime;
            return (currentTime << 16) | seq;
        } else {
            // 新的毫秒，重置序列号
            sequence.set(0);
            lastTimeMillis = currentTime;
            return currentTime << 16;
        }
    }
}
