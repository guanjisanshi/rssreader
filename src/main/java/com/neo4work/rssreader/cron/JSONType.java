package com.neo4work.rssreader.cron;
import com.alibaba.fastjson2.JSON;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONPath;
import com.neo4work.rssreader.db.dbFeedCommit;
import com.neo4work.rssreader.db.dbItemCommit;
import com.neo4work.rssreader.entity.Feed;
import com.neo4work.rssreader.entity.Item;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JSONType
{
    private static final Log log = LogFactory.getLog(JSONType.class);

    public static boolean isJSON(String content)
    {

        if(JSON.isValid(content))
            return true;
        else
            return false;
    }
    private Map<String, String> feedProperties(String url, String type)
    {
        //TODO
        if(url.contains(""))
        {

        }
        Map<String, String> map = new HashMap<>();

        String filename=null;
        Properties prop = new Properties();

        if(type.equals("json"))
        {
            filename="defaultjson.properties";
        }

        try(InputStream input = Files.newInputStream(Paths.get(ClassLoader.getSystemResource(filename).toURI())))
        {
            prop.load(input);
            String root=prop.getProperty("root");
            map.put("root",root);
            String title=prop.getProperty("title");
            map.put("title",title);
            String item=prop.getProperty("item");
            map.put("item",item);
            String subtitle=prop.getProperty("subtitle");
            map.put("subtitle",subtitle);
            String pubTime=prop.getProperty("pubTime");
            map.put("pubTime",pubTime);
            String content=prop.getProperty("content");
            map.put("content",content);
            String link=prop.getProperty("link");
            map.put("link",link);

        }
        catch (IOException e)
        {
            log.error(e);
        }
        catch (URISyntaxException e)
        {
            log.error(e);
        }
        return map;
    }
    public static void parseJSON(String url,String result,String type)
    {
        Map <String,String> map=new HashMap<>();
        List<Item> ItemList=new ArrayList<>();
        map=new JSONType().feedProperties(url,"json");
        try
        {
            JSONObject JOB=JSON.parseObject(result);
            Object title= JSONPath.eval(JOB,map.get("title"));
            //System.out.println(title);

            //System.out.println(map.get("title"));
            Object item= JSONPath.eval(JOB,map.get("item"));
            Object subtitle= JSONPath.eval(JOB,map.get("subtitle"));
            Object pubTime= JSONPath.eval(JOB,map.get("pubTime"));
            Object content= JSONPath.eval(JOB,map.get("content"));
            Object link= JSONPath.eval(JOB,map.get("link"));
            if(item instanceof List<?>
                    &&subtitle instanceof List<?>
                    &&pubTime instanceof List<?>)
            {
                List<Map<String,Object>> itemList=(List<Map<String,Object>>)item;
                List<String> subtitleList=(List<String>)subtitle;
                List<String> pubTimeList=(List<String>)pubTime;
                List<String> contentList=(List<String>)content;
                List<String> linkList=(List<String>)link;
                if("content".equals(type))
                {
                    if(itemList.size()==subtitleList.size()
                            &&subtitleList.size()==pubTimeList.size()
                            &&pubTimeList.size()==contentList.size()
                            &&contentList.size()==linkList.size()
                            &&itemList.size()!=0)
                    {
                        for(int i=0;i<itemList.size();i++)
                        {

                            Item Item=new Item(url,(String) title,subtitleList.get(i),linkList.get(i),pubTimeList.get(i),contentList.get(i));
                            ItemList.add(Item);

                        }
                        int resultcode= dbItemCommit.doCommit(ItemList);
                        System.out.println(resultcode);
                    }
                }
                if("description".equals(type))
                {
                    Feed feed=new Feed((String)title,url);
                    int resultcode= dbFeedCommit.doCommit(feed);
                    System.out.println(resultcode);
                }


            }


        }
        catch (Exception e)
        {
            log.error(e);
        }

    }
}
