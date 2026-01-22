package com.neo4work.rssreader.cron;

import java.util.ArrayList;
import java.util.List;

public class Feed
{
    public static void main(String[] args)
    {
        System.out.println("hello world");
        List<String> urls=new ArrayList<>();
        urls.add("https://daringfireball.net/feeds/json");
        urls.add("https://www.stats.gov.cn/sj/zxfb/rss.xml");
        urls.add("https://www.thisiscolossal.com/feed/");
        urls.add("https://jvns.ca/atom.xml");
        urls.add("https://furbo.org/feed/json");
        for(int i=0;i<urls.size();i++)
        {
            Feed.addFeed(urls.get(i));
        }

    }

    public static void addFeed(String feed_url)
    {
        String result=http.get(feed_url);
        if(JSON.isJSON(result))
        {
            JSON.parseJSON(feed_url,result,"description");
        }
        if(ATOM.isATOM(result))
        {
            ATOM.parseXML(feed_url,result,"description");
        }
        if(RSS.isRSS(result))
        {
            RSS.parseXML(feed_url,result,"description");
        }
        if(RSSATOM.isRSSATOM(result))
        {
            RSSATOM.parseXML(feed_url,result,"description");
        }
    }
}
