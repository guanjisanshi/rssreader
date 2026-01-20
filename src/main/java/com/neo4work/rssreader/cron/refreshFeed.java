package com.neo4work.rssreader.cron;

public class refreshFeed {
    public void refreshFeed(String url) {
        // 这里可以实现具体的刷新逻辑
        //String url="https://daringfireball.net/feeds/json";
        //String url="https://www.thisiscolossal.com/feed/";
        //String url="https://www.stats.gov.cn/sj/zxfb/rss.xml";
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
        if(RSSATOMType.isRSSATOM(result))
        {
            RSSATOMType.parseXML(url,result,"content");
        }
    }
}
