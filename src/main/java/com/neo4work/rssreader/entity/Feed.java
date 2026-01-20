package com.neo4work.rssreader.entity;

import java.io.Serializable;

public class Feed implements Serializable
{
    private Integer id;
    private String title;
    private String url;

    public Feed()
    {
        super();
    }

    public Feed( String title, String url)
    {
        super();
        this.title = title;
        this.url = url;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }
}
