package com.neo4work.rssreader.db;

import com.neo4work.rssreader.entity.Feed;
import com.neo4work.rssreader.mapper.feedMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class dbFeedGet
{
    private static final Log log = LogFactory.getLog(dbFeedGet.class);

    public static List<Feed> doGet()
    {
        SqlSession sqlSession=null;
        try
        {
            sqlSession= neoSqlSessionFactory.getSqlSession();
            feedMapper feedMapper=sqlSession.getMapper(feedMapper.class);
            List<Feed> feedList=feedMapper.selectFeedAll();
            sqlSession.commit();
            return (feedList);
        }
        catch(Exception e)
        {
            log.error(e);
            sqlSession.rollback();
            return null;
        }
        finally
        {
            if (sqlSession != null)
            {
                sqlSession.close();
            }
        }
    }
    
    // 根据URL获取feed信息
    public static Feed doGetByUrl(String url)
    {
        SqlSession sqlSession=null;
        try
        {
            sqlSession= neoSqlSessionFactory.getSqlSession();
            feedMapper feedMapper=sqlSession.getMapper(feedMapper.class);
            Feed feed=feedMapper.selectFeedByUrl(url);
            return feed;
        }
        catch(Exception e)
        {
            log.error(e);
            return null;
        }
        finally
        {
            if (sqlSession != null)
            {
                sqlSession.close();
            }
        }
    }
}
