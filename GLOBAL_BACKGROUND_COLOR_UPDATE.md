# 全局背景色更新说明

## 更新内容

已成功将整个应用的背景色统一设置为 `#D8E2F0`，包括所有页面和导航栏。

## 具体更改

### 1. 全局样式设置 (App.vue)
- 在 `App.vue` 中添加了全局样式规则
- 设置 `page` 元素的背景色为 `#D8E2F0`
- 确保所有导航栏类（`.uni-navbar`, `.tn-nav-bar`, `.nav-bar`, `.topNav`）使用相同背景色
- 设置通用容器类（`.container`, `.page-container`, `.content`）的背景色

### 2. 导航栏组件更新
所有使用 `tn-nav-bar` 组件的页面都添加了 `backgroundColor="#D8E2F0"` 属性：
- 搜索页面 (`pages/search/index.vue`)
- 系列详情页 (`pages/series/detail.vue`)
- 文章详情页 (`pages/article/detail.vue`)
- 收藏页面 (`pages/favorites/index.vue`)
- 历史记录页 (`pages/history/index.vue`)
- 分类页面 (`pages/category/index.vue`)
- 常见问题页 (`pages/faq/index.vue`)
- 列表页面 (`pages/list/list.vue`)

### 3. 页面背景色更新
更新了以下页面的背景色设置：
- **首页** (`pages/home/index.vue`): 背景色已设置为 `#D8E2F0`
- **播放器页** (`pages/player/index.vue`): `.player-page` 和 `.container` 背景色更新
- **收藏页** (`pages/favorites/index.vue`): `.favorites-page` 背景色从 `#F5F7FA` 改为 `#D8E2F0`
- **历史页** (`pages/history/index.vue`): `.history-page` 背景色从 `#F5F7FA` 改为 `#D8E2F0`
- **FAQ页** (`pages/faq/index.vue`): `.faq-page` 背景色从 `#F5F7FA` 改为 `#D8E2F0`
- **分类页** (`pages/category/index.vue`): 背景从渐变色改为纯色 `#D8E2F0`
- **搜索页** (`pages/search/index.vue`): 背景色已设置为 `#D8E2F0`
- **文章详情页** (`pages/article/detail.vue`): 背景色已设置为 `#D8E2F0`
- **我的页面** (`pages/me/index.vue`): 背景色已设置为 `#D8E2F0`

## 颜色说明

`#D8E2F0` 是一种柔和的浅蓝灰色，具有以下特点：
- RGB: (216, 226, 240)
- 给人以清新、宁静的感觉
- 适合冥想应用的整体氛围
- 与白色内容卡片形成良好对比
- 提供舒适的视觉体验

## 效果预览

更新后，整个应用将呈现统一的视觉风格：
- 所有页面背景色一致
- 导航栏与页面背景无缝融合
- 内容卡片在背景上清晰突出
- 整体视觉体验更加协调统一

## 注意事项

1. 如果某些页面有特殊的背景需求，可以在具体页面中覆盖全局样式
2. 确保所有新增页面也遵循此背景色规范
3. 组件内部的背景色（如卡片、按钮等）保持原有设计，以保证对比度