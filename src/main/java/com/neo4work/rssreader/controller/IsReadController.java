package com.neo4work.rssreader.controller;

import com.neo4work.rssreader.db.dbIsReadCommit;
import com.neo4work.rssreader.db.dbIsReadGet;
import com.neo4work.rssreader.entity.IsRead;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class IsReadController {

    // 切换已读状态API
    @PostMapping("/items/toggle-read")
    public ResponseEntity<IsRead> toggleReadStatus(@RequestBody IsRead isRead) {
        try {
            // 根据userIdCode和itemLink获取已读状态
            IsRead existingIsRead = dbIsReadGet.doGetByUserIdAndItemLink(isRead.getUserIdCode(), isRead.getItemLink());
            
            if (existingIsRead == null) {
                // 如果不存在，创建新的已读状态，默认为已读
                IsRead newIsRead = new IsRead(isRead.getUserIdCode(), isRead.getItemLink(), 0, 1);
                int result = dbIsReadCommit.doCommit(newIsRead);
                if (result > 0) {
                    return ResponseEntity.ok(newIsRead);
                } else {
                    return ResponseEntity.status(500).body(null);
                }
            } else {
                // 如果存在，切换已读状态
                existingIsRead.setIsRead(existingIsRead.getIsRead() == 1 ? 0 : 1);
                int result = dbIsReadCommit.doUpdate(existingIsRead);
                if (result > 0) {
                    return ResponseEntity.ok(existingIsRead);
                } else {
                    return ResponseEntity.status(500).body(null);
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}