// Mock data configuration
// Set USE_MOCK_DATA to true to use mock data instead of real API

export const USE_MOCK_DATA = false // 设置为 true 使用模拟数据，false 使用真实API

// 如果后端服务未启动，可以临时启用 mock 数据进行开发测试
// 在开发环境中，您可以根据需要切换这个配置

export const mockConfig = {
  // 是否在控制台显示 mock 数据日志
  showLogs: true,
  
  // 模拟网络延迟（毫秒）
  delay: 300,
  
  // 是否模拟随机错误
  simulateErrors: false,
  
  // 错误概率 (0-1)
  errorRate: 0.1
}