<template>
  <!-- 简化实现：展示 uni-datetime-picker 组件 -->
  <uni-datetime-picker
    v-if="visible"
    :model-value="value"
    :type="type"
    :start="start"
    :end="end"
    @confirm="handleConfirm"
    @cancel="handleCancel"
  />
</template>

<script>
export default {
  name: 'tn-datetime-picker',
  props: {
    // 用于 v-model 双向绑定显示状态（与原库保持一致）
    modelValue: {
      type: Boolean,
      default: false
    },
    // 选择器类型：date/daterange/time 等
    type: {
      type: String,
      default: 'date'
    },
    start: {
      type: String,
      default: ''
    },
    end: {
      type: String,
      default: ''
    }
  },
  emits: ['update:modelValue', 'confirm', 'cancel'],
  data() {
    return {
      // 选中的值，由于原组件设计为打开即确认一次，我们简单保留
      value: ''
    }
  },
  computed: {
    visible() {
      return this.modelValue
    }
  },
  methods: {
    handleConfirm(e) {
      this.$emit('confirm', e)
      // 关闭弹窗
      this.$emit('update:modelValue', false)
    },
    handleCancel() {
      this.$emit('cancel')
      this.$emit('update:modelValue', false)
    }
  }
}
</script>

<style scoped>
/* 无额外样式 */
</style> 