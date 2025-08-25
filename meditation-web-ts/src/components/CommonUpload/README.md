# CommonUpload 通用上传组件

## 组件介绍

`CommonUpload` 是一个功能强大的通用文件上传组件，支持图片、音频、视频、文档等多种文件类型的上传，并提供了文件类型限制、大小限制、图片压缩等功能。

## 功能特性

- ✅ 支持多种文件类型（图片、音频、视频、文档）
- ✅ 文件类型自动识别和限制
- ✅ 文件大小限制
- ✅ 图片自动压缩
- ✅ 单文件/多文件上传
- ✅ 文件预览功能
- ✅ 禁用状态支持
- ✅ 自定义文件类型和按钮文字
- ✅ 友好的错误提示

## 使用方法

### 基础用法

```vue
<template>
  <!-- 图片上传 -->
  <common-upload 
    v-model="imageUrl" 
    upload-type="image"
    :limit="1"
  />
  
  <!-- 音频上传 -->
  <common-upload 
    v-model="audioUrl" 
    upload-type="audio"
    :limit="1"
  />
</template>

<script setup>
import { ref } from 'vue';

const imageUrl = ref('');
const audioUrl = ref('');
</script>
```

### 高级用法

```vue
<template>
  <!-- 带压缩的图片上传 -->
  <common-upload 
    v-model="compressedImage" 
    upload-type="image"
    :limit="1"
    :file-size="5"
    :compress-image="true"
    :compress-target-size="300"
  />
  
  <!-- 自定义文件类型 -->
  <common-upload 
    v-model="customFile" 
    upload-type="custom"
    :limit="3"
    :file-size="20"
    :file-types="['zip', 'rar', '7z']"
    button-text="选择压缩文件"
  />
</template>
```

## API 属性

| 属性 | 说明 | 类型 | 默认值 |
|------|------|------|--------|
| v-model | 绑定值，文件ID或ID字符串 | `string \| string[] \| OssVO[]` | `[]` |
| upload-type | 上传类型 | `'image' \| 'audio' \| 'video' \| 'document' \| 'custom'` | `'image'` |
| limit | 文件数量限制 | `number` | `5` |
| file-size | 文件大小限制（MB） | `number` | 根据类型自动设置 |
| file-types | 允许的文件类型 | `string[]` | 根据类型自动设置 |
| show-tip | 是否显示提示信息 | `boolean` | `true` |
| disabled | 是否禁用 | `boolean` | `false` |
| compress-image | 是否压缩图片（仅图片类型） | `boolean` | `false` |
| compress-target-size | 压缩目标大小（KB） | `number` | `300` |
| button-text | 自定义按钮文字 | `string` | 根据类型自动设置 |

## 预设文件类型

### 图片 (image)
- 支持格式：jpg, jpeg, png, gif, bmp, webp
- 默认大小限制：5MB

### 音频 (audio)
- 支持格式：mp3, wav, ogg, aac, m4a, flac
- 默认大小限制：50MB

### 视频 (video)
- 支持格式：mp4, avi, mov, wmv, flv, mkv, webm
- 默认大小限制：100MB

### 文档 (document)
- 支持格式：doc, docx, xls, xlsx, ppt, pptx, pdf, txt
- 默认大小限制：10MB

## 使用示例

### 1. 单图片上传（带压缩）

```vue
<common-upload 
  v-model="form.avatar" 
  upload-type="image"
  :limit="1"
  :file-size="2"
  :compress-image="true"
  :compress-target-size="200"
/>
```

### 2. 多音频文件上传

```vue
<common-upload 
  v-model="form.audioList" 
  upload-type="audio"
  :limit="5"
  :file-size="30"
  :file-types="['mp3', 'wav']"
/>
```

### 3. 自定义文件类型上传

```vue
<common-upload 
  v-model="form.archive" 
  upload-type="custom"
  :limit="1"
  :file-size="50"
  :file-types="['zip', 'rar', '7z', 'tar', 'gz']"
  button-text="上传压缩包"
/>
```

### 4. 禁用状态（仅查看）

```vue
<common-upload 
  v-model="form.files" 
  upload-type="document"
  :disabled="true"
/>
```

## 注意事项

1. **文件大小限制**：请根据实际需求设置合理的文件大小限制，避免上传过大文件影响服务器性能。

2. **图片压缩**：启用图片压缩功能可以减少服务器存储压力，但会略微影响上传速度。

3. **文件类型限制**：建议明确指定允许的文件类型，避免上传不安全的文件。

4. **并发上传**：多文件上传时会并发进行，请确保服务器能够处理并发请求。

5. **错误处理**：组件内置了完善的错误处理机制，会自动提示用户错误信息。

## 服务端要求

组件默认使用 `/resource/oss/upload` 接口进行文件上传，服务端需要：

1. 支持 multipart/form-data 格式
2. 返回格式：
```json
{
  "code": 200,
  "data": {
    "ossId": "文件ID",
    "fileName": "文件名",
    "originalName": "原始文件名",
    "url": "文件访问URL"
  }
}
```

## 更新日志

### v1.0.0
- 初始版本发布
- 支持图片、音频、视频、文档上传
- 支持文件类型和大小限制
- 支持图片压缩功能
- 支持单文件和多文件上传
- 支持禁用状态