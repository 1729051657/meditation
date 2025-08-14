<template>
  <!-- 使用文本节点渲染图标。通过类名结合 iconfont.css 来显示实际图标 -->
  <text :class="iconClass" :style="iconStyle"></text>
</template>

<script>
export default {
  name: 'tn-icon',
  props: {
    /**
     * 图标名称，对应 iconfont.css 中定义的类名（不包含 "tn-icon-" 前缀）
     */
    name: {
      type: String,
      default: ''
    },
    /**
     * 图标尺寸，单位 rpx / px，支持字符串或数字
     */
    size: {
      type: [Number, String],
      default: 28
    },
    /**
     * 图标颜色
     */
    color: {
      type: String,
      default: '#333'
    }
  },
  computed: {
    iconClass() {
      // 最终的类名形如 tn-icon-home
      return `tn-icon-${this.name}`
    },
    iconStyle() {
      // 如果传入的 size 是数值则默认 rpx 单位
      const size = typeof this.size === 'number' ? `${this.size}rpx` : this.size
      return {
        fontSize: size,
        color: this.color
      }
    }
  }
}
</script>

<style scoped>
/* 这里只写一个占位，实际图标样式由全局的 iconfont.css 提供 */
</style> 