package com.neo4work.rssreader.controller;
import com.neo4work.rssreader.db.dbFeedCommit;
import com.neo4work.rssreader.db.dbFeedGet;
import com.neo4work.rssreader.db.dbIsReadGet;
import com.neo4work.rssreader.db.dbItemGet;
import com.neo4work.rssreader.entity.Feed;
import com.neo4work.rssreader.entity.IsRead;
import com.neo4work.rssreader.entity.Item;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.neo4work.rssreader.cron.refreshFeed;

import java.util.List;

@RestController
@RequestMapping("/api/feeds")
public class FeedsController {

    // 获取所有RSS源，包含未读数量
    @GetMapping
    public ResponseEntity<List<?>> getAllFeeds(@RequestParam(required = false) String userIdCode) {
        List<Feed> feeds = dbFeedGet.doGet();
        
        // 处理feeds为null的情况
        if (feeds == null) {
            feeds = java.util.Collections.emptyList();
        }
        
        if (userIdCode != null) {
            // 如果提供了userIdCode，创建包含Feed信息和未读数量的新对象列表
            java.util.List<java.util.Map<String, Object>> feedsWithUnreadCount = new java.util.ArrayList<>();
            for (Feed feed : feeds) {
                java.util.Map<String, Object> feedMap = new java.util.HashMap<>();
                feedMap.put("id", feed.getId());
                feedMap.put("title", feed.getTitle());
                feedMap.put("url", feed.getUrl());
                
                // 获取该订阅源的所有文章
                List<Item> items = dbItemGet.doGetByItemUrl(feed.getUrl());
                // 计算未读数量
                int unreadCount = 0;
                if (items != null) {
                    for (Item item : items) {
                        IsRead isRead = dbIsReadGet.doGetByUserIdAndItemLink(userIdCode, item.getLink());
                        // 如果isRead为null或isRead字段为0，则为未读
                        if (isRead == null || isRead.getIsRead() == 0) {
                            unreadCount++;
                        }
                    }
                }
                feedMap.put("unreadCount", unreadCount);
                
                feedsWithUnreadCount.add(feedMap);
            }
            return ResponseEntity.ok(feedsWithUnreadCount);
        } else {
            // 如果没有提供userIdCode，直接返回Feed列表
            return ResponseEntity.ok(feeds);
        }
    }

    // 获取指定RSS源的文章
    @GetMapping("/items")
    public ResponseEntity<List<?>> getFeedItems(@RequestParam String item_url, @RequestParam(required = false) String userIdCode) {
        List<Item> items = dbItemGet.doGetByItemUrl(item_url);
        
        // 处理items为null的情况
        if (items == null) {
            items = java.util.Collections.emptyList();
        }
        
        // 检查数据库中是否存在isread表
        if (userIdCode != null) {
            // 如果提供了userIdCode，创建包含Item信息和isRead状态的新对象列表
            java.util.List<java.util.Map<String, Object>> itemsWithReadStatus = new java.util.ArrayList<>();
            for (Item item : items) {
                java.util.Map<String, Object> itemMap = new java.util.HashMap<>();
                itemMap.put("id", item.getId());
                itemMap.put("item_url", item.getItem_url());
                itemMap.put("title", item.getTitle());
                itemMap.put("subtitle", item.getSubtitle());
                itemMap.put("link", item.getLink());
                itemMap.put("pubDate", item.getPubDate());
                itemMap.put("content", item.getContent());
                
                // 获取文章的已读状态
                IsRead isRead = dbIsReadGet.doGetByUserIdAndItemLink(userIdCode, item.getLink());
                itemMap.put("isRead", isRead != null ? isRead.getIsRead() : 0);
                
                itemsWithReadStatus.add(itemMap);
            }
            return ResponseEntity.ok(itemsWithReadStatus);
        } else {
            // 如果没有提供userIdCode，直接返回Item列表，前端默认显示为未读
            return ResponseEntity.ok(items);
        }
    }

    // 添加RSS源
    @PostMapping
    public ResponseEntity<Feed> addFeed(@RequestBody Feed feed) {
        try {
            // 检查URL是否已经存在
            Feed existingFeed = dbFeedGet.doGetByUrl(feed.getUrl());
            if (existingFeed != null) {
                return ResponseEntity.badRequest().body(null);
            }
            
            // 保存到数据库
            int result = dbFeedCommit.doCommit(feed);
            if (result > 0) {
                return ResponseEntity.ok(feed);
            } else {
                return ResponseEntity.status(500).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    // 删除RSS源
    @DeleteMapping
    public ResponseEntity<String> deleteFeed(@RequestParam String url) {
        try {
            // 从数据库删除
            int result = dbFeedCommit.doDeleteByUrl(url);
            if (result > 0) {
                return ResponseEntity.ok("RSS源删除成功");
            } else {
                return ResponseEntity.status(404).body("RSS源不存在");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("RSS源删除失败: " + e.getMessage());
        }
    }

    // 刷新指定RSS源
    @PostMapping("/refresh")
    public ResponseEntity<String> refreshFeed(@RequestParam String item_url) {  
        try {
            // 这里可以实现具体的刷新逻辑，暂时返回成功消息
            // 后续可以集成到现有的RSS处理流程中
            new refreshFeed().refreshFeed(item_url);
            return ResponseEntity.ok("RSS源刷新成功");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("RSS源刷新失败: " + e.getMessage());
        }
    }
    

}