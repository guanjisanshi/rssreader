package com.neo4work.rssreader.cron;

import com.neo4work.rssreader.entity.FeedItem;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.apache.http.client5.http.classic.methods.HttpGet;
import org.apache.http.client5.http.impl.classic.CloseableHttpClient;
import org.apache.http.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.http.client5.http.impl.classic.HttpClients;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RSSParser {

    // 使用项目中已有的SSLUtil来忽略SSL证书
    private static final CloseableHttpClient httpClient;

    static {
        try {
            httpClient = HttpClients.custom()
                    .setSSLSocketFactory(new SSLConnectionSocketFactory(
                            SSLUtil.createUnsafeSSLContext()))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create HttpClient with SSL support", e);
        }
    }

    public static List<FeedItem> parseRSS(String url, Integer feedId) throws Exception {
        List<FeedItem> feedItems = new ArrayList<>();
        
        // 使用HttpClient获取RSS内容
        HttpGet httpGet = new HttpGet(url);
        try (CloseableHttpResponse response = httpClient.execute(httpGet);
             InputStream inputStream = response.getEntity().getContent()) {
            
            // 使用dom4j解析RSS
            SAXReader reader = new SAXReader();
            Document document = reader.read(inputStream);
            Element root = document.getRootElement();
            
            // 处理RSS 2.0格式
            Element channel = root.element("channel");
            if (channel != null) {
                List<Element> items = channel.elements("item");
                for (Element item : items) {
                    FeedItem feedItem = new FeedItem();
                    feedItem.setFeedId(feedId);
                    
                    // 解析基本字段
                    feedItem.setTitle(getElementText(item, "title"));
                    feedItem.setLink(getElementText(item, "link"));
                    feedItem.setDescription(getElementText(item, "description"));
                    feedItem.setAuthor(getElementText(item, "author"));
                    feedItem.setGuid(getElementText(item, "guid"));
                    
                    // 解析发布日期
                    String pubDateStr = getElementText(item, "pubDate");
                    if (pubDateStr != null && !pubDateStr.isEmpty()) {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
                            Date pubDate = sdf.parse(pubDateStr);
                            feedItem.setPubDate(pubDate);
                        } catch (Exception e) {
                            // 忽略日期解析错误
                            e.printStackTrace();
                        }
                    }
                    
                    feedItems.add(feedItem);
                }
            }
        }
        
        return feedItems;
    }
    
    private static String getElementText(Element parent, String elementName) {
        Element element = parent.element(elementName);
        return element != null ? element.getTextTrim() : null;
    }
    
    // 关闭HttpClient
    public static void close() throws Exception {
        httpClient.close();
    }
}