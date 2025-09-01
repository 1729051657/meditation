<template>
  <view class="faq-page">
    <!-- 自定义导航栏 -->
    <tn-nav-bar
      :isBack="true"
      :bottomShadow="false"
      backgroundColor="#D8E2F0"
      :fixed="true"
    >
      <view class="nav-title">
        <text class="nav-title-text">常见问题</text>
      </view>
    </tn-nav-bar>
    
    <!-- 内容区域 -->
    <view class="content-area">
      <!-- 问题列表 -->
      <view class="faq-list">
        <view 
          class="faq-item" 
          v-for="(item, index) in faqList" 
          :key="index"
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
export default {
  data() {
    return {
      faqList: [
        {
          question: '如何开始冥想？',
          answer: '选择一个安静的环境，采用舒适的坐姿，闭上眼睛，专注于呼吸。初学者可以从5-10分钟开始，逐渐增加时长。',
          expanded: false
        },
        {
          question: '冥想的最佳时间是什么时候？',
          answer: '早晨起床后和晚上睡前是最佳的冥想时间。早晨冥想可以帮助您开启美好的一天，晚上冥想有助于放松身心，改善睡眠质量。',
          expanded: false
        },
        {
          question: '冥想时总是走神怎么办？',
          answer: '走神是正常现象，不必自责。当您意识到走神时，温柔地将注意力带回到呼吸上。随着练习的增加，专注力会逐渐提升。',
          expanded: false
        },
        {
          question: '需要特殊的装备吗？',
          answer: '冥想不需要特殊装备。您只需要一个安静的空间和舒适的坐垫或椅子即可。如果愿意，可以使用冥想垫或瑜伽垫增加舒适度。',
          expanded: false
        },
        {
          question: '冥想有什么好处？',
          answer: '冥想可以减轻压力和焦虑，提高专注力，改善睡眠质量，增强自我意识，促进情绪稳定，提升整体幸福感。',
          expanded: false
        },
        {
          question: '每天需要冥想多长时间？',
          answer: '初学者可以从每天5-10分钟开始。随着练习的深入，可以逐渐增加到20-30分钟。重要的是保持规律性，而不是时长。',
          expanded: false
        },
        {
          question: '如何知道自己冥想是否正确？',
          answer: '没有"正确"或"错误"的冥想方式。如果您感到更加平静、专注和放松，那就是有效的冥想。每个人的体验都是独特的。',
          expanded: false
        },
        {
          question: '可以躺着冥想吗？',
          answer: '可以，但坐姿通常更推荐，因为躺着容易睡着。如果选择躺姿，建议保持清醒的意识，避免进入睡眠状态。',
          expanded: false
        }
      ]
    }
  },
  
  methods: {
    toggleAnswer(index) {
      this.faqList[index].expanded = !this.faqList[index].expanded
    },
    
    contactUs() {
      uni.showModal({
        title: '联系客服',
        content: '客服邮箱：support@meditation.com\n客服电话：400-123-4567',
        showCancel: false,
        confirmText: '我知道了'
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

.nav-title {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  
  .nav-title-text {
    font-size: 32rpx;
    color: #fff;
    font-weight: 500;
  }
}

.content-area {
  padding-top: 120rpx;
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