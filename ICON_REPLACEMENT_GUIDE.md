# 图标替换指南

## 完成时间
2025-01-13

## 已完成的图标替换

### 1. 个人中心页面 (`/pages/me/index.vue`) ✅

#### 替换内容
| 原图片 | 新图标组件 | 说明 |
|--------|-----------|------|
| `/static/me/favorites@3x.png` | `<tn-icon name="like-fill" size="60" color="#79B1DA">` | 我的收藏图标 |
| `/static/me/recent@3x.png` | `<tn-icon name="time-fill" size="60" color="#79B1DA">` | 最近播放图标 |
| `/static/me/faq@3x.png` | `<tn-icon name="help-fill" size="48" color="#79B1DA">` | 常见问题图标 |
| `/static/me/logout@3x.png` | `<tn-icon name="logout" size="48" color="#79B1DA">` | 退出登录图标 |
| `/static/me/arrow@3x.png` | `<tn-icon name="right" size="32" color="#C0C0C0">` | 右箭头图标 |

#### 样式调整
```scss
// 移除了固定宽高，因为图标组件自带尺寸控制
.card-icon {
  margin-bottom: 16rpx;
  // width: 56rpx;  // 已移除
  // height: 56rpx; // 已移除
}
```

## 待替换的图标

### 2. 首页 (`/pages/home/index.vue`)

| 位置 | 原图片 | 建议图标 | 代码示例 |
|------|--------|---------|---------|
| 播放按钮 | `/static/home/play@3x.png` | play-fill | `<tn-icon name="play-fill" size="60" color="#FFFFFF">` |
| 搜索图标 | 搜索框图标 | search | `<tn-icon name="search" size="40" color="#999">` |
| 分类图标-放松 | `/static/home/relax-stress@2x.png` | spa | `<tn-icon name="spa" size="48" color="#79B1DA">` |
| 分类图标-睡眠 | `/static/home/improve-sleep@2x.png` | moon | `<tn-icon name="moon" size="48" color="#79B1DA">` |
| 分类图标-专注 | `/static/home/improve-focus@2x.png` | focus | `<tn-icon name="focus" size="48" color="#79B1DA">` |
| 分类图标-情绪 | `/static/home/emotion-regulation@2x.png` | happy | `<tn-icon name="happy" size="48" color="#79B1DA">` |

### 3. 分类页面 (`/pages/category/index.vue`)

| 位置 | 原图片 | 建议图标 | 代码示例 |
|------|--------|---------|---------|
| 分类芯片 | 网络图片URL | apps | `<tn-icon name="apps" size="32" color="#79B1DA">` |
| 播放图标 | 网络图片URL | play-circle | `<tn-icon name="play-circle" size="28" color="#79B1DA">` |

### 4. 收藏页面 (`/pages/favorites/index.vue`)

| 位置 | 原文本图标 | 建议图标 | 代码示例 |
|------|-----------|---------|---------|
| 收藏图标 | `<text class="tn-icon-like-fill">` | like-fill | `<tn-icon name="like-fill" size="36" color="#FF6B6B">` |

### 5. 播放器页面 (`/pages/player/index.vue`)

已使用TuniaoUI图标类（text方式），可以考虑改为组件方式：

| 位置 | 当前使用 | 建议改为 |
|------|---------|---------|
| 上一曲 | `<text class="tn-icon-skip-previous-fill">` | `<tn-icon name="skip-previous-fill" size="48" color="#333">` |
| 播放/暂停 | `<text class="tn-icon-play-circle-fill">` | `<tn-icon name="play-circle-fill" size="72" color="#79B1DA">` |
| 下一曲 | `<text class="tn-icon-skip-next-fill">` | `<tn-icon name="skip-next-fill" size="48" color="#333">` |

## TuniaoUI 常用图标列表

### 媒体控制类
- `play` - 播放
- `play-fill` - 播放（填充）
- `play-circle` - 播放（圆形）
- `play-circle-fill` - 播放（圆形填充）
- `pause` - 暂停
- `pause-circle-fill` - 暂停（圆形填充）
- `skip-previous-fill` - 上一曲
- `skip-next-fill` - 下一曲
- `stop` - 停止

### 功能图标类
- `like` - 收藏（空心）
- `like-fill` - 收藏（实心）
- `time` - 时间
- `time-fill` - 时间（填充）
- `list` - 列表
- `apps` - 应用/分类
- `search` - 搜索
- `share` - 分享
- `download` - 下载
- `settings` - 设置

### 导航类
- `home` - 首页
- `home-fill` - 首页（填充）
- `left` - 左箭头
- `right` - 右箭头
- `up` - 上箭头
- `down` - 下箭头
- `back` - 返回
- `close` - 关闭

### 状态类
- `success` - 成功
- `warning` - 警告
- `error` - 错误
- `info` - 信息
- `help` - 帮助
- `help-fill` - 帮助（填充）

## 使用方法

### 1. 组件方式（推荐）
```vue
<tn-icon 
  name="图标名称" 
  size="尺寸(rpx)" 
  color="颜色值"
  :bold="是否加粗"
></tn-icon>
```

### 2. 类名方式
```vue
<text class="tn-icon-图标名称"></text>
```

## 颜色建议

- **主色调**: `#79B1DA` (冥想蓝)
- **强调色**: `#FF6B6B` (收藏红)
- **次要色**: `#6B7280` (灰色)
- **背景色**: `#FFFFFF` (白色)
- **禁用色**: `#C0C0C0` (浅灰)

## 尺寸建议

- **超大图标**: 72rpx (主要操作按钮)
- **大图标**: 60rpx (功能卡片)
- **中图标**: 48rpx (菜单项)
- **小图标**: 36rpx (列表项)
- **迷你图标**: 28rpx (辅助说明)

## 注意事项

1. **性能优化**: 图标组件比图片加载更快，减少网络请求
2. **一致性**: 使用统一的图标库保证视觉一致性
3. **可维护性**: 通过props控制样式，便于统一修改
4. **无障碍**: 图标组件支持aria-label等无障碍属性
5. **响应式**: 图标大小使用rpx单位，自适应不同屏幕

## 迁移步骤

1. **查找图片引用**
   ```bash
   grep -r "static.*\.png" pages/
   grep -r "http.*\.png" pages/
   ```

2. **替换为图标组件**
   - 移除`<image>`标签
   - 添加`<tn-icon>`组件
   - 调整相关样式

3. **测试验证**
   - 检查图标显示效果
   - 验证点击交互
   - 确认样式适配

## 后续优化

1. **图标主题化**: 创建图标主题配置文件，统一管理颜色和尺寸
2. **自定义图标**: 如需特殊图标，可扩展TuniaoUI图标库
3. **动画效果**: 为部分图标添加过渡动画，提升用户体验
4. **暗黑模式**: 配置暗黑模式下的图标颜色方案

---

完成人：AI Assistant
更新日期：2025-01-13