# 分类页面数据对接指南

## 更新内容

已经完成了 `pages/category/index` 页面的数据对接和优化，主要包括：

### 1. 数据加载优化
- ✅ 修复了分类数据加载逻辑
- ✅ 实现了系列（Series）数据的动态加载
- ✅ 实现了单集（Tracks）数据的动态加载
- ✅ 添加了分页功能支持
- ✅ 优化了并发请求，使用 Promise.all 提高加载效率

### 2. 分类切换联动
- ✅ 实现了点击分类标签时自动切换数据
- ✅ 避免重复点击同一分类时的无效请求
- ✅ 切换分类时重置分页状态

### 3. 用户交互优化
- ✅ 添加了"加载更多"功能
- ✅ 实现了下拉刷新功能
- ✅ 优化了空状态显示逻辑
- ✅ 添加了详细的控制台日志，方便调试

### 4. Mock 数据支持
- ✅ 创建了完整的模拟数据 (`utils/mockData.js`)
- ✅ 支持在后端未启动时使用模拟数据开发
- ✅ 提供了配置文件方便切换真实API和模拟数据

## 使用方法

### 1. 使用真实API（默认）
确保后端服务已启动，数据会自动从后端API加载。

### 2. 使用模拟数据
如果后端服务未启动，可以临时使用模拟数据：

1. 打开 `/workspace/meditation-uniapp/common/config.mock.js`
2. 将 `USE_MOCK_DATA` 设置为 `true`
```javascript
export const USE_MOCK_DATA = true // 使用模拟数据
```

### 3. 数据结构说明

#### 分类数据 (Category)
```javascript
{
  id: 1,
  name: '情绪调节',
  description: '帮助您管理和调节情绪',
  status: 0,
  orderNum: 1
}
```

#### 系列数据 (Series)
```javascript
{
  id: 1,
  categoryId: 1,
  title: '情绪平衡冥想系列',
  subtitle: '7天情绪调节计划',
  coverUrl: '/static/images/meditation1.jpg',
  episodeCount: 7,
  status: '0'
}
```

#### 单集数据 (Track)
```javascript
{
  id: 1,
  seriesId: 1,
  categoryId: 1,
  title: '认识你的情绪',
  coverUrl: '/static/images/track1.jpg',
  durationSec: 600,
  status: 0
}
```

## API 接口

### 1. 获取分类列表
- **接口**: `/meditation/category/list`
- **参数**: `{ status: 0 }`
- **返回**: 分类列表

### 2. 获取系列列表
- **接口**: `/meditation/series/list`
- **参数**: 
  ```javascript
  {
    categoryId: 1,    // 分类ID
    status: '0',      // 状态（注意是字符串）
    pageNum: 1,       // 页码
    pageSize: 10      // 每页数量
  }
  ```

### 3. 获取单集列表
- **接口**: `/meditation/track/list`
- **参数**:
  ```javascript
  {
    categoryId: 1,    // 分类ID
    status: 0,        // 状态（整数）
    pageNum: 1,       // 页码
    pageSize: 10      // 每页数量
  }
  ```

## 功能特性

### 1. 分页加载
- 系列和单集都支持分页加载
- 默认每页加载10条数据
- 点击"加载更多"按钮加载下一页

### 2. 下拉刷新
- 下拉页面可以刷新所有数据
- 刷新时会重置分页状态

### 3. 分类切换
- 点击顶部分类标签切换不同分类
- 切换时自动加载对应分类的数据
- 切换时重置分页，从第一页开始

### 4. 空状态处理
- 当没有数据时显示友好的空状态提示
- 加载中不显示空状态

## 调试技巧

### 1. 查看控制台日志
页面添加了详细的日志输出，可以在浏览器控制台查看：
- 页面加载日志
- API请求参数
- API响应数据
- 数据解析结果

### 2. 使用Mock数据调试
当后端服务不可用时，可以启用Mock数据继续开发：
- 修改 `config.mock.js` 中的配置
- Mock数据会模拟真实的API响应格式
- 支持分页和分类筛选

### 3. 检查网络请求
在浏览器开发者工具的Network标签中可以查看：
- API请求是否正确发送
- 请求参数是否正确
- 响应数据格式是否符合预期

## 注意事项

1. **状态类型差异**：
   - Series的status是String类型（'0'）
   - Track的status是Integer类型（0）

2. **图片资源**：
   - 如果图片无法显示，检查coverUrl路径是否正确
   - 可以在代码中设置默认图片路径

3. **性能优化**：
   - 使用Promise.all并发加载系列和单集数据
   - 避免不必要的重复请求

## 后续优化建议

1. 添加骨架屏loading效果
2. 实现图片懒加载
3. 添加数据缓存机制
4. 优化列表滚动性能
5. 添加搜索功能
6. 实现收藏功能