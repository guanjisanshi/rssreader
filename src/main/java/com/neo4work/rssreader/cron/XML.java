package com.neo4work.rssreader.cron;

import org.dom4j.DocumentHelper;

public class XML
{
    public static boolean isXML(String xml)
    {
        try
        {
            DocumentHelper.parseText(xml);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
}
