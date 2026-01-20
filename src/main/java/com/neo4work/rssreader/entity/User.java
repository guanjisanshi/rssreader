package com.neo4work.rssreader.entity;

import java.io.Serializable;

public class User implements Serializable
{
    private Integer id;
    private String username;
    private String password;
    private String userIdCode;

    public User()
    {
        super();
    }

    public User(String username, String password,String userIdCode)
    {
        super();
        this.username = username;
        this.password = password;
        this.userIdCode = userIdCode;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getUserIdCode()
    {
        return userIdCode;
    }

    public void setUserIdCode(String userIdCode)
    {
        this.userIdCode = userIdCode;
    }
}
