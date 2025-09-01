// 定时器状态管理
const state = {
  // 定时器相关
  sleepTimer: null,           // 定时器实例
  sleepTimerEnd: 0,           // 定时器结束时间戳
  sleepTimerDuration: 0,      // 定时器总时长（分钟）
  isTimerActive: false,       // 定时器是否激活
  
  // 背景音频管理器
  backgroundAudioManager: null,
}

const getters = {
  // 获取剩余时间（秒）
  remainingSeconds: (state) => {
    if (!state.isTimerActive || !state.sleepTimerEnd) return 0
    const now = Date.now()
    const remaining = Math.max(0, Math.floor((state.sleepTimerEnd - now) / 1000))
    return remaining
  },
  
  // 获取剩余时间（格式化）
  remainingTime: (state, getters) => {
    const seconds = getters.remainingSeconds
    if (seconds <= 0) return '00:00'
    const minutes = Math.floor(seconds / 60)
    const secs = seconds % 60
    return `${String(minutes).padStart(2, '0')}:${String(secs).padStart(2, '0')}`
  }
}

const mutations = {
  // 设置背景音频管理器
  SET_AUDIO_MANAGER(state, manager) {
    state.backgroundAudioManager = manager
  },
  
  // 设置定时器
  SET_TIMER(state, { timer, endTime, duration }) {
    state.sleepTimer = timer
    state.sleepTimerEnd = endTime
    state.sleepTimerDuration = duration
    state.isTimerActive = true
  },
  
  // 清除定时器
  CLEAR_TIMER(state) {
    if (state.sleepTimer) {
      clearInterval(state.sleepTimer)
    }
    state.sleepTimer = null
    state.sleepTimerEnd = 0
    state.sleepTimerDuration = 0
    state.isTimerActive = false
  },
  
  // 更新定时器状态
  UPDATE_TIMER_STATUS(state, isActive) {
    state.isTimerActive = isActive
  }
}

const actions = {
  // 初始化背景音频管理器
  initAudioManager({ commit }) {
    // #ifdef MP-WEIXIN || MP-BAIDU || MP-QQ
    const manager = uni.getBackgroundAudioManager()
    commit('SET_AUDIO_MANAGER', manager)
    return manager
    // #endif
    
    // #ifndef MP-WEIXIN || MP-BAIDU || MP-QQ
    const manager = uni.createInnerAudioContext()
    commit('SET_AUDIO_MANAGER', manager)
    return manager
    // #endif
  },
  
  // 启动定时器
  startSleepTimer({ commit, dispatch, state }, minutes) {
    // 清除之前的定时器
    dispatch('stopSleepTimer')
    
    // 计算结束时间
    const endTime = Date.now() + minutes * 60 * 1000
    
    // 创建定时器
    const timer = setInterval(() => {
      const now = Date.now()
      
      // 检查是否到时间
      if (now >= endTime) {
        dispatch('onTimerEnd')
      }
    }, 1000) // 每秒检查一次
    
    // 保存定时器信息
    commit('SET_TIMER', {
      timer,
      endTime,
      duration: minutes
    })
    
    // 保存到本地存储，以便恢复
    uni.setStorageSync('sleepTimerEnd', endTime)
    uni.setStorageSync('sleepTimerDuration', minutes)
    
    console.log(`定时器已启动: ${minutes}分钟`)
    
    // 显示提示
    uni.showToast({
      title: `${minutes}分钟后停止播放`,
      icon: 'none',
      duration: 2000
    })
  },
  
  // 停止定时器
  stopSleepTimer({ commit }) {
    commit('CLEAR_TIMER')
    
    // 清除本地存储
    uni.removeStorageSync('sleepTimerEnd')
    uni.removeStorageSync('sleepTimerDuration')
    
    console.log('定时器已停止')
  },
  
  // 定时器结束处理
  onTimerEnd({ commit, state, dispatch }) {
    console.log('定时器时间到，停止播放')
    
    // 停止音频播放
    if (state.backgroundAudioManager) {
      try {
        state.backgroundAudioManager.stop()
      } catch (error) {
        console.error('停止音频失败:', error)
      }
    }
    
    // 清除定时器
    dispatch('stopSleepTimer')
    
    // 显示提示
    uni.showToast({
      title: '定时停止播放',
      icon: 'none',
      duration: 2000
    })
  },
  
  // 恢复定时器（从本地存储）
  restoreTimer({ commit, dispatch }) {
    const endTime = uni.getStorageSync('sleepTimerEnd')
    const duration = uni.getStorageSync('sleepTimerDuration')
    
    if (endTime && duration) {
      const now = Date.now()
      
      // 检查定时器是否还有效
      if (now < endTime) {
        // 重新创建定时器
        const timer = setInterval(() => {
          const currentTime = Date.now()
          
          if (currentTime >= endTime) {
            dispatch('onTimerEnd')
          }
        }, 1000)
        
        commit('SET_TIMER', {
          timer,
          endTime,
          duration
        })
        
        console.log('定时器已恢复')
        
        // 计算剩余分钟数
        const remainingMinutes = Math.ceil((endTime - now) / 60000)
        uni.showToast({
          title: `定时器已恢复，剩余${remainingMinutes}分钟`,
          icon: 'none',
          duration: 2000
        })
      } else {
        // 定时器已过期，清除存储
        dispatch('stopSleepTimer')
      }
    }
  },
  
  // 获取定时器状态
  getTimerStatus({ state, getters }) {
    return {
      isActive: state.isTimerActive,
      remainingSeconds: getters.remainingSeconds,
      remainingTime: getters.remainingTime,
      duration: state.sleepTimerDuration
    }
  }
}

export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions
}