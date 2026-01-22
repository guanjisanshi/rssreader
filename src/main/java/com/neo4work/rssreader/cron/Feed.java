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
        if(JSONType.isJSON(result))
        {
            JSONType.parseJSON(feed_url,result,"description");
        }
        if(ATOMType.isATOM(result))
        {
            ATOMType.parseXML(feed_url,result,"description");
        }
        if(RSSType.isRSS(result))
        {
            RSSType.parseXML(feed_url,result,"description");
        }
        if(RSSATOMType.isRSSATOM(result))
        {
            RSSATOMType.parseXML(feed_url,result,"description");
        }
    }
}
