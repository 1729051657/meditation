<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
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
              <el-select v-model="queryParams.contentType" placeholder="请选择内容类型" clearable style="width: 150px">
                <el-option label="系列" value="series" />
                <el-option label="文章" value="article" />
                <el-option label="音频" value="track" />
              </el-select>
            </el-form-item>
            <el-form-item label="内容ID" prop="contentId">
              <el-input v-model="queryParams.contentId" placeholder="请输入内容ID" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="显示顺序" prop="orderNum">
              <el-input v-model="queryParams.orderNum" placeholder="请输入显示顺序" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="生效时间" prop="startTime">
              <el-date-picker clearable
                v-model="queryParams.startTime"
                type="date"
                value-format="YYYY-MM-DD"
                placeholder="请选择生效时间"
              />
            </el-form-item>
            <el-form-item label="失效时间" prop="endTime">
              <el-date-picker clearable
                v-model="queryParams.endTime"
                type="date"
                value-format="YYYY-MM-DD"
                placeholder="请选择失效时间"
              />
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
        <el-table-column label="内容信息" align="center" prop="contentId">
          <template #default="scope">
            <div class="content-info">
              <div class="content-title">{{ getContentTitle(scope.row) }}</div>
              <div class="content-subtitle">{{ getContentSubtitle(scope.row) }}</div>
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
          <el-card shadow="hover" class="content-preview">
            <div class="preview-header">
              <el-tag :type="getContentTypeTag(form.contentType)" size="small">
                {{ getContentTypeLabel(form.contentType) }}
              </el-tag>
            </div>
            <div class="preview-content">
              <div class="preview-title">{{ selectedContent.title || selectedContent.name }}</div>
              <div class="preview-subtitle" v-if="selectedContent.subtitle">{{ selectedContent.subtitle }}</div>
              <div class="preview-info" v-if="selectedContent.intro">{{ selectedContent.intro }}</div>
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
import { listSeries } from '@/api/meditation/series';
import { listArticle } from '@/api/meditation/article';
import { listTrack } from '@/api/meditation/track';
import { RecommendItemVO, RecommendItemQuery, RecommendItemForm } from '@/api/meditation/recommendItem/types';
import { RecommendSlotVO, RecommendSlotQuery } from '@/api/meditation/recommendSlot/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const recommendItemList = ref<RecommendItemVO[]>([]);
const slotList = ref<RecommendSlotVO[]>([]);
const contentOptions = ref<any[]>([]);
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

/** 获取内容标题 */
const getContentTitle = (row: RecommendItemVO) => {
  // 这里可以根据contentType和contentId获取具体内容信息
  // 暂时返回ID，后续可以优化为显示实际标题
  return `ID: ${row.contentId}`;
}

/** 获取内容副标题 */
const getContentSubtitle = (row: RecommendItemVO) => {
  return row.contentType === 'series' ? '系列' : 
         row.contentType === 'article' ? '文章' : 
         row.contentType === 'track' ? '音频' : '';
}

/** 内容类型变化处理 */
const handleContentTypeChange = () => {
  form.value.contentId = undefined;
  selectedContent.value = null;
  contentOptions.value = [];
  contentSearch.value = '';
}

/** 内容搜索处理 */
const handleContentSearch = () => {
  if (contentSearch.value && form.value.contentType) {
    searchContent();
  }
}

/** 搜索内容 */
const searchContent = async () => {
  if (!form.value.contentType || !contentSearch.value.trim()) {
    return;
  }
  
  try {
    let res;
    const searchParams = { 
      title: contentSearch.value.trim(), 
      status: '0',
      pageNum: 1,
      pageSize: 20 
    };
    
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

/** 取消按钮 */
const cancel = () => {
  reset();
  dialog.visible = false;
}

/** 表单重置 */
const reset = () => {
  form.value = {...initFormData};
  selectedContent.value = null;
  contentOptions.value = [];
  contentSearch.value = '';
  recommendItemFormRef.value?.resetFields();
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value?.resetFields();
  handleQuery();
}

/** 多选框选中数据 */
const handleSelectionChange = (selection: RecommendItemVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加推荐位内容";
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
    font-weight: 500;
    margin-bottom: 4px;
  }
  
  .content-subtitle {
    font-size: 12px;
    color: #999;
  }
}

.content-preview {
  .preview-header {
    margin-bottom: 12px;
  }
  
  .preview-content {
    .preview-title {
      font-size: 16px;
      font-weight: 500;
      margin-bottom: 8px;
      color: #333;
    }
    
    .preview-subtitle {
      font-size: 14px;
      color: #666;
      margin-bottom: 8px;
    }
    
    .preview-info {
      font-size: 12px;
      color: #999;
      line-height: 1.5;
    }
  }
}
</style>
