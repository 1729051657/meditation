// Mock data for testing when backend is not available

export const mockCategories = [
  {
    id: 1,
    name: '情绪调节',
    description: '帮助您管理和调节情绪',
    icon: '/static/category/emotion-regulation@2x.png',
    status: 0,
    orderNum: 1
  },
  {
    id: 2,
    name: '提升专注',
    description: '提高专注力和注意力',
    icon: '/static/category/improve-focus@2x.png',
    status: 0,
    orderNum: 2
  },
  {
    id: 3,
    name: '改善睡眠',
    description: '帮助您获得更好的睡眠',
    icon: '/static/category/improve-sleep@2x.png',
    status: 0,
    orderNum: 3
  },
  {
    id: 4,
    name: '放松减压',
    description: '缓解压力，放松身心',
    icon: '/static/category/relax-stress@2x.png',
    status: 0,
    orderNum: 4
  }
]

export const mockSeries = [
  {
    id: 1,
    categoryId: 1,
    title: '情绪平衡冥想系列',
    subtitle: '7天情绪调节计划',
    coverUrl: '/static/images/meditation1.jpg',
    bannerUrl: '/static/images/banner1.jpg',
    intro: '通过7天的练习，帮助您更好地理解和管理自己的情绪',
    episodeCount: 7,
    recommendDuration: 600,
    status: '0',
    publishTime: '2024-01-01'
  },
  {
    id: 2,
    categoryId: 1,
    title: '正念情绪觉察',
    subtitle: '觉察当下的情绪状态',
    coverUrl: '/static/images/meditation2.jpg',
    bannerUrl: '/static/images/banner2.jpg',
    intro: '学习如何觉察和接纳当下的情绪，不被情绪所控制',
    episodeCount: 5,
    recommendDuration: 900,
    status: '0',
    publishTime: '2024-01-02'
  },
  {
    id: 3,
    categoryId: 2,
    title: '专注力训练',
    subtitle: '21天专注力提升计划',
    coverUrl: '/static/images/meditation3.jpg',
    bannerUrl: '/static/images/banner3.jpg',
    intro: '通过系统的训练，显著提升您的专注力',
    episodeCount: 21,
    recommendDuration: 1200,
    status: '0',
    publishTime: '2024-01-03'
  },
  {
    id: 4,
    categoryId: 3,
    title: '深度睡眠引导',
    subtitle: '每晚好眠计划',
    coverUrl: '/static/images/meditation4.jpg',
    bannerUrl: '/static/images/banner4.jpg',
    intro: '帮助您快速入睡，获得深度睡眠',
    episodeCount: 10,
    recommendDuration: 1800,
    status: '0',
    publishTime: '2024-01-04'
  },
  {
    id: 5,
    categoryId: 4,
    title: '压力释放冥想',
    subtitle: '快速减压技巧',
    coverUrl: '/static/images/meditation5.jpg',
    bannerUrl: '/static/images/banner5.jpg',
    intro: '学习有效的减压技巧，释放累积的压力',
    episodeCount: 8,
    recommendDuration: 600,
    status: '0',
    publishTime: '2024-01-05'
  }
]

export const mockTracks = [
  {
    id: 1,
    seriesId: 1,
    categoryId: 1,
    title: '认识你的情绪',
    coverUrl: '/static/images/track1.jpg',
    audioUrl: '/static/audio/track1.mp3',
    durationSec: 600,
    intro: '第一课：学习识别和命名不同的情绪',
    orderIndex: 1,
    status: 0
  },
  {
    id: 2,
    seriesId: 1,
    categoryId: 1,
    title: '情绪的身体感受',
    coverUrl: '/static/images/track2.jpg',
    audioUrl: '/static/audio/track2.mp3',
    durationSec: 720,
    intro: '第二课：觉察情绪在身体上的表现',
    orderIndex: 2,
    status: 0
  },
  {
    id: 3,
    seriesId: 2,
    categoryId: 1,
    title: '当下的觉察',
    coverUrl: '/static/images/track3.jpg',
    audioUrl: '/static/audio/track3.mp3',
    durationSec: 900,
    intro: '保持对当下情绪的觉察',
    orderIndex: 1,
    status: 0
  },
  {
    id: 4,
    seriesId: 3,
    categoryId: 2,
    title: '呼吸专注练习',
    coverUrl: '/static/images/track4.jpg',
    audioUrl: '/static/audio/track4.mp3',
    durationSec: 1200,
    intro: '通过呼吸训练提升专注力',
    orderIndex: 1,
    status: 0
  },
  {
    id: 5,
    seriesId: 3,
    categoryId: 2,
    title: '数息观练习',
    coverUrl: '/static/images/track5.jpg',
    audioUrl: '/static/audio/track5.mp3',
    durationSec: 900,
    intro: '传统的数息观专注力训练',
    orderIndex: 2,
    status: 0
  },
  {
    id: 6,
    seriesId: 4,
    categoryId: 3,
    title: '身体扫描放松',
    coverUrl: '/static/images/track6.jpg',
    audioUrl: '/static/audio/track6.mp3',
    durationSec: 1800,
    intro: '逐步放松全身，准备进入睡眠',
    orderIndex: 1,
    status: 0
  },
  {
    id: 7,
    seriesId: 4,
    categoryId: 3,
    title: '睡前冥想',
    coverUrl: '/static/images/track7.jpg',
    audioUrl: '/static/audio/track7.mp3',
    durationSec: 1500,
    intro: '专为睡前设计的放松冥想',
    orderIndex: 2,
    status: 0
  },
  {
    id: 8,
    seriesId: 5,
    categoryId: 4,
    title: '快速减压呼吸法',
    coverUrl: '/static/images/track8.jpg',
    audioUrl: '/static/audio/track8.mp3',
    durationSec: 300,
    intro: '5分钟快速减压技巧',
    orderIndex: 1,
    status: 0
  },
  {
    id: 9,
    seriesId: 5,
    categoryId: 4,
    title: '渐进式肌肉放松',
    coverUrl: '/static/images/track9.jpg',
    audioUrl: '/static/audio/track9.mp3',
    durationSec: 900,
    intro: '通过肌肉放松释放压力',
    orderIndex: 2,
    status: 0
  },
  {
    id: 10,
    seriesId: 5,
    categoryId: 4,
    title: '正念减压练习',
    coverUrl: '/static/images/track10.jpg',
    audioUrl: '/static/audio/track10.mp3',
    durationSec: 1200,
    intro: 'MBSR正念减压核心练习',
    orderIndex: 3,
    status: 0
  }
]

// Helper functions to simulate API responses
export const getMockCategories = () => {
  return {
    code: 200,
    msg: 'success',
    rows: mockCategories,
    total: mockCategories.length
  }
}

export const getMockSeries = (categoryId, pageNum = 1, pageSize = 10) => {
  const filtered = categoryId 
    ? mockSeries.filter(s => s.categoryId === categoryId)
    : mockSeries
  
  const start = (pageNum - 1) * pageSize
  const end = start + pageSize
  const paged = filtered.slice(start, end)
  
  return {
    code: 200,
    msg: 'success',
    rows: paged,
    total: filtered.length
  }
}

export const getMockTracks = (categoryId, pageNum = 1, pageSize = 10) => {
  const filtered = categoryId
    ? mockTracks.filter(t => t.categoryId === categoryId)
    : mockTracks
  
  const start = (pageNum - 1) * pageSize
  const end = start + pageSize
  const paged = filtered.slice(start, end)
  
  return {
    code: 200,
    msg: 'success',
    rows: paged,
    total: filtered.length
  }
}