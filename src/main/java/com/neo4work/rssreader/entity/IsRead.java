package com.neo4work.rssreader.entity;

import java.io.Serializable;

public class IsRead implements Serializable
{
    private String id;
    private String userIdCode ;
    private String itemLink;
    private Integer star;
    private Integer isRead;

    public IsRead()
    {
        super();
    }

    public IsRead( String userIdCode, String itemLink, Integer star,Integer isRead)
    {
        super();
        this.userIdCode = userIdCode;
        this.itemLink = itemLink;
        this.star = star;
        this.isRead = isRead;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getUserIdCode()
    {
        return userIdCode;
    }

    public void setUserIdCode(String userIdCode)
    {
        this.userIdCode = userIdCode;
    }

    public String getItemLink()
    {
        return itemLink;
    }

    public void setItemLink(String itemLink)
    {
        this.itemLink = itemLink;
    }

    public Integer getStar()
    {
        return star;
    }

    public void setStar(Integer star)
    {
        this.star = star;
    }

    public Integer getIsRead()
    {
        return isRead;
    }

    public void setIsRead(Integer isRead)
    {
        this.isRead = isRead;
    }
}
