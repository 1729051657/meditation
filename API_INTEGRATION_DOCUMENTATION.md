# 冥想小程序 API 对接文档

## 概述
本文档详细说明了冥想小程序与后端API的对接情况，包括所有已对接的接口、数据字段映射以及使用说明。

## 基础配置

### API基础URL
```javascript
// 文件位置: /meditation-uniapp/common/config.js
baseUrl: 'https://thoughtadmin.bawutech.com/prod-api/'
```

### 请求工具
```javascript
// 文件位置: /meditation-uniapp/utils/request.js
- GET请求: get(url, params)
- POST请求: post(url, data)
- PUT请求: put(url, data)  
- DELETE请求: del(url, data)
```

## API接口清单

### 1. 首页数据接口

#### 接口信息
- **路径**: `/meditation/home/data`
- **方法**: GET
- **权限**: 无需登录（@SaIgnore）

#### 返回数据结构
```json
{
  "code": 200,
  "data": {
    "greeting": "早上好",
    "categoryVoList": [
      {
        "id": 1,
        "name": "放松减压",
        "description": "帮助缓解压力和焦虑",
        "icon": "icon_url",
        "orderIndex": 1,
        "status": 0
      }
    ],
    "seriesVoList": [
      {
        "id": 1,
        "categoryId": 1,
        "title": "7天冥想入门",
        "subtitle": "适合初学者",
        "cover": "cover_url",
        "banner": "banner_url",
        "intro": "系列介绍",
        "episodeCount": 7,
        "totalDuration": 3600,
        "playCount": 1000,
        "status": 0
      }
    ],
    "trackVoList": [
      {
        "id": 1,
        "seriesId": 1,
        "categoryId": 1,
        "title": "呼吸冥想",
        "cover": "cover_url",
        "audioUrl": "audio_url",
        "durationSec": 600,
        "playCount": 500,
        "status": 0
      }
    ],
    "articleVoList": [
      {
        "id": 1,
        "title": "冥想的科学原理",
        "summary": "文章摘要",
        "content": "文章内容",
        "cover": "cover_url",
        "author": "作者",
        "viewCount": 200,
        "status": 0
      }
    ]
  }
}
```

#### 使用示例
```javascript
// 文件位置: /meditation-uniapp/pages/home/index.vue
import { getHomeData } from '@/api/home'

async loadHomeData() {
  const res = await getHomeData()
  if (res.code === 200 && res.data) {
    this.greeting = res.data.greeting
    this.categories = res.data.categoryVoList
    this.meditationSlots = res.data.seriesVoList
    this.recommendItems = res.data.trackVoList
    this.knowledgeItems = res.data.articleVoList
  }
}
```

### 2. 分类接口

#### 获取分类列表
- **路径**: `/meditation/category/list`
- **方法**: GET
- **权限**: 无需登录
- **参数**: 
  - `status`: 状态（0-正常，1-停用）
  - `name`: 分类名称（模糊查询）

#### 获取分类详情
- **路径**: `/meditation/category/{id}`
- **方法**: GET
- **权限**: 需要权限 `meditation:category:query`

### 3. 系列接口

#### 获取系列列表
- **路径**: `/meditation/series/list`
- **方法**: GET
- **权限**: 无需登录
- **参数**:
  - `categoryId`: 分类ID
  - `status`: 状态
  - `pageNum`: 页码
  - `pageSize`: 每页数量

#### 获取系列详情
- **路径**: `/meditation/series/{id}`
- **方法**: GET
- **权限**: 无需登录

#### 使用示例
```javascript
// 文件位置: /meditation-uniapp/pages/series/detail.vue
import { getSeries } from '@/api/series'
import { listTracks } from '@/api/track'

async onLoad(query) {
  this.id = Number(query.id)
  const [s, ts] = await Promise.all([
    getSeries(this.id),
    listTracks({ 
      seriesId: this.id, 
      status: 0, 
      orderByColumn: 'order_index', 
      isAsc: 'asc',
      pageNum: 1,
      pageSize: 100 
    })
  ])
  this.series = s.data
  this.tracks = ts.rows || ts.data || []
}
```

### 4. 单集接口

#### 获取单集列表
- **路径**: `/meditation/track/list`
- **方法**: GET
- **权限**: 无需登录
- **参数**:
  - `seriesId`: 系列ID
  - `categoryId`: 分类ID
  - `status`: 状态
  - `pageNum`: 页码
  - `pageSize`: 每页数量
  - `orderByColumn`: 排序字段
  - `isAsc`: 升序/降序

#### 获取单集详情
- **路径**: `/meditation/track/{id}`
- **方法**: GET
- **权限**: 无需登录

#### 记录播放历史
- **路径**: `/meditation/playHistory`
- **方法**: POST
- **权限**: 需要登录
- **参数**:
  ```json
  {
    "trackId": 1
  }
  ```

### 5. 文章接口

#### 获取文章列表
- **路径**: `/meditation/article/list`
- **方法**: GET
- **权限**: 无需登录
- **参数**:
  - `status`: 状态
  - `pageNum`: 页码
  - `pageSize`: 每页数量

#### 获取文章详情
- **路径**: `/meditation/article/{id}`
- **方法**: GET
- **权限**: 无需登录

#### 使用示例
```javascript
// 文件位置: /meditation-uniapp/pages/article/detail.vue
import { getArticle } from '@/api/article'

async onLoad(query) {
  this.id = Number(query.id)
  const res = await getArticle(this.id)
  this.article = res.data
}
```

### 6. 收藏接口

#### 获取收藏列表
- **路径**: `/meditation/favorite/list`
- **方法**: GET
- **权限**: 需要登录
- **参数**:
  - `pageNum`: 页码
  - `pageSize`: 每页数量

#### 添加收藏
- **路径**: `/meditation/favorite`
- **方法**: POST
- **权限**: 需要登录
- **参数**:
  ```json
  {
    "trackId": 1,
    "type": "track"
  }
  ```

#### 取消收藏
- **路径**: `/meditation/favorite/{trackId}`
- **方法**: DELETE
- **权限**: 需要登录

#### 检查收藏状态
- **路径**: `/meditation/favorite/check`
- **方法**: GET
- **权限**: 需要登录
- **参数**:
  - `trackId`: 单集ID
  - `type`: 类型（track/series）

### 7. 播放历史接口

#### 获取播放历史
- **路径**: `/meditation/playHistory/list`
- **方法**: GET
- **权限**: 需要登录
- **参数**:
  - `pageNum`: 页码
  - `pageSize`: 每页数量

#### 添加播放记录
- **路径**: `/meditation/playHistory`
- **方法**: POST
- **权限**: 需要登录
- **参数**:
  ```json
  {
    "trackId": 1
  }
  ```

#### 更新播放进度
- **路径**: `/meditation/playHistory`
- **方法**: PUT
- **权限**: 需要登录
- **参数**:
  ```json
  {
    "id": 1,
    "playedDuration": 300,
    "totalDuration": 600
  }
  ```

### 8. 搜索接口

#### 搜索单集
- **路径**: `/meditation/search/tracks`
- **方法**: GET
- **权限**: 无需登录
- **参数**:
  - `keyword`: 关键词
  - `pageNum`: 页码
  - `pageSize`: 每页数量

#### 搜索系列
- **路径**: `/meditation/search/series`
- **方法**: GET
- **权限**: 无需登录
- **参数**:
  - `keyword`: 关键词
  - `pageNum`: 页码
  - `pageSize`: 每页数量

#### 搜索文章
- **路径**: `/meditation/search/articles`
- **方法**: GET
- **权限**: 无需登录
- **参数**:
  - `keyword`: 关键词
  - `pageNum`: 页码
  - `pageSize`: 每页数量

#### 获取热门关键词
- **路径**: `/meditation/hotKeyword/list`
- **方法**: GET
- **权限**: 无需登录
- **参数**:
  - `status`: 状态

#### 添加搜索历史
- **路径**: `/meditation/searchHistory`
- **方法**: POST
- **权限**: 需要登录
- **参数**:
  ```json
  {
    "keyword": "冥想"
  }
  ```

#### 使用示例
```javascript
// 文件位置: /meditation-uniapp/pages/search/index.vue
import { listHot, addHistory, searchTracks, searchSeries, searchArticles } from '@/api/search'

async searchAll(kw) {
  const [ts, ss, as] = await Promise.all([
    searchTracks(kw, { pageNum: 1, pageSize: 20 }),
    searchSeries(kw, { pageNum: 1, pageSize: 20 }),
    searchArticles(kw, { pageNum: 1, pageSize: 20 })
  ])
  this.tracks = ts.rows || ts.data || []
  this.series = ss.rows || ss.data || []
  this.articles = as.rows || as.data || []
}
```

## 数据字段映射

### 系列（Series）字段映射
| 后端字段 | 前端字段 | 说明 |
|---------|---------|------|
| id | id | 系列ID |
| categoryId | categoryId | 分类ID |
| title | title | 标题 |
| subtitle | subtitle | 副标题 |
| cover | cover | 封面图片OSS ID |
| banner | banner | 横幅图片OSS ID |
| intro | intro | 简介 |
| episodeCount | episodeCount | 集数 |
| totalDuration | totalDuration | 总时长（秒） |
| playCount | playCount | 播放次数 |
| status | status | 状态（0-正常，1-停用） |

### 单集（Track）字段映射
| 后端字段 | 前端字段 | 说明 |
|---------|---------|------|
| id | id | 单集ID |
| seriesId | seriesId | 所属系列ID |
| categoryId | categoryId | 分类ID |
| title | title | 标题 |
| cover | cover | 封面图片OSS ID |
| audioUrl | audioUrl | 音频文件URL |
| durationSec | durationSec | 时长（秒） |
| playCount | playCount | 播放次数 |
| orderIndex | orderIndex | 排序索引 |
| status | status | 状态 |

### 文章（Article）字段映射
| 后端字段 | 前端字段 | 说明 |
|---------|---------|------|
| id | id | 文章ID |
| title | title | 标题 |
| summary | summary | 摘要 |
| content | content | 内容（富文本） |
| cover | cover | 封面图片OSS ID |
| author | author | 作者 |
| viewCount | viewCount | 浏览次数 |
| status | status | 状态 |

### 分类（Category）字段映射
| 后端字段 | 前端字段 | 说明 |
|---------|---------|------|
| id | id | 分类ID |
| name | name | 分类名称 |
| description | description | 描述 |
| icon | icon | 图标OSS ID |
| orderIndex | orderIndex | 排序索引 |
| status | status | 状态 |

## OSS文件访问

所有OSS文件ID需要通过以下方式转换为可访问的URL：

```javascript
// 工具方法
oss(id) { 
  return id ? `${this.$baseUrl}/system/oss/download/${id}` : '' 
}

// 使用示例
<image :src="oss(article.cover)" />
```

## 认证与权限

### 登录认证
- 小程序使用微信登录，通过 `wx.login` 获取code
- 后端通过code换取openid和session_key
- 返回的token存储在本地，用于后续请求认证

### 请求头
```javascript
{
  'Authorization': 'Bearer ' + token,
  'Content-Type': 'application/json;charset=UTF-8',
  'tenant-id': tenantId,  // 租户ID
  'dept-id': deptId,      // 部门ID
  'clientid': clientId    // 客户端ID
}
```

## 分页规范

所有列表接口统一使用以下分页参数：
- `pageNum`: 页码，从1开始
- `pageSize`: 每页数量，默认10

返回格式：
```json
{
  "code": 200,
  "data": {
    "rows": [],    // 数据列表
    "total": 100   // 总数量
  }
}
```

## 错误处理

### 错误码
- 200: 成功
- 401: 未授权/登录过期
- 403: 无权限
- 404: 资源不存在
- 500: 服务器错误

### 错误响应格式
```json
{
  "code": 401,
  "msg": "登录状态已过期"
}
```

## 静态数据说明

### 已使用API的页面
1. **首页** (`/pages/home/index.vue`) - ✅ 完全对接
2. **分类页** (`/pages/category/index.vue`) - ✅ 完全对接
3. **系列详情** (`/pages/series/detail.vue`) - ✅ 完全对接
4. **播放器** (`/pages/player/index.vue`) - ✅ 完全对接
5. **文章详情** (`/pages/article/detail.vue`) - ✅ 完全对接
6. **搜索页** (`/pages/search/index.vue`) - ✅ 完全对接
7. **收藏页** (`/pages/favorites/index.vue`) - ✅ 完全对接
8. **历史记录** (`/pages/history/index.vue`) - ✅ 完全对接
9. **个人中心** (`/pages/me/index.vue`) - ✅ 完全对接

### 仍使用静态数据的页面
1. **常见问题** (`/pages/faq/index.vue`) - ⚠️ 使用静态数据
   - 原因：后端暂无FAQ相关接口
   - 建议：后续开发FAQ管理功能

## API服务文件结构

```
/meditation-uniapp/api/
├── index.js          # API统一导出
├── auth.js           # 认证相关
├── home.js           # 首页数据
├── category.js       # 分类管理
├── series.js         # 系列管理
├── track.js          # 单集管理
├── article.js        # 文章管理
├── banner.js         # 横幅管理
├── favorite.js       # 收藏管理
├── play.js           # 播放记录
├── recommend.js      # 推荐管理
├── search.js         # 搜索功能
├── faq.js           # 常见问题（预留）
└── common.js         # 通用接口
```

## 注意事项

1. **图片处理**: 所有图片字段返回的是OSS文件ID，需要通过 `oss()` 方法转换为URL
2. **时长处理**: 音频时长统一使用秒为单位，前端根据需要转换显示格式
3. **状态值**: 0表示正常/启用，1表示停用/禁用
4. **权限控制**: 带有 `@SaIgnore` 注解的接口无需登录，其他接口需要登录认证
5. **分页处理**: 列表数据可能在 `res.rows` 或 `res.data` 中，需要兼容处理

## 后续优化建议

1. **FAQ功能开发**
   - 开发后端FAQ管理接口
   - 支持FAQ分类和排序
   - 支持FAQ搜索功能

2. **性能优化**
   - 实现接口数据缓存
   - 优化图片懒加载
   - 实现分页预加载

3. **用户体验**
   - 添加加载骨架屏
   - 优化错误提示
   - 实现离线缓存

4. **数据统计**
   - 添加用户行为统计
   - 实现播放时长统计
   - 添加内容推荐算法

## 联系方式

如有问题或需要支持，请联系开发团队。

---

文档更新日期：2025-01-13
版本：1.0.0