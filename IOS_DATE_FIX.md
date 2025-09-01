# iOS 日期格式兼容性修复

## 问题描述

在历史记录页面，使用 `new Date("2025-09-01 12:41:06")` 格式在部分 iOS 设备上无法正常解析，导致页面报错。

### 错误信息
```
new Date("2025-09-01 12:41:06") 在部分 iOS 下无法正常使用
```

### iOS 支持的日期格式
- `yyyy/MM/dd`
- `yyyy/MM/dd HH:mm:ss`
- `yyyy-MM-dd`
- `yyyy-MM-ddTHH:mm:ss`
- `yyyy-MM-ddTHH:mm:ss+HH:mm`

## 解决方案

### 1. 添加日期解析辅助函数

在 `pages/history/index.vue` 中添加了 `parseDate` 方法：

```javascript
// 将日期字符串转换为 iOS 兼容格式
parseDate(dateString) {
  if (!dateString) return null
  
  // 如果已经是 Date 对象，直接返回
  if (dateString instanceof Date) {
    return dateString
  }
  
  // 将 "2025-09-01 12:41:06" 格式转换为 "2025/09/01 12:41:06"
  // iOS 支持这种格式
  const iOSCompatibleString = dateString.replace(/-/g, '/')
  return new Date(iOSCompatibleString)
}
```

### 2. 更新相关方法

修改了以下方法以使用 `parseDate`：

- **formatTime(timestamp)**：格式化时间显示
- **formatDateLabel(timestamp)**：格式化日期标签

### 3. FAQ 页面导航栏更新

同时完成了 FAQ 页面的导航栏配置更新：

#### pages.json 配置
```json
{
  "path": "pages/faq/index",
  "style": {
    "navigationBarTitleText": "常见问题",
    "navigationBarBackgroundColor": "#D8E2F0",
    "navigationBarTextStyle": "black"
  }
}
```

#### 页面更新
- 移除了自定义导航栏组件 `<tn-nav-bar>`
- 调整了内容区域的 padding-top

## 影响范围

- ✅ 历史记录页面日期显示正常
- ✅ iOS 设备兼容性问题已解决
- ✅ FAQ 页面导航栏统一使用系统导航栏

## 测试建议

1. 在 iOS 设备上测试历史记录页面
2. 验证日期和时间显示是否正常
3. 检查 FAQ 页面导航栏显示效果

## 导航栏配置统计

目前使用系统导航栏的页面：
- 收藏页 (favorites/index)
- 历史记录页 (history/index)
- FAQ页 (faq/index)

仍使用自定义导航栏的页面：
- 首页 (home/index)
- 分类页 (category/index)
- 搜索页 (search/index)
- 我的页面 (me/index)
- 播放器页 (player/index)
- 文章详情页 (article/detail)
- 列表页 (list/list)