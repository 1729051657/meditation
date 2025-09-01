// 播放列表状态管理
const state = {
  // 播放列表
  playlist: [],
  // 当前播放索引
  currentIndex: -1,
  // 当前播放的音频信息
  currentTrack: null,
  // 播放模式：'sequence' 顺序播放, 'loop' 单曲循环, 'random' 随机播放
  playMode: 'sequence',
  // 是否正在播放
  isPlaying: false,
  // 播放进度
  currentTime: 0,
  duration: 0,
  // 背景音频管理器
  audioManager: null
}

const getters = {
  // 获取播放列表长度
  playlistLength: (state) => state.playlist.length,
  
  // 是否有下一首
  hasNext: (state) => {
    if (state.playlist.length === 0) return false
    if (state.playMode === 'loop') return true
    return state.currentIndex < state.playlist.length - 1
  },
  
  // 是否有上一首
  hasPrevious: (state) => {
    if (state.playlist.length === 0) return false
    if (state.playMode === 'loop') return true
    return state.currentIndex > 0
  },
  
  // 获取下一首索引
  nextIndex: (state) => {
    if (state.playlist.length === 0) return -1
    
    if (state.playMode === 'random') {
      // 随机播放
      let nextIdx = Math.floor(Math.random() * state.playlist.length)
      // 避免重复播放同一首（列表长度大于1时）
      if (state.playlist.length > 1 && nextIdx === state.currentIndex) {
        nextIdx = (nextIdx + 1) % state.playlist.length
      }
      return nextIdx
    } else if (state.playMode === 'loop') {
      // 单曲循环
      return state.currentIndex
    } else {
      // 顺序播放
      return (state.currentIndex + 1) % state.playlist.length
    }
  },
  
  // 获取上一首索引
  previousIndex: (state) => {
    if (state.playlist.length === 0) return -1
    
    if (state.playMode === 'random') {
      // 随机播放
      let prevIdx = Math.floor(Math.random() * state.playlist.length)
      if (state.playlist.length > 1 && prevIdx === state.currentIndex) {
        prevIdx = (prevIdx - 1 + state.playlist.length) % state.playlist.length
      }
      return prevIdx
    } else if (state.playMode === 'loop') {
      // 单曲循环
      return state.currentIndex
    } else {
      // 顺序播放
      return (state.currentIndex - 1 + state.playlist.length) % state.playlist.length
    }
  }
}

const mutations = {
  // 设置播放列表
  SET_PLAYLIST(state, playlist) {
    state.playlist = playlist
    // 保存到本地存储
    uni.setStorageSync('playlist', playlist)
  },
  
  // 添加到播放列表
  ADD_TO_PLAYLIST(state, track) {
    const exists = state.playlist.find(item => item.id === track.id)
    if (!exists) {
      state.playlist.push(track)
      uni.setStorageSync('playlist', state.playlist)
    }
  },
  
  // 从播放列表移除
  REMOVE_FROM_PLAYLIST(state, trackId) {
    const index = state.playlist.findIndex(item => item.id === trackId)
    if (index > -1) {
      state.playlist.splice(index, 1)
      // 调整当前索引
      if (state.currentIndex >= index && state.currentIndex > 0) {
        state.currentIndex--
      }
      uni.setStorageSync('playlist', state.playlist)
    }
  },
  
  // 清空播放列表
  CLEAR_PLAYLIST(state) {
    state.playlist = []
    state.currentIndex = -1
    state.currentTrack = null
    uni.removeStorageSync('playlist')
    uni.removeStorageSync('currentIndex')
    uni.removeStorageSync('currentTrack')
  },
  
  // 设置当前播放索引
  SET_CURRENT_INDEX(state, index) {
    state.currentIndex = index
    uni.setStorageSync('currentIndex', index)
  },
  
  // 设置当前播放音频
  SET_CURRENT_TRACK(state, track) {
    state.currentTrack = track
    uni.setStorageSync('currentTrack', track)
  },
  
  // 设置播放模式
  SET_PLAY_MODE(state, mode) {
    state.playMode = mode
    uni.setStorageSync('playMode', mode)
  },
  
  // 设置播放状态
  SET_PLAYING(state, playing) {
    state.isPlaying = playing
  },
  
  // 设置播放进度
  SET_PROGRESS(state, { currentTime, duration }) {
    state.currentTime = currentTime
    state.duration = duration
  },
  
  // 设置音频管理器
  SET_AUDIO_MANAGER(state, manager) {
    state.audioManager = manager
  }
}

const actions = {
  // 初始化播放列表（从本地存储恢复）
  initPlaylist({ commit }) {
    const playlist = uni.getStorageSync('playlist') || []
    const currentIndex = uni.getStorageSync('currentIndex') || -1
    const currentTrack = uni.getStorageSync('currentTrack') || null
    const playMode = uni.getStorageSync('playMode') || 'sequence'
    
    commit('SET_PLAYLIST', playlist)
    commit('SET_CURRENT_INDEX', currentIndex)
    commit('SET_CURRENT_TRACK', currentTrack)
    commit('SET_PLAY_MODE', playMode)
    
    console.log('播放列表已恢复:', playlist.length, '首')
  },
  
  // 初始化音频管理器并设置事件监听
  initAudioManager({ commit, dispatch, state }) {
    // 统一使用全局唯一的背景音频管理器
    const manager = uni.getBackgroundAudioManager()
    
    // 播放事件
    manager.onPlay(() => {
      console.log('音频开始播放')
      commit('SET_PLAYING', true)
    })
    
    // 暂停事件
    manager.onPause(() => {
      console.log('音频暂停')
      commit('SET_PLAYING', false)
    })
    
    // 停止事件
    manager.onStop(() => {
      console.log('音频停止')
      commit('SET_PLAYING', false)
    })
    
    // 播放结束事件 - 自动播放下一首
    manager.onEnded(() => {
      console.log('音频播放结束，准备播放下一首')
      dispatch('playNext')
    })
    
    // 错误事件
    manager.onError((res) => {
      console.error('音频播放错误:', res)
      uni.showToast({
        title: '播放失败',
        icon: 'none'
      })
      // 播放失败时尝试播放下一首
      setTimeout(() => {
        dispatch('playNext')
      }, 1000)
    })
    
    // 进度更新
    manager.onTimeUpdate(() => {
      commit('SET_PROGRESS', {
        currentTime: manager.currentTime,
        duration: manager.duration
      })
    })
    
    commit('SET_AUDIO_MANAGER', manager)
    return manager
  },
  
  // 播放指定音频
  async playTrack({ commit, state, rootState }, { track, index }) {
    if (!state.audioManager) {
      console.error('音频管理器未初始化')
      return
    }
    
    // 更新当前播放信息
    commit('SET_CURRENT_TRACK', track)
    commit('SET_CURRENT_INDEX', index)
    
    // 获取音频URL
    const baseUrl = rootState.app.baseUrl || ''
    
    // 判断是否已经是完整的URL
    let audioUrl = ''
    if (track.audioUrl) {
      if (track.audioUrl.startsWith('http://') || track.audioUrl.startsWith('https://')) {
        // 已经是完整URL，直接使用
        audioUrl = track.audioUrl
      } else {
        // 需要拼接OSS下载路径
        audioUrl = `${baseUrl}/system/oss/download/${track.audioUrl}`
      }
    }
    
    let coverUrl = '/static/images/default-cover.jpg'
    if (track.coverUrl) {
      if (track.coverUrl.startsWith('http://') || track.coverUrl.startsWith('https://')) {
        // 已经是完整URL，直接使用
        coverUrl = track.coverUrl
      } else {
        // 需要拼接OSS下载路径
        coverUrl = `${baseUrl}/system/oss/download/${track.coverUrl}`
      }
    }
    
    // #ifdef MP-WEIXIN || MP-BAIDU || MP-QQ
    // 设置背景音频信息
    state.audioManager.title = track.title || '未知音频'
    state.audioManager.singer = track.artist || '未知艺术家'
    state.audioManager.coverImgUrl = coverUrl
    state.audioManager.src = audioUrl
    // #endif
    
    // #ifndef MP-WEIXIN || MP-BAIDU || MP-QQ
    state.audioManager.src = audioUrl
    state.audioManager.play()
    // #endif
    
    console.log('开始播放:', track.title)
  },
  
  // 播放下一首
  async playNext({ state, getters, dispatch }) {
    if (state.playlist.length === 0) {
      console.log('播放列表为空')
      return
    }
    
    const nextIdx = getters.nextIndex
    if (nextIdx === -1) {
      console.log('没有下一首')
      return
    }
    
    const nextTrack = state.playlist[nextIdx]
    await dispatch('playTrack', {
      track: nextTrack,
      index: nextIdx
    })
  },
  
  // 播放上一首
  async playPrevious({ state, getters, dispatch }) {
    if (state.playlist.length === 0) {
      console.log('播放列表为空')
      return
    }
    
    const prevIdx = getters.previousIndex
    if (prevIdx === -1) {
      console.log('没有上一首')
      return
    }
    
    const prevTrack = state.playlist[prevIdx]
    await dispatch('playTrack', {
      track: prevTrack,
      index: prevIdx
    })
  },
  
  // 设置播放列表并开始播放
  async setPlaylistAndPlay({ commit, dispatch }, { playlist, startIndex = 0 }) {
    if (!playlist || playlist.length === 0) {
      console.error('播放列表为空')
      return
    }
    
    // 设置播放列表
    commit('SET_PLAYLIST', playlist)
    
    // 播放指定索引的音频
    const track = playlist[startIndex]
    await dispatch('playTrack', {
      track,
      index: startIndex
    })
  },
  
  // 添加到播放列表并播放
  async addAndPlay({ commit, dispatch, state }, track) {
    // 检查是否已在播放列表中
    const existIndex = state.playlist.findIndex(item => item.id === track.id)
    
    if (existIndex > -1) {
      // 已存在，直接播放
      await dispatch('playTrack', {
        track: state.playlist[existIndex],
        index: existIndex
      })
    } else {
      // 不存在，添加到列表并播放
      commit('ADD_TO_PLAYLIST', track)
      const newIndex = state.playlist.length - 1
      await dispatch('playTrack', {
        track,
        index: newIndex
      })
    }
  },
  
  // 切换播放模式
  togglePlayMode({ commit, state }) {
    const modes = ['sequence', 'loop', 'random']
    const currentIndex = modes.indexOf(state.playMode)
    const nextIndex = (currentIndex + 1) % modes.length
    const nextMode = modes[nextIndex]
    
    commit('SET_PLAY_MODE', nextMode)
    
    const modeNames = {
      'sequence': '顺序播放',
      'loop': '单曲循环',
      'random': '随机播放'
    }
    
    uni.showToast({
      title: modeNames[nextMode],
      icon: 'none'
    })
  },
  
  // 播放/暂停
  togglePlay({ state }) {
    if (!state.audioManager) return
    
    if (state.isPlaying) {
      state.audioManager.pause()
    } else {
      state.audioManager.play()
    }
  },
  
  // 跳转到指定位置
  seek({ state }, position) {
    if (!state.audioManager) return
    state.audioManager.seek(position)
  }
}

export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions
}