# 冥想模块 (ruoyi-meditation)

## 概述
冥想模块是若依框架的扩展模块，提供冥想相关的业务功能，包括音频、系列、文章、收藏等管理。

## 主要功能

### 1. 音频管理 (Track)
- 音频列表查询
- 音频详情查询
- 音频播放记录

### 2. 系列管理 (Series)
- 系列列表查询
- 系列详情查询
- 系列进度管理

### 3. 文章管理 (Article)
- 文章列表查询
- 文章详情查询

### 4. 收藏管理 (Favorite)
- 收藏列表查询
- 添加收藏
- 取消收藏
- **检查收藏状态** ✅ 新增

### 5. 分类管理 (Category)
- 分类列表查询
- 分类详情查询

### 6. 播放历史 (PlayHistory)
- 播放历史记录
- 播放历史查询

## 新增接口

### 检查收藏状态
- **接口路径**: `GET /meditation/favorite/check`
- **请求参数**:
  - `trackId`: 目标ID (Long)
  - `type`: 目标类型 (String) - 支持 "track", "series", "article"
- **返回结果**: `R<Boolean>` - true表示已收藏，false表示未收藏
- **使用场景**: 前端需要判断用户是否已收藏某个内容，用于显示收藏状态

## 技术特点
- 基于若依框架开发
- 支持多租户
- 使用MyBatis-Plus进行数据访问
- 支持软删除
- 完整的CRUD操作

## 数据库表
- `mg_track` - 音频表
- `mg_series` - 系列表
- `mg_article` - 文章表
- `mg_favorite` - 收藏表
- `mg_category` - 分类表
- `mg_play_history` - 播放历史表

## 更新日志
- 2024-08-25: 新增检查收藏状态接口 `/meditation/favorite/check`

