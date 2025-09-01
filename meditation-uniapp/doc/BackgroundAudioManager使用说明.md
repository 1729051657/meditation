# BackgroundAudioManager 使用说明

## 重要提示

根据 uni-app 官方文档，使用 `BackgroundAudioManager` 时必须注意以下几点：

### 1. 必填属性

- **`title`**: 音频标题（必填）
- **`src`**: 音频地址（必填）

### 2. 选填属性

- `epname`: 专辑名
- `singer`: 歌手名
- `coverImgUrl`: 封面图 URL
- `webUrl`: 页面链接
- `protocol`: 音频协议

### 3. 正确的设置顺序

**重要**：必须先设置 `title`，然后再设置 `src`，否则可能导致属性设置失败。

```javascript
// ✅ 正确的顺序
audioContext.title = '音频标题'  // 先设置 title
audioContext.singer = '歌手名'   // 设置其他选填属性
audioContext.coverImgUrl = '封面图URL'
audioContext.src = '音频地址'    // 最后设置 src

// ❌ 错误的顺序
audioContext.src = '音频地址'    // 先设置 src
audioContext.title = '音频标题'  // 后设置 title，可能无效
```

### 4. 实际应用示例

```javascript
// 在播放器页面设置音频信息
async loadTrack() {
  const track = await getTrackDetail(this.trackId)
  
  if (track.audioUrl) {
    // 如果有正在播放的音频，先停止
    if (this.audioContext.src) {
      this.audioContext.stop()
    }
    
    // 按正确顺序设置属性
    this.audioContext.title = track.title || '默认标题'        // 必填
    this.audioContext.epname = '专辑名'                        // 选填
    this.audioContext.singer = track.artist || '未知歌手'       // 选填
    this.audioContext.coverImgUrl = track.coverUrl || '默认封面' // 选填
    
    // 最后设置 src，这会触发音频加载
    this.audioContext.src = track.audioUrl
    
    // 保存信息供其他组件使用
    uni.setStorageSync('currentTrackId', track.id)
  }
}
```

### 5. 迷你播放器获取信息

```javascript
// 在迷你播放器组件中获取播放信息
initAudioContext() {
  const audioContext = uni.getBackgroundAudioManager()
  
  if (audioContext && audioContext.src) {
    // 音频信息已经在播放器页面设置好
    this.currentTrack = {
      title: audioContext.title,         // 获取标题
      coverUrl: audioContext.coverImgUrl, // 获取封面
      audioUrl: audioContext.src,         // 获取音频地址
      isPlaying: !audioContext.paused     // 获取播放状态
    }
  }
}
```

### 6. 常见问题

#### Q: 为什么迷你播放器显示"未知音频"？
A: 可能是因为设置属性的顺序不对。确保在设置 `src` 之前先设置 `title`。

#### Q: 为什么音频信息没有同步到系统通知栏？
A: 检查是否正确设置了 `title`、`singer`、`coverImgUrl` 等属性。

#### Q: 切换音频时需要注意什么？
A: 切换前最好先调用 `stop()` 方法停止当前音频，然后按正确顺序设置新音频的属性。

### 7. 调试技巧

在控制台执行以下代码检查当前音频状态：

```javascript
const audio = uni.getBackgroundAudioManager()
console.log({
  src: audio.src,
  title: audio.title,
  singer: audio.singer,
  coverImgUrl: audio.coverImgUrl,
  paused: audio.paused,
  duration: audio.duration,
  currentTime: audio.currentTime
})
```

### 8. 最佳实践

1. **统一管理**：在播放器页面统一管理音频的设置和控制
2. **状态同步**：使用本地存储或状态管理器同步播放信息
3. **错误处理**：监听 `onError` 事件处理播放错误
4. **资源清理**：页面卸载时正确清理事件监听器

## 参考文档

- [uni-app BackgroundAudioManager 文档](https://uniapp.dcloud.net.cn/api/media/background-audio-manager.html)