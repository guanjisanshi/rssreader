package com.neo4work.rssreader.cron;

import com.neo4work.rssreader.db.dbFeedGet;
import com.neo4work.rssreader.entity.Feed;

import java.util.List;

public class getFeed
{
    public static void main(String[] args)
    {
        List<Feed> feedList=dbFeedGet.doGet();
        for(Feed feed:feedList)
        {
            System.out.println(feed.getUrl());
        }
        System.out.println(feedList);
    }
}
