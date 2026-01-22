package com.neo4work.rssreader.cron;

import com.neo4work.rssreader.config.PropFiles;
import com.neo4work.rssreader.db.dbFeedCommit;
import com.neo4work.rssreader.db.dbItemCommit;
import com.neo4work.rssreader.entity.Feed;
import com.neo4work.rssreader.entity.Item;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ATOM
{
    private static final Log log = LogFactory.getLog(ATOM.class);

    public static boolean isATOM(String content)
    {
        if(XML.isXML(content))
        {
            Document doc ;
            String rootName ;
            try
            {
                doc= DocumentHelper.parseText(content);
                rootName=doc.getRootElement().getName();
                if("feed".equals(rootName)||"FEED".equals(rootName))
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
    private Map<String, String> feedProperties(String url)
    {
        //TODO
        String host;
        if (url.split("2").length > 2)
        {
            host = url.split("/")[2];
            List<String> propFileList ;
            Map<String, String> map = new HashMap<>();

            Properties prop = new Properties();

            propFileList = PropFiles.getPropFiles();

            if (!propFileList.isEmpty() && propFileList.contains(host + "atom.properties"))
            {
                try (InputStream input = Files.newInputStream(Paths.get(ClassLoader.getSystemResource(host + "atom.properties").toURI())))
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
                catch (IOException | URISyntaxException e)
                {
                    log.error(e);
                }
                return map;
            }

        }
        Map<String, String> map = new HashMap<>();

        String filename;
        Properties prop = new Properties();
        filename="defaultatom.properties";

        try(InputStream input = Files.newInputStream(Paths.get(ClassLoader.getSystemResource(filename).toURI())))
        {
            prop.load(input);
            String feed=prop.getProperty("feed");
            map.put("feed",feed);
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
        catch (IOException | URISyntaxException e)
        {
            log.error(e);
        }
        return map;
    }
    public static void parseXML(String url,String result,String type)
    {
        Map <String,String> map;
        List<Item> ItemList=new ArrayList<>();

        map=new ATOM().feedProperties(url);
        Document doc;
        try
        {
            doc= DocumentHelper.parseText(result);
            Map<String,String> map2=new HashMap<>();
            map2.put("feed",map.get("feed"));
            XPath titleXpath= doc.createXPath(map.get("title"));
            titleXpath.setNamespaceURIs(map2);
            Element title= (Element) titleXpath.selectSingleNode(doc);
            //System.out.println(title.getTextTrim());
            String titleString=title.getTextTrim();

            XPath itemXpath= doc.createXPath(map.get("item"));
            itemXpath.setNamespaceURIs(map2);
            List<Node> itemList=  itemXpath.selectNodes(doc);

            XPath subtitleXpath= doc.createXPath(map.get("subtitle"));
            subtitleXpath.setNamespaceURIs(map2);
            List<Node> subtitleList= subtitleXpath.selectNodes(doc);

            XPath pubtimeXpath= doc.createXPath(map.get("pubTime"));
            pubtimeXpath.setNamespaceURIs(map2);
            List<Node> pubtimeList= pubtimeXpath.selectNodes(doc);

            XPath contentXpath= doc.createXPath(map.get("content"));
            contentXpath.setNamespaceURIs(map2);
            List<Node> contentList= contentXpath.selectNodes(doc);

            XPath linkXpath= doc.createXPath(map.get("link"));
            linkXpath.setNamespaceURIs(map2);
            List<Node> linkList= linkXpath.selectNodes(doc);
            //List<Node> linkList=doc.selectNodes(map.get("link"));
            if("content".equals(type))
            {
                if(itemList.size()==subtitleList.size()
                        &&subtitleList.size() == pubtimeList.size()
                        &&pubtimeList.size()==contentList.size()
                        && contentList.size() == linkList.size()
                        &&!itemList.isEmpty())
                {
                    for(int i=0;i<itemList.size();i++)
                    {

                        Element subtitleElement =(Element) subtitleList.get(i);
                        //System.out.println(subtitleElement.getTextTrim());

                        Element pubtimeElement =(Element) pubtimeList.get(i);
                        //System.out.println(pubtimeElement.getTextTrim());
                        Element contentElement =(Element) contentList.get(i);
                        //System.out.println(contentElement .getTextTrim());
                        Element linkElement =(Element) linkList.get(i);
                        //System.out.println(linkElement.getTextTrim());
                        Item Item=new Item(url,titleString,subtitleElement.getTextTrim(),linkElement.getTextTrim(),pubtimeElement.getTextTrim(),contentElement .getTextTrim());
                        ItemList.add(Item);
                    }
                    int resultcode= dbItemCommit.doCommit(ItemList);
                    System.out.println(resultcode);
                }

            }
            if("description".equals(type))
            {
                Feed feed=new Feed(titleString,url);
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
