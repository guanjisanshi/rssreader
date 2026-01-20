package com.neo4work.rssreader.db;

import com.neo4work.rssreader.entity.Feed;
import com.neo4work.rssreader.mapper.feedMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;

public class dbFeedCommit
{
    private static final Log log = LogFactory.getLog(dbFeedCommit.class);

    public static int doCommit(Feed feed)
    {
        SqlSession sqlSession=null;
        try
        {
            sqlSession= neoSqlSessionFactory.getSqlSession();
            feedMapper feedMapper=sqlSession.getMapper(feedMapper.class);
            int count=feedMapper.insertFeed(feed);
            sqlSession.commit();
            return count;
        }
        catch(Exception e)
        {
            sqlSession.rollback();
            log.error(e);
            return -1;
        }
        finally
        {
            if(sqlSession!=null)
            {
                sqlSession.close();
            }
        }
    }
    
    // 根据URL删除feed
    public static int doDeleteByUrl(String url)
    {
        SqlSession sqlSession=null;
        try
        {
            sqlSession= neoSqlSessionFactory.getSqlSession();
            feedMapper feedMapper=sqlSession.getMapper(feedMapper.class);
            int count=feedMapper.deleteFeedByUrl(url);
            sqlSession.commit();
            return count;
        }
        catch(Exception e)
        {
            sqlSession.rollback();
            log.error(e);
            return -1;
        }
        finally
        {
            if(sqlSession!=null)
            {
                sqlSession.close();
            }
        }
    }
}
