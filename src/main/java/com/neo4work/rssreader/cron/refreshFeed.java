package com.neo4work.rssreader.cron;

public class refreshFeed {
    public void refreshFeed(String url) {
        // 这里可以实现具体的刷新逻辑
        //String url="https://daringfireball.net/feeds/json";
        //String url="https://www.thisiscolossal.com/feed/";
        //String url="https://www.stats.gov.cn/sj/zxfb/rss.xml";
        String result = http.get(url);
        //System.out.println(result);
        if(RSS.isRSS(result))
        {
            RSS.parseXML(url,result,"content");
        }
        if(ATOM.isATOM(result))
        {
            ATOM.parseXML(url,result,"content");
        }
        if(JSON.isJSON(result))
        {
            JSON.parseJSON(url,result,"content");
        }
        if(RSSATOM.isRSSATOM(result))
        {
            RSSATOM.parseXML(url,result,"content");
        }
    }
}
