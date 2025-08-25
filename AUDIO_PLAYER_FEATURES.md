# 小程序音频播放功能实现文档

## 功能概述

本次更新为小程序的收藏功能添加了完整的音频播放支持，包括背景音频播放、收藏管理和定时关闭功能。

## 主要功能实现

### 1. 后端优化 (FavoriteServiceImpl.java)

#### 解决的问题：
- **N+1 查询问题**：原代码在查询收藏详情时，对每个收藏记录都单独查询关联数据
- **重复收藏问题**：没有检查是否已存在相同的收藏记录
- **数据校验缺失**：缺少必要的数据验证

#### 优化方案：
```java
// 批量查询，避免N+1问题
private List<FavoriteDetailVo> convertToDetailVoBatch(List<FavoriteVo> favoriteVoList) {
    // 按类型分组
    Map<String, List<FavoriteVo>> typeGroupMap = favoriteVoList.stream()
        .collect(Collectors.groupingBy(FavoriteVo::getTargetType));
    
    // 批量查询各类型数据
    // 使用selectVoBatchIds一次性查询所有需要的数据
    // 将结果缓存到Map中，通过ID快速查找
}
```

### 2. 小程序收藏页面 (favorites/index.vue)

#### 新增功能：

##### 2.1 背景音频播放
- 使用 `uni.getBackgroundAudioManager()` 实现背景音频播放
- 支持小程序后台播放，用户切换页面音频不会中断
- 兼容处理：非小程序环境使用 `uni.createInnerAudioContext()`

```javascript
// 初始化背景音频管理器
initBackgroundAudio() {
    // #ifdef MP-WEIXIN || MP-BAIDU || MP-QQ
    this.backgroundAudioManager = uni.getBackgroundAudioManager()
    // 设置音频信息
    this.backgroundAudioManager.title = item.title
    this.backgroundAudioManager.singer = item.author
    this.backgroundAudioManager.coverImgUrl = item.cover
    this.backgroundAudioManager.src = item.audioUrl
    // #endif
}
```

##### 2.2 收藏状态切换
- 点击收藏图标可以切换收藏/取消收藏状态
- 取消收藏时会弹出确认对话框
- 如果正在播放的音频被取消收藏，会自动停止播放

##### 2.3 播放控制栏
- 页面底部显示正在播放的音频信息
- 包含播放/暂停按钮
- 动画效果显示播放状态
- 点击可跳转到播放器页面

##### 2.4 播放列表管理
- 自动播放下一首（循环播放）
- 点击列表项直接播放
- 正在播放的项目高亮显示

### 3. 定时关闭组件 (SleepTimer/index.vue)

#### 功能特点：
- 独立的可复用组件
- 预设时间选项：5、10、15、30、45、60、90、120分钟
- 支持自定义时间输入（1-999分钟）
- 实时显示剩余时间
- 最后10秒倒计时提醒
- 可随时取消定时

#### 使用方式：
```vue
<sleep-timer 
  :audioManager="backgroundAudioManager" 
  @timer-start="onTimerStart" 
  @timer-end="onTimerEnd" 
/>
```

### 4. API 接口优化

#### 更新的接口：
- `listFavoritesDetail`: 获取收藏详情列表（只查询音频类型）
- `addFavorite`: 添加收藏（使用targetId和targetType参数）
- `removeFavorite`: 取消收藏（使用favoriteId参数）
- `checkFavoriteStatus`: 检查收藏状态

## 用户体验优化

### 1. 视觉反馈
- 正在播放的音频项目有特殊样式标识
- 播放动画显示音频播放状态
- 收藏图标颜色变化表示收藏状态

### 2. 交互优化
- 点击音频项目直接播放，无需跳转
- 再次点击可暂停/继续播放
- 支持后台播放，切换页面不中断

### 3. 性能优化
- 批量查询减少数据库访问次数
- 使用缓存避免重复查询
- 优化查询语句（添加LIMIT等）

## 技术实现细节

### 条件编译
使用 uni-app 的条件编译功能，确保代码在不同平台正确运行：
```javascript
// #ifdef MP-WEIXIN || MP-BAIDU || MP-QQ
// 小程序专用代码
// #endif

// #ifndef MP-WEIXIN || MP-BAIDU || MP-QQ  
// 非小程序平台代码
// #endif
```

### 事件监听
完整的音频事件监听，确保状态同步：
- `onPlay`: 播放开始
- `onPause`: 暂停
- `onStop`: 停止
- `onEnded`: 播放结束
- `onError`: 播放错误

## 使用说明

### 用户操作流程：
1. 进入"我的收藏"页面
2. 点击音频项目开始播放
3. 底部出现播放控制栏
4. 可以暂停/继续播放
5. 点击定时器图标设置定时关闭
6. 点击收藏图标取消收藏

### 注意事项：
1. 小程序需要配置相应的权限才能使用背景音频
2. 音频URL需要是HTTPS协议
3. 建议音频文件不要过大，影响加载速度

## 后续优化建议

1. **播放模式**：添加单曲循环、随机播放等模式
2. **播放速度**：支持调节播放速度（0.5x、1.5x、2x等）
3. **断点续播**：记录播放进度，下次打开继续播放
4. **歌词显示**：如果有文字内容，可以同步显示
5. **缓存优化**：缓存已播放的音频，减少网络请求
6. **播放统计**：记录用户播放行为，提供个性化推荐

## 相关文档参考

- [uni-app 背景音频管理器文档](https://uniapp.dcloud.net.cn/api/media/background-audio-manager.html)
- [微信小程序背景音频API](https://developers.weixin.qq.com/miniprogram/dev/api/media/background-audio/wx.getBackgroundAudioManager.html)