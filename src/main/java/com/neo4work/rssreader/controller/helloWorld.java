package com.neo4work.rssreader.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class helloWorld
{
    @RequestMapping(value = "hello")
    public String hello()
    {
        System.out.println("Hello World");
        return "Hello World";
    }
}