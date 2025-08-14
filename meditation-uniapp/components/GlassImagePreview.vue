<template>
  <div class="glass-image-preview" v-if="visible">
    <div class="preview-bg glass" @click="close"></div>
    <div
      class="preview-img-wrapper"
      :class="{ 'broken': isBroken }"
      @touchstart="onTouchStart"
      @touchmove="onTouchMove"
      @touchend="onTouchEnd"
    >
      <img
        :src="src"
        class="preview-img"
        :style="imgStyle"
        @touchstart.stop
        @touchmove.stop
        @touchend.stop
      />
    </div>
    <div class="close-btn" @click="close">×</div>
  </div>
</template>

<script>
export default {
  name: 'GlassImagePreview',
  props: {
    src: { type: String, required: true },
    visible: { type: Boolean, default: false }
  },
  data() {
    return {
      scale: 1,
      lastDist: 0,
      isBroken: false
    }
  },
  computed: {
    imgStyle() {
      return {
        transform: `scale(${this.scale})`,
        transition: this.isBroken ? 'none' : 'transform 0.3s'
      }
    }
  },
  methods: {
    close() {
      this.$emit('close')
    },
    onTouchStart(e) {
      if (e.touches.length === 2) {
        const dist = this.getDist(e.touches)
        this.lastDist = dist
      }
    },
    onTouchMove(e) {
      if (e.touches.length === 2) {
        const dist = this.getDist(e.touches)
        const delta = dist - this.lastDist
        this.scale += delta / 200
        this.scale = Math.max(1, Math.min(this.scale, 3))
        this.lastDist = dist
        if (this.scale > 2.5) {
          this.isBroken = true
        } else {
          this.isBroken = false
        }
      }
    },
    onTouchEnd(e) {
      if (this.scale > 2.5) {
        setTimeout(() => {
          this.isBroken = false
          this.scale = 1
        }, 600)
      }
    },
    getDist(touches) {
      const [a, b] = touches
      return Math.sqrt(Math.pow(a.pageX - b.pageX, 2) + Math.pow(a.pageY - b.pageY, 2))
    }
  }
}
</script>

<style scoped lang="scss">
.glass-image-preview {
  position: fixed;
  left: 0; top: 0; right: 0; bottom: 0;
  z-index: 9999;
  display: flex;
  align-items: center;
  justify-content: center;
}
.preview-bg {
  position: absolute;
  left: 0; top: 0; right: 0; bottom: 0;
  background: rgba(0,0,0,0.25);
  z-index: 1;
}
.preview-img-wrapper {
  position: relative;
  z-index: 2;
  border-radius: 1.2em;
  overflow: hidden;
  background: rgba(255,255,255,0.18);
  box-shadow: 0 4px 32px 0 rgba(0,0,0,0.18);
  display: flex;
  align-items: center;
  justify-content: center;
  width: 80vw;
  height: 60vh;
  transition: box-shadow 0.3s;
}
.preview-img {
  max-width: 100%;
  max-height: 100%;
  border-radius: 1.2em;
  user-select: none;
  pointer-events: none;
}
.preview-img-wrapper.broken {
  animation: glass-break 0.6s linear;
  box-shadow: 0 0 32px 8px #ff5252, 0 0 64px 16px #ff5252 inset;
}
@keyframes glass-break {
  0% { filter: none; }
  20% { filter: blur(2px) brightness(1.2) contrast(1.2); }
  40% { filter: blur(4px) brightness(1.4) contrast(1.4) grayscale(0.5); }
  60% { filter: blur(8px) brightness(1.1) contrast(1.1) grayscale(1); }
  100% { filter: none; }
}
.close-btn {
  position: absolute;
  top: 32rpx;
  right: 48rpx;
  z-index: 10;
  font-size: 2.2em;
  color: #fff;
  background: rgba(0,0,0,0.18);
  border-radius: 50%;
  width: 64rpx;
  height: 64rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 2px 8px 0 rgba(0,0,0,0.12);
}
</style> 