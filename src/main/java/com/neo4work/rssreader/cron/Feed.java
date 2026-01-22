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
        urls.add("https://feeds.allenpike.com/feed/");
        for(int i=0;i<urls.size();i++)
        {
            Feed.addFeed(urls.get(i));
        }

    }

    public static int addFeed(String feed_url)
    {
        int resultcode;
        String result=http.get(feed_url);
        if(JSON.isJSON(result))
        {
            resultcode=JSON.parseJSON(feed_url,result,"description");
            return resultcode;
        }
        if(ATOM.isATOM(result))
        {
            resultcode=ATOM.parseXML(feed_url,result,"description");
            return resultcode;
        }
        if(RSS.isRSS(result))
        {
            resultcode=RSS.parseXML(feed_url,result,"description");
            return resultcode;
        }
        if(RSSATOM.isRSSATOM(result))
        {
            resultcode=RSSATOM.parseXML(feed_url,result,"description");
            return resultcode;
        }
        return -1;
    }
}
