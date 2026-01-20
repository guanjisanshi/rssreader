/*
 Navicat Premium Dump SQL

 Source Server         : rss
 Source Server Type    : SQLite
 Source Server Version : 3045000 (3.45.0)
 Source Schema         : main

 Target Server Type    : SQLite
 Target Server Version : 3045000 (3.45.0)
 File Encoding         : 65001

 Date: 10/01/2026 23:34:39
*/

PRAGMA foreign_keys = false;

-- ----------------------------
-- Table structure for feed
-- ----------------------------
DROP TABLE IF EXISTS "feed";
CREATE TABLE "feed" (
  "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  "title" TEXT,
  "url" TEXT,
  CONSTRAINT "url" UNIQUE ("url" COLLATE BINARY ASC) ON CONFLICT IGNORE
);

DROP TABLE IF EXISTS "item";
CREATE TABLE "item" (
                        "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                        "title" TEXT,
                        "subtitle" TEXT,
                        "link" TEXT,
                        "pubdate" TEXT,
                        "content" TEXT,
                        "item_url" TEXT,
                        CONSTRAINT "link" UNIQUE ("link" COLLATE BINARY ASC) ON CONFLICT IGNORE
);
-- ----------------------------
-- Auto increment value for feed
-- ----------------------------

PRAGMA foreign_keys = true;
