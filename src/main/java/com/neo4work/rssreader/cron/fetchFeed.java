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
