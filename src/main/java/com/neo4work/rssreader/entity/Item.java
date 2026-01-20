package com.neo4work.rssreader.entity;
import java.io.Serializable;
public class Item implements Serializable

{
    private Long id;
    private String item_url;
    private String title;
    private String subtitle;
    private String link;
    private String pubDate;
    private String content;
    public Item()
    {
        super();
    }
    public Item(String item_url,String title, String subtitle,String link, String pubDate, String content)
    {
        super();
        this.item_url=item_url;
        this.title = title;
        this.subtitle = subtitle;
        this.link = link;
        this.pubDate = pubDate;
        this.content = content;
    }

    public String getItem_url()
    {
        return item_url;
    }

    public String getSubtitle()
    {
        return subtitle;
    }


    public Long getId()
    {
        return id;
    }


    public Long getItem_id()
    {
        return id;
    }

    public String getTitle()
    {
        return title;
    }
    public String getLink()
    {
        return link;
    }
    public String getPubDate()
    {
        return pubDate;
    }
    public String getContent()
    {
        return content;
    }
    public void setId(Long id)
    {
        this.id = id;
    }

    public void setItem_url(String item_url)
    {
        this.item_url = item_url;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }
    public void setSubtitle(String subtitle)
    {
        this.subtitle = subtitle;
    }


    public void setLink(String link)
    {
        this.link = link;
    }

    public void setPubDate(String pubDate)
    {
        this.pubDate = pubDate;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

}