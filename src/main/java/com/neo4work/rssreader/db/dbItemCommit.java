package com.neo4work.rssreader.db;

import com.neo4work.rssreader.entity.Item;
import com.neo4work.rssreader.mapper.itemMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class dbItemCommit
{
    private static final Log log = LogFactory.getLog(dbItemCommit.class);

    public static int doCommit(List<Item> items)
    {
        SqlSession sqlSession=null;
        try
        {
            sqlSession= neoSqlSessionFactory.getSqlSession(true);
            itemMapper itemMapper=sqlSession.getMapper(itemMapper.class);
            int count=itemMapper.batchInsertItem(items);
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
