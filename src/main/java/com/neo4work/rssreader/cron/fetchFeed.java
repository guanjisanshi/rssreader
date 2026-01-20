package com.neo4work.rssreader.cron;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public class fetchFeed {
    private static final Logger log = LogManager.getLogger(fetchFeed.class);




    public static void main(String[] args) {
        //String url="https://daringfireball.net/feeds/json";
        String url="https://www.thisiscolossal.com/feed/";
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
