<template>
  <div class="common-upload">
    <!-- 图片上传模式 -->
    <el-upload
      v-if="uploadType === 'image'"
      ref="uploadRef"
      multiple
      :action="uploadUrl"
      list-type="picture-card"
      :on-success="handleUploadSuccess"
      :before-upload="handleBeforeUpload"
      :limit="limit"
      :accept="acceptTypes"
      :on-error="handleUploadError"
      :on-exceed="handleExceed"
      :before-remove="handleDelete"
      :show-file-list="true"
      :headers="headers"
      :file-list="fileList"
      :on-preview="handlePictureCardPreview"
      :class="{ hide: fileList.length >= limit }"
      :disabled="disabled"
    >
      <el-icon class="avatar-uploader-icon">
        <Plus />
      </el-icon>
    </el-upload>

    <!-- 文件上传模式（音频等） -->
    <div v-else class="file-upload-wrapper">
      <el-upload
        ref="uploadRef"
        multiple
        :action="uploadUrl"
        :before-upload="handleBeforeUpload"
        :file-list="fileList"
        :limit="limit"
        :accept="acceptTypes"
        :on-error="handleUploadError"
        :on-exceed="handleExceed"
        :on-success="handleUploadSuccess"
        :show-file-list="false"
        :headers="headers"
        :disabled="disabled"
      >
        <!-- 上传按钮 -->
        <el-button v-if="!disabled" type="primary">
          <el-icon class="el-icon--left"><Upload /></el-icon>
          {{ buttonText }}
        </el-button>
      </el-upload>

      <!-- 文件列表 -->
      <transition-group 
        class="upload-file-list el-upload-list el-upload-list--text" 
        name="el-fade-in-linear" 
        tag="ul"
      >
        <li 
          v-for="(file, index) in fileList" 
          :key="file.uid || file.ossId" 
          class="el-upload-list__item ele-upload-list__item-content"
        >
          <el-link :href="file.url" :underline="false" target="_blank">
            <el-icon v-if="uploadType === 'audio'" class="el-icon--left">
              <Headset />
            </el-icon>
            <el-icon v-else class="el-icon--left">
              <Document />
            </el-icon>
            <span>{{ getFileName(file.name) }}</span>
          </el-link>
          <div class="ele-upload-list__item-content-action">
            <el-button v-if="!disabled" type="danger" link @click="handleFileDelete(index)">
              删除
            </el-button>
          </div>
        </li>
      </transition-group>
    </div>

    <!-- 上传提示 -->
    <div v-if="showTip && !disabled" class="el-upload__tip">
      <el-alert :title="tipMessage" type="info" :closable="false" />
    </div>

    <!-- 图片预览对话框 -->
    <el-dialog v-if="uploadType === 'image'" v-model="dialogVisible" title="预览" width="800px" append-to-body>
      <img :src="dialogImageUrl" style="display: block; max-width: 100%; margin: 0 auto" />
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { Plus, Upload, Headset, Document } from '@element-plus/icons-vue';
import { listByIds, delOss } from '@/api/system/oss';
import { OssVO } from '@/api/system/oss/types';
import { globalHeaders } from '@/utils/request';
import { compressAccurately } from 'image-conversion';
import type { UploadFile } from 'element-plus';

// 预定义的文件类型配置
const FILE_TYPE_PRESETS = {
  image: {
    types: ['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp'],
    maxSize: 5, // MB
    buttonText: '选择图片'
  },
  audio: {
    types: ['mp3', 'wav', 'ogg', 'aac', 'm4a', 'flac'],
    maxSize: 50, // MB
    buttonText: '选择音频'
  },
  video: {
    types: ['mp4', 'avi', 'mov', 'wmv', 'flv', 'mkv', 'webm'],
    maxSize: 100, // MB
    buttonText: '选择视频'
  },
  document: {
    types: ['doc', 'docx', 'xls', 'xlsx', 'ppt', 'pptx', 'pdf', 'txt'],
    maxSize: 10, // MB
    buttonText: '选择文件'
  }
};

interface Props {
  modelValue: string | string[] | OssVO[];
  uploadType?: 'image' | 'audio' | 'video' | 'document' | 'custom'; // 上传类型
  limit?: number; // 文件数量限制
  fileSize?: number; // 文件大小限制(MB)
  fileTypes?: string[]; // 自定义文件类型
  showTip?: boolean; // 是否显示提示
  disabled?: boolean; // 是否禁用
  compressImage?: boolean; // 是否压缩图片
  compressTargetSize?: number; // 压缩目标大小(KB)
  buttonText?: string; // 自定义按钮文字
}

const props = withDefaults(defineProps<Props>(), {
  modelValue: () => [],
  uploadType: 'image',
  limit: 5,
  fileSize: undefined,
  fileTypes: undefined,
  showTip: true,
  disabled: false,
  compressImage: false,
  compressTargetSize: 300,
  buttonText: undefined
});

const { proxy } = getCurrentInstance() as ComponentInternalInstance;
const emit = defineEmits(['update:modelValue']);

// 状态
const uploadRef = ref<any>();
const fileList = ref<any[]>([]);
const uploadList = ref<any[]>([]);
const uploadCount = ref(0);
const dialogVisible = ref(false);
const dialogImageUrl = ref('');

// 上传配置
const baseUrl = import.meta.env.VITE_APP_BASE_API;
const uploadUrl = ref(baseUrl + '/resource/oss/upload');
const headers = ref(globalHeaders());

// 获取文件类型配置
const getFileTypeConfig = computed(() => {
  if (props.uploadType === 'custom') {
    return {
      types: props.fileTypes || [],
      maxSize: props.fileSize || 10,
      buttonText: props.buttonText || '选择文件'
    };
  }
  return FILE_TYPE_PRESETS[props.uploadType] || FILE_TYPE_PRESETS.document;
});

// 允许的文件类型
const allowedFileTypes = computed(() => {
  return props.fileTypes || getFileTypeConfig.value.types;
});

// 文件大小限制
const maxFileSize = computed(() => {
  return props.fileSize || getFileTypeConfig.value.maxSize;
});

// 按钮文字
const buttonText = computed(() => {
  return props.buttonText || getFileTypeConfig.value.buttonText;
});

// 接受的文件类型
const acceptTypes = computed(() => {
  return allowedFileTypes.value.map(type => `.${type}`).join(',');
});

// 提示信息
const tipMessage = computed(() => {
  const tips = [];
  if (maxFileSize.value) {
    tips.push(`文件大小不超过 ${maxFileSize.value}MB`);
  }
  if (allowedFileTypes.value.length) {
    tips.push(`支持格式：${allowedFileTypes.value.join('、')}`);
  }
  if (props.limit > 1) {
    tips.push(`最多上传 ${props.limit} 个文件`);
  }
  return tips.join('，');
});

// 是否显示提示
const showTip = computed(() => {
  return props.showTip && (allowedFileTypes.value.length > 0 || maxFileSize.value > 0);
});

// 监听值变化
watch(
  () => props.modelValue,
  async (val) => {
    if (val) {
      let list: OssVO[] = [];
      if (Array.isArray(val)) {
        if (val.length > 0 && typeof val[0] === 'string') {
          // 字符串数组，需要查询详情
          const ossIds = (val as string[]).join(',');
          if (ossIds) {
            const res = await listByIds(ossIds);
            list = res.data;
          }
        } else {
          list = val as OssVO[];
        }
      } else if (typeof val === 'string' && val) {
        // 单个字符串
        const res = await listByIds(val);
        list = res.data;
      }

      // 转换为文件列表格式
      fileList.value = list.map((item, index) => {
        if (typeof item === 'string') {
          return {
            uid: Date.now() + index,
            name: item,
            url: item
          };
        }
        return {
          uid: item.ossId || Date.now() + index,
          name: item.originalName || item.fileName || item.ossId,
          url: item.url,
          ossId: item.ossId
        };
      });
    } else {
      fileList.value = [];
    }
  },
  { deep: true, immediate: true }
);

// 文件上传前的校验
const handleBeforeUpload = async (file: File) => {
  // 校验文件类型
  if (allowedFileTypes.value.length > 0) {
    const fileName = file.name.toLowerCase();
    const fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1);
    const isValidType = allowedFileTypes.value.some(type => {
      return fileExtension === type.toLowerCase() || 
             file.type.includes(type.toLowerCase());
    });
    
    if (!isValidType) {
      proxy?.$modal.msgError(
        `文件格式不正确，请上传 ${allowedFileTypes.value.join('、')} 格式的文件！`
      );
      return false;
    }
  }

  // 校验文件名
  if (file.name.includes(',')) {
    proxy?.$modal.msgError('文件名不能包含英文逗号！');
    return false;
  }

  // 校验文件大小
  const fileSizeMB = file.size / 1024 / 1024;
  if (fileSizeMB > maxFileSize.value) {
    proxy?.$modal.msgError(`文件大小不能超过 ${maxFileSize.value}MB！`);
    return false;
  }

  // 图片压缩处理
  if (props.uploadType === 'image' && props.compressImage && file.size / 1024 > props.compressTargetSize) {
    proxy?.$modal.loading('正在压缩图片，请稍候...');
    uploadCount.value++;
    try {
      const compressedFile = await compressAccurately(file, props.compressTargetSize);
      return compressedFile;
    } catch (error) {
      proxy?.$modal.closeLoading();
      proxy?.$modal.msgError('图片压缩失败');
      uploadCount.value--;
      return false;
    }
  }

  proxy?.$modal.loading('正在上传文件，请稍候...');
  uploadCount.value++;
  return true;
};

// 文件上传成功
const handleUploadSuccess = (res: any, file: UploadFile) => {
  if (res.code === 200) {
    uploadList.value.push({
      name: res.data.fileName || res.data.originalName,
      url: res.data.url,
      ossId: res.data.ossId
    });
    checkUploadComplete();
  } else {
    uploadCount.value--;
    proxy?.$modal.closeLoading();
    proxy?.$modal.msgError(res.msg || '上传失败');
    uploadRef.value?.handleRemove(file);
    checkUploadComplete();
  }
};

// 上传失败
const handleUploadError = (error: any) => {
  uploadCount.value--;
  proxy?.$modal.closeLoading();
  proxy?.$modal.msgError('上传失败，请重试');
  console.error('Upload error:', error);
};

// 文件数量超出限制
const handleExceed = () => {
  proxy?.$modal.msgError(`最多只能上传 ${props.limit} 个文件！`);
};

// 删除图片（picture-card模式）
const handleDelete = (file: UploadFile) => {
  const index = fileList.value.findIndex(f => f.uid === file.uid || f.name === file.name);
  if (index > -1 && uploadList.value.length === uploadCount.value) {
    const ossId = fileList.value[index].ossId;
    if (ossId) {
      delOss(ossId);
    }
    fileList.value.splice(index, 1);
    emitValue();
    return false;
  }
  return true;
};

// 删除文件（列表模式）
const handleFileDelete = (index: number) => {
  const file = fileList.value[index];
  if (file.ossId) {
    delOss(file.ossId);
  }
  fileList.value.splice(index, 1);
  emitValue();
};

// 检查上传是否完成
const checkUploadComplete = () => {
  if (uploadCount.value > 0 && uploadList.value.length === uploadCount.value) {
    fileList.value = fileList.value.filter(f => f.url !== undefined).concat(uploadList.value);
    uploadList.value = [];
    uploadCount.value = 0;
    emitValue();
    proxy?.$modal.closeLoading();
  }
};

// 图片预览
const handlePictureCardPreview = (file: any) => {
  dialogImageUrl.value = file.url;
  dialogVisible.value = true;
};

// 获取文件名
const getFileName = (name: string) => {
  if (!name) return '未知文件';
  if (name.lastIndexOf('/') > -1) {
    return name.slice(name.lastIndexOf('/') + 1);
  }
  return name;
};

// 发送更新事件
const emitValue = () => {
  const ossIds = fileList.value
    .filter(file => file.ossId)
    .map(file => file.ossId);
  
  if (props.limit === 1) {
    emit('update:modelValue', ossIds[0] || '');
  } else {
    emit('update:modelValue', ossIds.join(','));
  }
};
</script>

<style lang="scss" scoped>
.common-upload {
  width: 100%;

  // 隐藏超出限制的上传按钮
  :deep(.hide .el-upload--picture-card) {
    display: none;
  }

  // 文件上传包装器
  .file-upload-wrapper {
    width: 100%;
  }

  // 文件列表样式
  .upload-file-list {
    margin-top: 10px;

    .el-upload-list__item {
      border: 1px solid #e4e7ed;
      border-radius: 6px;
      line-height: 2;
      margin-bottom: 10px;
      padding: 10px;
      position: relative;
      transition: all 0.3s;

      &:hover {
        background-color: #f5f7fa;
      }
    }

    .ele-upload-list__item-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
      color: inherit;

      .el-link {
        display: flex;
        align-items: center;
        flex: 1;
        
        .el-icon {
          margin-right: 5px;
        }
      }
    }

    .ele-upload-list__item-content-action {
      margin-left: 20px;
    }
  }

  // 上传提示样式
  .el-upload__tip {
    margin-top: 10px;

    .el-alert {
      padding: 8px 16px;
    }
  }
}
</style>