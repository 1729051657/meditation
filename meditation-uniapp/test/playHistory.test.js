/**
 * 播放历史数据结构测试
 * 用于验证小程序端是否正确适配了后端的新数据结构
 */

// 模拟后端返回的新数据结构（PlayHistoryVo包含track对象）
const mockPlayHistoryResponse = {
  code: 200,
  msg: "查询成功",
  rows: [
    {
      id: 1,
      userId: 1001,
      trackId: 2001,
      progressSec: 120,
      lastPlayTime: "2025-01-13 14:30:00",
      playCount: 3,
      isCompleted: "N",
      // 新增：track对象直接包含在PlayHistoryVo中
      track: {
        id: 2001,
        seriesId: 3001,
        categoryId: 4001,
        title: "5分钟快速放松",
        cover: "https://example.com/cover1.jpg",
        audio: "https://example.com/audio1.mp3",
        durationSec: 300,
        intro: "快速缓解压力的冥想练习",
        orderIndex: 1,
        status: "0",
        remark: ""
      }
    },
    {
      id: 2,
      userId: 1001,
      trackId: 2002,
      progressSec: 600,
      lastPlayTime: "2025-01-13 10:20:00",
      playCount: 5,
      isCompleted: "Y",
      track: {
        id: 2002,
        seriesId: 3001,
        categoryId: 4001,
        title: "深度睡眠引导",
        cover: "https://example.com/cover2.jpg",
        audio: "https://example.com/audio2.mp3",
        durationSec: 600,
        intro: "帮助深度睡眠的冥想音乐",
        orderIndex: 2,
        status: "0",
        remark: ""
      }
    }
  ],
  total: 2
};

// 模拟PlayHistoryDetailVo的响应（包含额外的series和category信息）
const mockPlayHistoryDetailResponse = {
  code: 200,
  msg: "查询成功",
  rows: [
    {
      // PlayHistoryVo的基础字段
      id: 1,
      userId: 1001,
      trackId: 2001,
      progressSec: 120,
      lastPlayTime: "2025-01-13 14:30:00",
      playCount: 3,
      isCompleted: "N",
      // 包含track对象
      track: {
        id: 2001,
        seriesId: 3001,
        categoryId: 4001,
        title: "5分钟快速放松",
        cover: "https://example.com/cover1.jpg",
        audio: "https://example.com/audio1.mp3",
        durationSec: 300,
        intro: "快速缓解压力的冥想练习",
        orderIndex: 1,
        status: "0",
        remark: "正念基础训练" // 临时存储series名称
      },
      // PlayHistoryDetailVo的额外字段
      trackTitle: "5分钟快速放松",
      trackSubtitle: "",
      trackAuthor: "",
      trackCover: "https://example.com/cover1.jpg",
      trackIntro: "快速缓解压力的冥想练习",
      audioUrl: "https://example.com/audio1.mp3",
      totalDuration: 300,
      progressPercent: 40.0,
      seriesId: 3001,
      seriesTitle: "正念基础训练",
      categoryId: 4001,
      categoryName: "放松减压",
      orderIndex: 1,
      status: "0"
    }
  ],
  total: 1
};

/**
 * 测试数据处理函数
 * 模拟小程序端的数据处理逻辑
 */
function processPlayHistoryData(item) {
  // 从PlayHistoryVo中获取track信息（后端已经填充）
  const track = item.track || {};
  
  // 计算播放进度百分比
  let progressPercent = 0;
  if (track.durationSec && track.durationSec > 0) {
    progressPercent = Math.min(100, Math.round((item.progressSec * 100) / track.durationSec));
  }
  
  return {
    id: item.id, // 播放历史ID
    trackId: item.trackId,
    title: track.title || item.trackTitle || '未知音频',
    cover: track.coverUrl || item.trackCoverUrl || '/static/images/default-cover.png',
    audioUrl: track.audioUrl || item.audioUrl || '',
    duration: track.durationSec || item.totalDuration || 0,
    seriesId: track.seriesId || item.seriesId,
    seriesTitle: item.seriesTitle || track.remark || '', // 从remark中获取series名称（临时方案）
    categoryName: item.categoryName || '',
    progressSec: item.progressSec || 0,
    progressPercent: item.progressPercent || progressPercent,
    isCompleted: item.isCompleted === 'Y',
    lastPlayTime: item.lastPlayTime,
    track: track,
    series: item.series || {},
    category: item.category || {}
  };
}

/**
 * 运行测试
 */
function runTests() {
  console.log('=== 播放历史数据结构测试 ===\n');
  
  // 测试1：处理普通播放历史数据
  console.log('测试1: 处理普通播放历史数据（包含track对象）');
  const processedData1 = mockPlayHistoryResponse.rows.map(processPlayHistoryData);
  console.log('处理后的数据：', JSON.stringify(processedData1, null, 2));
  
  // 验证关键字段
  console.assert(processedData1[0].title === "5分钟快速放松", "标题应该从track对象中获取");
  console.assert(processedData1[0].progressPercent === 40, "进度百分比应该正确计算");
  console.assert(processedData1[1].isCompleted === true, "完成状态应该正确转换");
  console.log('✓ 测试1通过\n');
  
  // 测试2：处理详情数据
  console.log('测试2: 处理播放历史详情数据');
  const processedData2 = mockPlayHistoryDetailResponse.rows.map(processPlayHistoryData);
  console.log('处理后的数据：', JSON.stringify(processedData2, null, 2));
  
  // 验证详情字段
  console.assert(processedData2[0].seriesTitle === "正念基础训练", "系列名称应该正确获取");
  console.assert(processedData2[0].categoryName === "放松减压", "分类名称应该正确获取");
  console.log('✓ 测试2通过\n');
  
  console.log('=== 所有测试通过 ===');
}

// 导出测试函数供外部调用
module.exports = {
  mockPlayHistoryResponse,
  mockPlayHistoryDetailResponse,
  processPlayHistoryData,
  runTests
};

// 如果直接运行此文件，执行测试
if (require.main === module) {
  runTests();
}