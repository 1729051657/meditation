<template>
  <view class="faq-page">
    <!-- 内容区域 -->
    <view class="content-area">
      <!-- 问题列表 -->
      <view class="faq-list">
        <view 
          class="faq-item" 
          v-for="(item, index) in faqList" 
          :key="item.id || index"
          @click="toggleAnswer(index)"
        >
          <view class="question-box">
            <text class="question-text">{{ item.question }}</text>
            <view class="arrow" :class="{ 'arrow-up': item.expanded }">
              <text class="tn-icon-down"></text>
            </view>
          </view>
          
          <view class="answer-box" v-if="item.expanded">
            <text class="answer-text">{{ item.answer }}</text>
          </view>
        </view>
      </view>
      
      <!-- 底部提示 -->
      <view class="bottom-tips">
        <text class="tips-text">没有找到答案？</text>
        <view class="contact-btn" @click="contactUs">
          <text class="btn-text">联系客服</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { getFaqData } from '@/api/faq'

export default {
  data() {
    return {
      faqList: [],
      contactInfo: {
        email: '',
        phone: '',
        wechat: '',
        qq: '',
        address: ''
      },
      loading: false
    }
  },
  
  onLoad() {
    this.loadFaqData()
  },
  
  methods: {
    // 加载FAQ数据
    async loadFaqData() {
      this.loading = true
      try {
        const res = await getFaqData()
        if (res.code === 200 && res.data) {
          // 设置联系信息
          if (res.data.contactInfo) {
            this.contactInfo = {
              ...this.contactInfo,
              ...res.data.contactInfo
            }
          }
          
          // 设置问题列表
          if (res.data.questions && res.data.questions.length > 0) {
            // 按sort字段排序
            this.faqList = res.data.questions.sort((a, b) => {
              return (a.sort || 0) - (b.sort || 0)
            }).map(item => ({
              ...item,
              expanded: false
            }))
          }
        }
      } catch (error) {
        console.error('加载FAQ数据失败:', error)
      } finally {
        this.loading = false
      }
    },
    
    toggleAnswer(index) {
      this.faqList[index].expanded = !this.faqList[index].expanded
    },
    
    contactUs() {
      // 构建联系方式选项列表，只显示有值的联系方式
      const itemList = []
      
      if (this.contactInfo.email) {
        itemList.push(`邮箱：${this.contactInfo.email}`)
      }
      if (this.contactInfo.phone) {
        itemList.push(`电话：${this.contactInfo.phone}`)
      }
      if (this.contactInfo.wechat) {
        itemList.push(`微信：${this.contactInfo.wechat}`)
      }
      if (this.contactInfo.qq) {
        itemList.push(`QQ：${this.contactInfo.qq}`)
      }
      if (this.contactInfo.address) {
        itemList.push(`地址：${this.contactInfo.address}`)
      }
      
      // 如果没有任何联系方式，显示提示
      if (itemList.length === 0) {
        uni.showToast({
          title: '暂无联系方式',
          icon: 'none',
          duration: 2000
        })
        return
      }
      
      uni.showActionSheet({
        itemList: itemList,
        success: (res) => {
          const selectedItem = itemList[res.tapIndex]
          
          // 复制到剪贴板
          const value = selectedItem.split('：')[1]
          if (value) {
            uni.setClipboardData({
              data: value,
              success: () => {
                uni.showToast({
                  title: '已复制到剪贴板',
                  icon: 'success',
                  duration: 1500
                })
              }
            })
          }
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.faq-page {
  min-height: 100vh;
  background: #D8E2F0;
}

.content-area {
  padding-top: 20rpx;
  padding-bottom: 40rpx;
}

.faq-list {
  padding: 20rpx;
  
  .faq-item {
    background: #FFFFFF;
    border-radius: 20rpx;
    margin-bottom: 20rpx;
    overflow: hidden;
    box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.04);
    
    .question-box {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 30rpx;
      
      .question-text {
        flex: 1;
        font-size: 30rpx;
        color: #333333;
        font-weight: 500;
        line-height: 1.5;
      }
      
      .arrow {
        margin-left: 20rpx;
        transition: transform 0.3s ease;
        
        text {
          font-size: 24rpx;
          color: #999999;
        }
        
        &.arrow-up {
          transform: rotate(180deg);
        }
      }
    }
    
    .answer-box {
      padding: 0 30rpx 30rpx;
      border-top: 1rpx solid #F0F0F0;
      animation: slideDown 0.3s ease;
      
      .answer-text {
        display: block;
        padding-top: 20rpx;
        font-size: 28rpx;
        color: #666666;
        line-height: 1.8;
      }
    }
  }
}

.bottom-tips {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 60rpx 20rpx;
  
  .tips-text {
    font-size: 28rpx;
    color: #999999;
    margin-bottom: 30rpx;
  }
  
  .contact-btn {
    padding: 20rpx 60rpx;
    background: linear-gradient(135deg, #7C3AED, #A855F7);
    border-radius: 50rpx;
    box-shadow: 0 8rpx 20rpx rgba(124, 58, 237, 0.3);
    
    .btn-text {
      font-size: 30rpx;
      color: #FFFFFF;
      font-weight: 500;
    }
    
    &:active {
      transform: scale(0.98);
    }
  }
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-20rpx);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>