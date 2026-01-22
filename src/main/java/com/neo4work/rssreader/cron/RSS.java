package com.neo4work.rssreader.cron;


import com.neo4work.rssreader.config.PropFiles;
import com.neo4work.rssreader.db.dbFeedCommit;
import com.neo4work.rssreader.db.dbItemCommit;
import com.neo4work.rssreader.entity.Feed;
import com.neo4work.rssreader.entity.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Namespace;
import org.dom4j.Node;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

//https://www.stats.gov.cn/sj/zxfb/rss.xml
//https://www.stats.gov.cn/sj/sjjd/rss.xml
public class RSS
{
    private static final Logger log = LogManager.getLogger(RSS.class);

    public static boolean isRSS(String content)
    {

        if(XML.isXML(content))
        {
            Document doc ;
            String rootName ;
            try
            {
                doc=DocumentHelper.parseText(content);
                rootName=doc.getRootElement().getName();
                List<Namespace> namespaceList=doc.getRootElement().declaredNamespaces();
                if(namespaceList.size()>0)
                {
                    for(Namespace namespace :namespaceList)
                    {
                        if(namespace.getURI().equals("http://www.w3.org/2005/Atom"))
                        {
                            return false;
                        }
                    }
                }
                if("rss".equals(rootName)||"RSS".equals(rootName))
                {
                    return true;
                }
            }
            catch (Exception e)
            {
                log.warn(e);
                return false;
            }
        }
        return false;
    }

    private Map<String, String> feedProperties(String url, String type)
    {
        //TODO
        String host;
        if (url.split("2").length > 2)
        {
            host = url.split("/")[2];
            List<String> propFileList;
            Map<String, String> map = new HashMap<>();

            Properties prop = new Properties();

            propFileList = PropFiles.getPropFiles();
            if (!propFileList.isEmpty() && propFileList.contains(host+"rss.properties"))
            {
                try (InputStream input = Files.newInputStream(Paths.get(ClassLoader.getSystemResource(host + "rss.properties").toURI())))
                {
                    prop.load(input);
                    String root = prop.getProperty("root");
                    map.put("root", root);
                    String title = prop.getProperty("title");
                    map.put("title", title);
                    String item = prop.getProperty("item");
                    map.put("item", item);
                    String subtitle = prop.getProperty("subtitle");
                    map.put("subtitle", subtitle);
                    String pubTime = prop.getProperty("pubTime");
                    map.put("pubTime", pubTime);
                    String content = prop.getProperty("content");
                    map.put("content", content);
                    String link = prop.getProperty("link");
                    map.put("link", link);

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

        }

        Map<String, String> map = new HashMap<>();

        String filename = null;
        Properties prop = new Properties();
        if (type.equals("rss"))
        {
            filename = "defaultrss.properties";
        }

        try (InputStream input = Files.newInputStream(Paths.get(ClassLoader.getSystemResource(filename).toURI())))
        {
            prop.load(input);
            String root = prop.getProperty("root");
            map.put("root", root);
            String title = prop.getProperty("title");
            map.put("title", title);
            String item = prop.getProperty("item");
            map.put("item", item);
            String subtitle = prop.getProperty("subtitle");
            map.put("subtitle", subtitle);
            String pubTime = prop.getProperty("pubTime");
            map.put("pubTime", pubTime);
            String content = prop.getProperty("content");
            map.put("content", content);
            String link = prop.getProperty("link");
            map.put("link", link);

        } catch (IOException e)
        {
            log.error(e);
        } catch (URISyntaxException e)
        {
            log.error(e);
        }
        return map;
    }

    public static void parseXML(String url,String result,String type)
    {
        Map <String,String> map;
        List<Item> ItemList=new ArrayList<>();
        map=new RSS().feedProperties(url,"rss");
        Document doc;
        try
        {
            doc= DocumentHelper.parseText(result);
            String title=doc.selectSingleNode(map.get("title")).getText();
            List<Node> itemList=doc.selectNodes(map.get("item"));

            List<Node> subtitleList=doc.selectNodes(map.get("subtitle"));
            List<Node> pubtimeList=doc.selectNodes(map.get("pubTime"));
            List<Node> contentList=doc.selectNodes(map.get("content"));
            List<Node> linkList=doc.selectNodes(map.get("link"));
            if("content".equals(type))
            {
                if(itemList.size()==subtitleList.size()
                        &&subtitleList.size() == pubtimeList.size()
                        &&pubtimeList.size()==contentList.size()
                        &&contentList.size() == linkList.size()
                        &&!itemList.isEmpty())
                {
                    for(int i=0;i<itemList.size();i++)
                    {

                        //System.out.println(subtitleList.get(i).getText());
                        //System.out.println(pubtimeList.get(i).getText());
                        //System.out.println(contentList.get(i).getText());
                        //System.out.println(linkList.get(i).getText());
                        Item Item=new Item(url,title,subtitleList.get(i).getText(),linkList.get(i).getText(),pubtimeList.get(i).getText(),contentList.get(i).getText());
                        // 获取feed_id
                        ItemList.add(Item);
                    }
                    int resultcode= dbItemCommit.doCommit(ItemList);
                    System.out.println(resultcode);
                }
            }
            if("description".equals(type))
            {
                Feed feed=new Feed(title,url);
                int resultcode= dbFeedCommit.doCommit(feed);
                System.out.println(resultcode);
            }


        }
        catch (Exception e)
        {
            log.error(e);
        }

    }
}
