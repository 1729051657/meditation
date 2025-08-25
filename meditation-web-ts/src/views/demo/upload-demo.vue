<template>
  <div class="app-container">
    <el-card shadow="never">
      <template #header>
        <h2>通用上传组件演示</h2>
      </template>
      
      <el-form :model="form" label-width="120px">
        <el-divider content-position="left">图片上传</el-divider>
        
        <!-- 单图上传 -->
        <el-form-item label="单图上传">
          <common-upload 
            v-model="form.singleImage" 
            upload-type="image"
            :limit="1"
            :file-size="5"
            :compress-image="true"
            :compress-target-size="300"
          />
        </el-form-item>
        
        <!-- 多图上传 -->
        <el-form-item label="多图上传">
          <common-upload 
            v-model="form.multipleImages" 
            upload-type="image"
            :limit="5"
            :file-size="10"
            :file-types="['jpg', 'jpeg', 'png', 'gif']"
          />
        </el-form-item>
        
        <el-divider content-position="left">音频上传</el-divider>
        
        <!-- 音频上传 -->
        <el-form-item label="音频文件">
          <common-upload 
            v-model="form.audio" 
            upload-type="audio"
            :limit="1"
            :file-size="50"
          />
        </el-form-item>
        
        <!-- 多音频上传 -->
        <el-form-item label="多音频文件">
          <common-upload 
            v-model="form.multipleAudios" 
            upload-type="audio"
            :limit="3"
            :file-size="30"
            :file-types="['mp3', 'wav', 'ogg']"
          />
        </el-form-item>
        
        <el-divider content-position="left">视频上传</el-divider>
        
        <!-- 视频上传 -->
        <el-form-item label="视频文件">
          <common-upload 
            v-model="form.video" 
            upload-type="video"
            :limit="1"
            :file-size="100"
          />
        </el-form-item>
        
        <el-divider content-position="left">文档上传</el-divider>
        
        <!-- 文档上传 -->
        <el-form-item label="文档文件">
          <common-upload 
            v-model="form.document" 
            upload-type="document"
            :limit="3"
            :file-size="10"
          />
        </el-form-item>
        
        <el-divider content-position="left">自定义文件上传</el-divider>
        
        <!-- 自定义文件类型 -->
        <el-form-item label="自定义文件">
          <common-upload 
            v-model="form.customFile" 
            upload-type="custom"
            :limit="2"
            :file-size="20"
            :file-types="['zip', 'rar', '7z']"
            button-text="选择压缩文件"
          />
        </el-form-item>
        
        <el-divider content-position="left">禁用状态</el-divider>
        
        <!-- 禁用状态 -->
        <el-form-item label="禁用的上传">
          <common-upload 
            v-model="form.disabledFile" 
            upload-type="image"
            :limit="1"
            :disabled="true"
          />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleSubmit">提交</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
      
      <el-divider content-position="left">表单数据</el-divider>
      <pre>{{ JSON.stringify(form, null, 2) }}</pre>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { ElMessage } from 'element-plus';

// 表单数据
const form = ref({
  singleImage: '',
  multipleImages: '',
  audio: '',
  multipleAudios: '',
  video: '',
  document: '',
  customFile: '',
  disabledFile: '1,2,3' // 模拟已有文件
});

// 提交表单
const handleSubmit = () => {
  ElMessage.success('表单数据已打印到控制台');
  console.log('Form Data:', form.value);
};

// 重置表单
const handleReset = () => {
  form.value = {
    singleImage: '',
    multipleImages: '',
    audio: '',
    multipleAudios: '',
    video: '',
    document: '',
    customFile: '',
    disabledFile: ''
  };
  ElMessage.info('表单已重置');
};
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
  
  pre {
    background-color: #f5f7fa;
    padding: 15px;
    border-radius: 4px;
    font-family: 'Courier New', monospace;
    font-size: 14px;
    line-height: 1.5;
    overflow-x: auto;
  }
}
</style>