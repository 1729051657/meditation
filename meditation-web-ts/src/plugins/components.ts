import { App } from 'vue';

// 导入全局组件
import ImageUpload from '@/components/ImageUpload/index.vue';
import FileUpload from '@/components/FileUpload/index.vue';
import CommonUpload from '@/components/CommonUpload/index.vue';
import ImagePreview from '@/components/ImagePreview/index.vue';
import Editor from '@/components/Editor/index.vue';
import RightToolbar from '@/components/RightToolbar/index.vue';
import DictTag from '@/components/DictTag/index.vue';
import Pagination from '@/components/Pagination/index.vue';
import TreeSelect from '@/components/TreeSelect/index.vue';
import FileUploadSub from '@/components/FileUploadSub/index.vue';
import IconSelect from '@/components/IconSelect/index.vue';
import TopNav from '@/components/TopNav/index.vue';
import SvgIcon from '@/components/SvgIcon/index.vue';

// 全局组件列表
const components = {
  ImageUpload,
  FileUpload,
  CommonUpload,
  ImagePreview,
  Editor,
  RightToolbar,
  DictTag,
  Pagination,
  TreeSelect,
  FileUploadSub,
  IconSelect,
  TopNav,
  SvgIcon
};

export default {
  install: (app: App) => {
    // 注册全局组件
    Object.keys(components).forEach((key) => {
      app.component(key, components[key as keyof typeof components]);
    });
  }
};