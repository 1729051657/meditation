// 全局mixin，用于在所有组件中注入公共方法和属性
export default {
  methods: {
    // 全局方法
    $tn: {
      // 显示消息提示
      showToast(title, icon = 'none', duration = 2000) {
        uni.showToast({
          title,
          icon,
          duration
        })
      },
      
      // 显示加载中
      showLoading(title = '加载中...') {
        uni.showLoading({
          title,
          mask: true
        })
      },
      
      // 隐藏加载中
      hideLoading() {
        uni.hideLoading()
      },
      
      // 显示确认对话框
      showModal(title, content = '') {
        return new Promise((resolve, reject) => {
          uni.showModal({
            title,
            content,
            success: (res) => {
              if (res.confirm) {
                resolve(res)
              } else {
                reject(res)
              }
            },
            fail: reject
          })
        })
      },
      
      // 页面跳转
      navigateTo(url) {
        uni.navigateTo({ url })
      },
      
      // 页面重定向
      redirectTo(url) {
        uni.redirectTo({ url })
      },
      
      // 返回上一页
      navigateBack(delta = 1) {
        uni.navigateBack({ delta })
      },
      
      // 切换到tabBar页面
      switchTab(url) {
        uni.switchTab({ url })
      },
      
      // 格式化金额
      formatMoney(amount) {
        if (!amount) return '0.00'
        return parseFloat(amount).toFixed(2)
      },
      
      // 格式化日期
      formatDate(date, format = 'YYYY-MM-DD') {
        if (!date) return ''
        const d = new Date(date)
        const year = d.getFullYear()
        const month = String(d.getMonth() + 1).padStart(2, '0')
        const day = String(d.getDate()).padStart(2, '0')
        const hour = String(d.getHours()).padStart(2, '0')
        const minute = String(d.getMinutes()).padStart(2, '0')
        const second = String(d.getSeconds()).padStart(2, '0')
        
        return format
          .replace('YYYY', year)
          .replace('MM', month)
          .replace('DD', day)
          .replace('HH', hour)
          .replace('mm', minute)
          .replace('ss', second)
      }
    }
  }
} 