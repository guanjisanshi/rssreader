package com.neo4work.rssreader.controller;

import com.neo4work.rssreader.db.dbUserGet;
import com.neo4work.rssreader.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    // 登录API
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {
        try {
            // 根据用户名获取用户信息
            User existingUser = dbUserGet.doGetByUsername(user.getUsername());
            if (existingUser == null) {
                return ResponseEntity.status(401).body(null);
            }
            
            // 验证密码（这里简化处理，实际应该使用加密密码）
            if (!existingUser.getPassword().equals(user.getPassword())) {
                return ResponseEntity.status(401).body(null);
            }
            
            // 登录成功，返回用户信息（实际应该返回token）
            return ResponseEntity.ok(existingUser);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
    
    // 注销API
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        try {
            // 注销成功（实际应该清除token）
            return ResponseEntity.ok("注销成功");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("注销失败: " + e.getMessage());
        }
    }
}