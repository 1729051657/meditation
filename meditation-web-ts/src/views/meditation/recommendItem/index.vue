<template>
  <div class="p-2">
    <!-- 搜索区域：用于筛选推荐位内容列表 -->
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <!-- 查询表单：支持多条件筛选，内容ID需要先选择内容类型 -->
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="推荐位" prop="slotId">
              <el-select v-model="queryParams.slotId" placeholder="请选择推荐位" clearable style="width: 200px">
                <el-option 
                  v-for="slot in slotList" 
                  :key="slot.id" 
                  :label="slot.name" 
                  :value="slot.id"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="内容类型" prop="contentType">
              <!-- 内容类型选择：必须先选择类型，才能选择内容 -->
              <el-select 
                v-model="queryParams.contentType" 
                placeholder="请选择内容类型" 
                clearable 
                style="width: 150px" 
                @change="handleQueryContentTypeChange">
                <el-option label="系列" value="series" />
                <el-option label="文章" value="article" />
                <el-option label="音频" value="track" />
              </el-select>
            </el-form-item>
            <el-form-item label="内容选择" prop="contentId">
              <!-- 内容选择框：只有在选择内容类型后才能选择，避免无效查询 -->
              <el-select 
                v-model="queryParams.contentId" 
                placeholder="请选择内容" 
                clearable 
                filterable
                :disabled="!queryParams.contentType"
                style="width: 200px">
                <el-option 
                  v-for="item in queryContentOptions" 
                  :key="item.id" 
                  :label="getContentDisplayName(item)" 
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
            
            <el-form-item>
              <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
              <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </div>
    </transition>

    <el-card shadow="never">
      <template #header>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['meditation:recommendItem:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['meditation:recommendItem:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['meditation:recommendItem:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['meditation:recommendItem:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" border :data="recommendItemList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="主键" align="center" prop="id" v-if="false" />
        <el-table-column label="推荐位" align="center" prop="slotId">
          <template #default="scope">
            <span>{{ getSlotName(scope.row.slotId) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="内容类型" align="center" prop="contentType">
          <template #default="scope">
            <el-tag v-if="scope.row.contentType === 'series'" type="success">系列</el-tag>
            <el-tag v-else-if="scope.row.contentType === 'article'" type="warning">文章</el-tag>
            <el-tag v-else-if="scope.row.contentType === 'track'" type="info">音频</el-tag>
            <el-tag v-else>{{ scope.row.contentType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="内容信息" align="center" prop="contentId" width="400">
          <template #default="scope">
            <div class="content-info">
              <div class="content-title">{{ getContentTitle(scope.row) }}</div>
              <div class="content-subtitle">{{ getContentSubtitle(scope.row) }}</div>
              <div class="content-meta">
                <el-tag size="small" type="info" class="content-type-tag">
                  {{ getContentTypeLabel(scope.row.contentType) }}
                </el-tag>
                <template v-if="getContentDetail(scope.row)">
                  <span class="content-extra" v-if="getContentDetail(scope.row).episodeCount">
                    <el-icon size="12"><VideoPlay /></el-icon>
                    {{ getContentDetail(scope.row).episodeCount }} 小节
                  </span>
                  <span class="content-extra" v-if="getContentDetail(scope.row).recommendDuration">
                    <el-icon size="12"><Clock /></el-icon>
                    {{ formatDuration(getContentDetail(scope.row).recommendDuration) }}
                  </span>
                  <span class="content-extra" v-if="getContentDetail(scope.row).authorName">
                    <el-icon size="12"><User /></el-icon>
                    {{ getContentDetail(scope.row).authorName }}
                  </span>
                </template>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="显示顺序" align="center" prop="orderNum" />
        <el-table-column label="生效时间" align="center" prop="startTime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.startTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="失效时间" align="center" prop="endTime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" align="center" prop="status">
          <template #default="scope">
            <el-tag :type="scope.row.status === '0' ? 'success' : 'danger'">
              {{ scope.row.status === '0' ? '启用' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['meditation:recommendItem:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['meditation:recommendItem:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    
    <!-- 添加或修改推荐位内容对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="700px" append-to-body>
      <el-form ref="recommendItemFormRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="推荐位" prop="slotId">
              <el-select v-model="form.slotId" placeholder="请选择推荐位" style="width: 100%">
                <el-option 
                  v-for="slot in slotList" 
                  :key="slot.id" 
                  :label="slot.name" 
                  :value="slot.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="内容类型" prop="contentType">
              <el-select v-model="form.contentType" placeholder="请选择内容类型" @change="handleContentTypeChange" style="width: 100%">
                <el-option label="系列" value="series" />
                <el-option label="文章" value="article" />
                <el-option label="音频" value="track" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="内容搜索" prop="contentSearch">
              <el-input 
                v-model="contentSearch" 
                placeholder="请输入内容标题搜索" 
                clearable
                @input="handleContentSearch"
                style="width: 100%"
              >
                <template #append>
                  <el-button icon="Search" @click="searchContent" />
                </template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="内容选择" prop="contentId">
              <el-select 
                v-model="form.contentId" 
                placeholder="请选择内容" 
                filterable
                style="width: 100%"
                @change="handleContentSelect"
              >
                <el-option 
                  v-for="item in contentOptions" 
                  :key="item.id" 
                  :label="item.title || item.name" 
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <!-- 选中内容预览 -->
        <el-form-item label="内容预览" v-if="selectedContent">
          <el-card shadow="hover" class="content-preview compact" body-class="no-margin">
            <div class="preview-header">
              <el-tag :type="getContentTypeTag(form.contentType)" size="small" class="preview-type-tag">
                {{ getContentTypeLabel(form.contentType) }}
              </el-tag>
            </div>
            <div class="preview-content">
              <div class="preview-title">{{ selectedContent.title || selectedContent.name }}</div>
              <div class="preview-subtitle" v-if="selectedContent.subtitle">{{ selectedContent.subtitle }}</div>
              <div class="preview-info" v-if="selectedContent.intro">{{ selectedContent.intro }}</div>
              <div class="preview-meta" v-if="getContentMeta(selectedContent)">
                <span class="meta-item" v-if="selectedContent.episodeCount">
                  <el-icon><VideoPlay /></el-icon>
                  {{ selectedContent.episodeCount }} 小节
                </span>
                <span class="meta-item" v-if="selectedContent.recommendDuration">
                  <el-icon><Clock /></el-icon>
                  {{ formatDuration(selectedContent.recommendDuration) }}
                </span>
                <span class="meta-item" v-if="selectedContent.authorId">
                  <el-icon><User /></el-icon>
                  {{ selectedContent.authorName || '未知作者' }}
                </span>
                <span class="meta-item" v-if="selectedContent.publishTime">
                  <el-icon><Calendar /></el-icon>
                  {{ formatDate(selectedContent.publishTime) }}
                </span>
              </div>
            </div>
          </el-card>
        </el-form-item>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="显示顺序" prop="orderNum">
              <el-input-number v-model="form.orderNum" :min="1" :max="999" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio value="0">启用</el-radio>
                <el-radio value="1">停用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="生效时间" prop="startTime">
              <el-date-picker clearable
                v-model="form.startTime"
                type="datetime"
                value-format="YYYY-MM-DD HH:mm:ss"
                placeholder="请选择生效时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="失效时间" prop="endTime">
              <el-date-picker clearable
                v-model="form.endTime"
                type="datetime"
                value-format="YYYY-MM-DD HH:mm:ss"
                placeholder="请选择失效时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="RecommendItem" lang="ts">
import { listRecommendItem, getRecommendItem, delRecommendItem, addRecommendItem, updateRecommendItem } from '@/api/meditation/recommendItem';
import { listRecommendSlot } from '@/api/meditation/recommendSlot';
import { listSeries, getSeries } from '@/api/meditation/series';
import { listArticle, getArticle } from '@/api/meditation/article';
import { listTrack, getTrack } from '@/api/meditation/track';
import { RecommendItemVO, RecommendItemQuery, RecommendItemForm } from '@/api/meditation/recommendItem/types';
import { RecommendSlotVO, RecommendSlotQuery } from '@/api/meditation/recommendSlot/types';
import { VideoPlay, Clock, User, Calendar } from '@element-plus/icons-vue';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const recommendItemList = ref<RecommendItemVO[]>([]);
const slotList = ref<RecommendSlotVO[]>([]);
const contentOptions = ref<any[]>([]);
const queryContentOptions = ref<any[]>([]); // 查询用的内容选项
const selectedContent = ref<any>(null);
const contentSearch = ref('');
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const recommendItemFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: RecommendItemForm = {
  id: undefined,
  slotId: undefined,
  contentType: undefined,
  contentId: undefined,
  orderNum: 1,
  startTime: new Date().toISOString().slice(0, 19).replace('T', ' '),
  endTime: '9999-12-31 23:59:59',
  status: '0',
}
const data = reactive<PageData<RecommendItemForm, RecommendItemQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    slotId: undefined,
    contentType: undefined,
    contentId: undefined,
    orderNum: undefined,
    startTime: undefined,
    endTime: undefined,
    status: undefined,
    params: {
    }
  },
  rules: {
    slotId: [
      { required: true, message: "推荐位不能为空", trigger: "change" }
    ],
    contentType: [
      { required: true, message: "内容类型不能为空", trigger: "change" }
    ],
    contentId: [
      { required: true, message: "内容不能为空", trigger: "change" }
    ],
    orderNum: [
      { required: true, message: "显示顺序不能为空", trigger: "blur" }
    ],
    startTime: [
      { required: true, message: "生效时间不能为空", trigger: "blur" }
    ],
    endTime: [
      { required: true, message: "失效时间不能为空", trigger: "blur" }
    ],
    status: [
      { required: true, message: "状态不能为空", trigger: "change" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询推荐位内容列表 */
const getList = async () => {
  loading.value = true;
  const res = await listRecommendItem(queryParams.value);
  recommendItemList.value = res.rows;
  total.value = res.total;
  loading.value = false;
}

/** 获取推荐位列表 */
const getSlotList = async () => {
  const res = await listRecommendSlot({ 
    status: '0',
    pageNum: 1,
    pageSize: 100
  });
  slotList.value = res.rows || res.data || [];
}

/** 获取推荐位名称 */
const getSlotName = (slotId: string | number) => {
  const slot = slotList.value.find(s => s.id === slotId);
  return slot ? slot.name : slotId;
}

/** 获取内容详情 - 从后端返回的嵌套数据中获取 */
const getContentDetail = (row: RecommendItemVO) => {
  // 根据内容类型返回对应的内容对象
  switch (row.contentType) {
    case 'series':
      return row.seriesContent;
    case 'article':
      return row.articleContent;
    case 'track':
      return row.trackContent;
    default:
      return null;
  }
}

/** 获取内容标题 */
const getContentTitle = (row: RecommendItemVO) => {
  const content = getContentDetail(row);
  if (content) {
    return content.title || content.name || '未知内容';
  }
  return '未知内容';
}

/** 获取内容副标题 */
const getContentSubtitle = (row: RecommendItemVO) => {
  const content = getContentDetail(row);
  if (content) {
    // 根据内容类型返回不同的副标题信息
    if (row.contentType === 'series') {
      return content.subtitle || content.intro || '系列内容';
    } else if (row.contentType === 'article') {
      return content.subtitle || content.summary || '文章内容';
    } else if (row.contentType === 'track') {
      return content.subtitle || content.description || '音频内容';
    }
  }
  return row.contentType === 'series' ? '系列' : 
         row.contentType === 'article' ? '文章' : 
         row.contentType === 'track' ? '音频' : '';
}

/** 获取内容显示名称 - 用于查询筛选中的内容选择框 */
const getContentDisplayName = (item: any): string => {
  if (!item) return '';
  
  // 优先显示标题，如果没有则显示名称
  const title = item.title || item.name || '';
  const subtitle = item.subtitle || item.intro || '';
  
  if (subtitle) {
    return `${title} - ${subtitle}`;
  }
  return title;
}

/** 内容类型变化处理 */
const handleContentTypeChange = async () => {
  // 重置内容相关数据
  form.value.contentId = undefined;
  selectedContent.value = null;
  contentOptions.value = [];
  contentSearch.value = '';
  
  // 如果选择了内容类型，自动加载对应类型的内容选项
  // 这样用户就不需要手动搜索，可以直接从下拉框中选择内容
  if (form.value.contentType) {
    await loadContentOptions();
  }
}

/** 加载内容选项 - 自动初始化内容选择下拉框 */
const loadContentOptions = async () => {
  if (!form.value.contentType) return;
  
  try {
    let res;
    const searchParams = { 
      status: '0',        // 只加载启用状态的内容
      pageNum: 1,         // 第一页
      pageSize: 100       // 最多加载100条，足够选择使用
    };
    
    // 根据内容类型调用对应的API
    switch (form.value.contentType) {
      case 'series':
        res = await listSeries(searchParams);
        break;
      case 'article':
        res = await listArticle(searchParams);
        break;
      case 'track':
        res = await listTrack(searchParams);
        break;
      default:
        return;
    }
    
    // 更新内容选项列表，供下拉框使用
    contentOptions.value = res.rows || res.data || [];
  } catch (error) {
    console.error('加载内容选项失败:', error);
    proxy?.$modal.msgError('加载内容选项失败');
  }
}

/** 内容搜索处理 - 支持实时搜索和显示所有内容 */
const handleContentSearch = () => {
  if (contentSearch.value && form.value.contentType) {
    searchContent();
  }
}

/** 搜索内容 - 智能搜索：有关键词时搜索，无关键词时显示所有 */
const searchContent = async () => {
  if (!form.value.contentType || !contentSearch.value.trim()) {
    // 如果没有搜索关键词，显示所有内容
    // 这样用户可以先浏览所有可用内容，再进行精确搜索
    await loadContentOptions();
    return;
  }
  
  try {
    let res;
    const searchParams = { 
      title: contentSearch.value.trim(), 
      status: '0',
      pageNum: 1,
      pageSize: 20        // 搜索结果限制20条，提高性能
    };
    
    // 根据内容类型调用对应的搜索API
    switch (form.value.contentType) {
      case 'series':
        res = await listSeries(searchParams);
        break;
      case 'article':
        res = await listArticle(searchParams);
        break;
      case 'track':
        res = await listTrack(searchParams);
        break;
      default:
        return;
    }
    
    contentOptions.value = res.rows || res.data || [];
  } catch (error) {
    console.error('搜索内容失败:', error);
    proxy?.$modal.msgError('搜索内容失败');
  }
}

/** 内容选择处理 */
const handleContentSelect = () => {
  if (form.value.contentId) {
    selectedContent.value = contentOptions.value.find(item => item.id === form.value.contentId);
  } else {
    selectedContent.value = null;
  }
}

/** 获取内容类型标签样式 */
const getContentTypeTag = (type: string) => {
  switch (type) {
    case 'series': return 'success';
    case 'article': return 'warning';
    case 'track': return 'info';
    default: return 'primary';
  }
}

/** 获取内容类型标签文本 */
const getContentTypeLabel = (type: string) => {
  switch (type) {
    case 'series': return '系列';
    case 'article': return '文章';
    case 'track': return '音频';
    default: return type;
  }
}

/** 获取内容元数据 - 判断是否显示元数据区域 */
const getContentMeta = (content: any): boolean => {
  return !!(content.episodeCount || content.recommendDuration || content.authorId || content.publishTime);
}

/** 格式化时长 - 将秒数转换为可读格式 */
const formatDuration = (seconds: number): string => {
  if (!seconds) return '';
  
  const hours = Math.floor(seconds / 3600);
  const minutes = Math.floor((seconds % 3600) / 60);
  const remainingSeconds = seconds % 60;
  
  if (hours > 0) {
    return `${hours}小时${minutes}分钟`;
  } else if (minutes > 0) {
    return `${minutes}分钟${remainingSeconds}秒`;
  } else {
    return `${remainingSeconds}秒`;
  }
}

/** 格式化日期 - 将日期格式化为可读格式 */
const formatDate = (dateString: string): string => {
  if (!dateString) return '';
  
  try {
    const date = new Date(dateString);
    return date.toLocaleDateString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit'
    });
  } catch (error) {
    return dateString;
  }
}

/** 内容类型查询变化处理 - 智能查询优化 */
const handleQueryContentTypeChange = async () => {
  // 当内容类型变化时，清空内容选择，避免无效查询
  // 例如：从"系列"切换到"文章"时，之前选择的系列就无效了
  queryParams.value.contentId = undefined;
  queryContentOptions.value = [];
  
  // 如果选择了内容类型，加载对应的内容选项
  if (queryParams.value.contentType) {
    await loadQueryContentOptions();
  }
  
  // 重置到第一页，重新查询
  queryParams.value.pageNum = 1;
  getList();
}

/** 加载查询用的内容选项 */
const loadQueryContentOptions = async () => {
  if (!queryParams.value.contentType) return;
  
  try {
    let res;
    const searchParams = { 
      status: '0',        // 只加载启用状态的内容
      pageNum: 1,         // 第一页
      pageSize: 200       // 查询筛选需要更多选项
    };
    
    // 根据内容类型调用对应的API
    switch (queryParams.value.contentType) {
      case 'series':
        res = await listSeries(searchParams);
        break;
      case 'article':
        res = await listArticle(searchParams);
        break;
      case 'track':
        res = await listTrack(searchParams);
        break;
      default:
        return;
    }
    
    // 更新查询用的内容选项列表
    queryContentOptions.value = res.rows || res.data || [];
  } catch (error) {
    console.error('加载查询内容选项失败:', error);
    proxy?.$modal.msgError('加载查询内容选项失败');
  }
}

/** 取消按钮 */
const cancel = () => {
  reset();
  dialog.visible = false;
}

/** 表单重置 - 智能重置：保持内容类型，重新加载内容选项 */
const reset = () => {
  form.value = {...initFormData};
  selectedContent.value = null;
  contentOptions.value = [];
  contentSearch.value = '';
  recommendItemFormRef.value?.resetFields();
  
  // 如果当前有选择的内容类型，重新加载内容选项
  // 这样用户重置表单后，内容选择下拉框仍然可用
  if (form.value.contentType) {
    loadContentOptions();
  }
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 - 智能重置：保持禁用状态逻辑 */
const resetQuery = () => {
  queryFormRef.value?.resetFields();
  // 重置后，如果内容类型被清空，内容ID应该保持禁用状态
  // 这样用户就能清楚地知道需要先选择内容类型
  handleQuery();
}

/** 多选框选中数据 */
const handleSelectionChange = (selection: RecommendItemVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 - 智能初始化：自动选择推荐位，准备内容选择 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加推荐位内容";
  
  // 新增时默认选择第一个推荐位（如果有的话）
  // 这样用户就不需要每次都手动选择推荐位，提升用户体验
  if (slotList.value.length > 0 && !form.value.slotId) {
    form.value.slotId = slotList.value[0].id;
  }
}

/** 修改按钮操作 */
const handleUpdate = async (row?: RecommendItemVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getRecommendItem(_id);
  Object.assign(form.value, res.data);
  
  // 如果是修改，需要加载已选内容的信息
  if (form.value.contentType && form.value.contentId) {
    await searchContent();
    handleContentSelect();
  }
  
  dialog.visible = true;
  dialog.title = "修改推荐位内容";
}

/** 提交按钮 */
const submitForm = () => {
  recommendItemFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateRecommendItem(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addRecommendItem(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: RecommendItemVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除推荐位内容编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delRecommendItem(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('meditation/recommendItem/export', {
    ...queryParams.value
  }, `recommendItem_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
  getSlotList();
});
</script>

<style lang="scss" scoped>
.content-info {
  text-align: left;
  
  .content-title {
    font-weight: 600;
    margin-bottom: 6px;
    color: #303133;
    font-size: 15px;
    line-height: 1.4;
  }
  
  .content-subtitle {
    font-size: 13px;
    color: #606266;
    margin-bottom: 10px;
    line-height: 1.5;
    max-width: 350px;
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
  }
  
  .content-meta {
    display: flex;
    align-items: center;
    gap: 10px;
    flex-wrap: wrap;
    
    .content-type-tag {
      border-radius: 10px;
      font-size: 11px;
    }
    
    .content-extra {
      display: inline-flex;
      align-items: center;
      gap: 4px;
      font-size: 11px;
      color: #909399;
      padding: 2px 6px;
      background: #f4f4f5;
      border-radius: 8px;
      
      .el-icon {
        color: #909399;
      }
    }
  }
}

.content-preview {
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  overflow: hidden;
  
  &.compact {
    .preview-header {
      padding: 10px 14px;
      
      .preview-type-tag {
        font-size: 12px;
      }
      
      .preview-id {
        font-size: 12px;
      }
    }
    
    .preview-content {
      padding: 14px;
      
      .preview-title {
        font-size: 15px;
        margin-bottom: 10px;
      }
      
      .preview-subtitle {
        font-size: 13px;
        margin-bottom: 10px;
      }
      
      .preview-info {
        font-size: 12px;
        margin-bottom: 14px;
        padding: 10px;
      }
      
      .preview-meta {
        gap: 10px;
        padding-top: 14px;
        
        .meta-item {
          font-size: 11px;
          padding: 3px 8px;
          height: 22px;
          border-radius: 12px;
          
          .el-icon {
            font-size: 11px;
          }
        }
      }
    }
  }
  
  .preview-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 12px 16px;
    background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
    border-bottom: 1px solid #e4e7ed;
    
    .preview-type-tag {
      font-weight: 500;
      border-radius: 12px;
    }
  }
  
  .preview-content {
    padding: 20px;
    background: #fff;
    
    .preview-title {
      font-size: 18px;
      font-weight: 600;
      color: #303133;
      margin-bottom: 12px;
      line-height: 1.4;
      word-break: break-word;
    }
    
    .preview-subtitle {
      font-size: 14px;
      color: #606266;
      margin-bottom: 12px;
      line-height: 1.5;
      font-style: italic;
    }
    
    .preview-info {
      font-size: 13px;
      color: #909399;
      line-height: 1.6;
      margin-bottom: 16px;
      padding: 12px;
      background: #f8f9fa;
      border-radius: 6px;
      border-left: 3px solid #409eff;
    }
    
    .preview-meta {
      display: flex;
      flex-wrap: wrap;
      gap: 12px;
      padding-top: 16px;
      border-top: 1px solid #f0f0f0;
      
      .meta-item {
        display: flex;
        align-items: center;
        gap: 6px;
        font-size: 13px;
        color: #606266;
        padding: 4px 10px;
        background: #f5f7fa;
        border-radius: 14px;
        border: 1px solid #e4e7ed;
        transition: all 0.2s ease;
        line-height: 1;
        height: 24px;
        
        &:hover {
          background: #ecf5ff;
          border-color: #409eff;
          color: #409eff;
        }
        
        .el-icon {
          font-size: 13px;
        }
      }
    }
  }
  
  &:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    transform: translateY(-2px);
    transition: all 0.3s ease;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .content-preview {
    .preview-content {
      padding: 16px;
      
      .preview-title {
        font-size: 16px;
      }
      
      .preview-meta {
        gap: 8px;
        
        .meta-item {
          font-size: 11px;
          padding: 4px 8px;
        }
      }
    }
    
    &.compact {
      .preview-content {
        padding: 12px;
        
        .preview-title {
          font-size: 14px;
        }
        
        .preview-subtitle {
          font-size: 12px;
        }
        
        .preview-info {
          font-size: 11px;
          padding: 8px;
        }
        
        .preview-meta {
          gap: 8px;
          
          .meta-item {
            font-size: 10px;
            padding: 3px 7px;
            height: 20px;
            border-radius: 10px;
          }
        }
      }
    }
  }
}

// 去掉卡片默认margin
:deep(.no-margin) {
  padding: 0 !important;
  margin: 0 !important;
}
</style>
