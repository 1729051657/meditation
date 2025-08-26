<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="分类" prop="categoryId">
              <el-select v-model="queryParams.categoryId" placeholder="请选择分类" clearable>
                <el-option
                  v-for="category in categoryList"
                  :key="category.id"
                  :label="category.name"
                  :value="category.id"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="标题" prop="title">
              <el-input v-model="queryParams.title" placeholder="请输入标题" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="副标题" prop="subtitle">
              <el-input v-model="queryParams.subtitle" placeholder="请输入副标题" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="发布时间" prop="publishTime">
              <el-date-picker clearable
                v-model="queryParams.publishTime"
                type="date"
                value-format="YYYY-MM-DD"
                placeholder="请选择发布时间"
              />
            </el-form-item>
            <el-form-item label="状态" prop="status">
              <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
                <el-option label="启用" value="0" />
                <el-option label="停用" value="1" />
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
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['meditation:series:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['meditation:series:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['meditation:series:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['meditation:series:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" border :data="seriesList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="主键" align="center" prop="id" v-if="false" />
        <el-table-column label="分类" align="center" prop="categoryId">
          <template #default="scope">
            <span>{{ categoryList.find(c => c.id == scope.row.categoryId)?.name || scope.row.categoryId }}</span>
          </template>
        </el-table-column>
        <el-table-column label="标题" align="center" prop="title" />
        <el-table-column label="副标题" align="center" prop="subtitle" />
        <el-table-column label="封面" align="center" prop="coverUrl">
          <template #default="scope">
            <image-preview :src="scope.row.coverUrl" :width="50" :height="50"/>
          </template>
        </el-table-column>
        <el-table-column label="简介" align="center" prop="intro" :show-overflow-tooltip="true" />
        <el-table-column label="小节数" align="center" prop="episodeCount" />
        <el-table-column label="建议时长" align="center" prop="recommendDuration" />
        <el-table-column label="显示顺序" align="center" prop="orderNum" />
        <el-table-column label="状态" align="center" prop="status">
          <template #default="scope">
            <el-tag :type="scope.row.status === '0' ? 'success' : 'danger'">
              {{ scope.row.status === '0' ? '启用' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="发布时间" align="center" prop="publishTime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.publishTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="标签" align="center" prop="tagIds" width="200">
          <template #default="scope">
            <div class="tag-list">
              <el-tag 
                v-for="tagId in scope.row.tagIds" 
                :key="tagId"
                size="small"
                class="mx-1"
              >
                {{ tagList.find(t => t.id === tagId)?.name || tagId }}
              </el-tag>
              <span v-if="!scope.row.tagIds || scope.row.tagIds.length === 0" class="text-gray-400">
                无标签
              </span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="备注" align="center" prop="remark" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['meditation:series:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['meditation:series:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改冥想系列对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="800px" append-to-body>
      <el-form ref="seriesFormRef" :model="form" :rules="rules" label-width="100px">
        <!-- 第一行：分类和标题 -->
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="分类" prop="categoryId">
              <el-select v-model="form.categoryId" placeholder="请选择分类" style="width: 100%">
                <el-option
                  v-for="category in categoryList"
                  :key="category.id"
                  :label="category.name"
                  :value="category.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="标题" prop="title">
              <el-input v-model="form.title" placeholder="请输入标题" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <!-- 第二行：副标题 -->
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="副标题" prop="subtitle">
              <el-input v-model="form.subtitle" placeholder="请输入副标题" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <!-- 第三行：小节数和建议时长 -->
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="小节数" prop="episodeCount">
              <el-input-number 
                v-model="form.episodeCount" 
                placeholder="请输入小节数"
                :min="1"
                :max="999"
                style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="建议时长(秒)" prop="recommendDuration">
              <el-input-number 
                v-model="form.recommendDuration" 
                placeholder="请输入建议时长"
                :min="1"
                :max="9999"
                style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <!-- 第四行：显示顺序和发布时间 -->
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="显示顺序" prop="orderNum">
              <el-input-number 
                v-model="form.orderNum" 
                placeholder="请输入显示顺序"
                :min="1"
                :max="999"
                style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发布时间" prop="publishTime">
              <el-date-picker clearable
                v-model="form.publishTime"
                type="datetime"
                value-format="YYYY-MM-DD HH:mm:ss"
                placeholder="请选择发布时间"
                style="width: 100%">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        
        <!-- 第五行：状态和备注 -->
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio value="0">启用</el-radio>
                <el-radio value="1">停用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" placeholder="请输入备注" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <!-- 第六行：封面和横幅图 -->
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="封面" prop="cover">
              <image-upload 
                v-model="form.cover" 
                :limit="1"
                :file-size="5"
                :file-type="['jpg', 'jpeg', 'png', 'gif', 'webp']"
                :compress-support="true"
                :compress-target-size="500"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="横幅图" prop="banner">
              <image-upload 
                v-model="form.banner" 
                :limit="1"
                :file-size="8"
                :file-type="['jpg', 'jpeg', 'png', 'gif', 'webp']"
                :compress-support="true"
                :compress-target-size="800"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <!-- 第七行：简介（跨列） -->
        <el-form-item label="简介" prop="intro">
          <el-input 
            v-model="form.intro" 
            type="textarea" 
            placeholder="请输入简介内容" 
            :rows="4"
            style="width: 100%" />
        </el-form-item>
        
        <!-- 第八行：标签选择 -->
        <el-form-item label="标签" prop="tagIds">
          <el-select 
            v-model="form.tagIds" 
            multiple 
            filterable 
            placeholder="请选择标签"
            style="width: 100%"
            clearable
          >
            <el-option 
              v-for="tag in tagList" 
              :key="tag.id" 
              :label="tag.name" 
              :value="tag.id"
            />
          </el-select>
        </el-form-item>
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

<script setup name="Series" lang="ts">
import { listSeries, getSeries, delSeries, addSeries, updateSeries } from '@/api/meditation/series';
import { listCategory } from '@/api/meditation/category';
import { getAllAvailableTags } from '@/api/meditation/tag';
import { CategoryVO } from '@/api/meditation/category/types';
import { TagVO } from '@/api/meditation/tag/types';
import { SeriesVO, SeriesQuery, SeriesForm } from '@/api/meditation/series/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const seriesList = ref<SeriesVO[]>([]);
const categoryList = ref<CategoryVO[]>([]);
const tagList = ref<TagVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const seriesFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: SeriesForm = {
  id: undefined,
  categoryId: undefined,
  title: undefined,
  subtitle: undefined,
  cover: undefined,
  banner: undefined,
  intro: undefined,
  episodeCount: undefined,
  recommendDuration: undefined,
  orderNum: undefined,
  status: undefined,
  publishTime: new Date().toISOString().slice(0, 19).replace('T', ' '),
  remark: undefined,
  tagIds: []
}
const data = reactive<PageData<SeriesForm, SeriesQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    categoryId: undefined,
    title: undefined,
    subtitle: undefined,
    cover: undefined,
    banner: undefined,
    intro: undefined,
    episodeCount: undefined,
    recommendDuration: undefined,
    orderNum: undefined,
    status: undefined,
    publishTime: undefined,
    params: {
    }
  },
  rules: {
    categoryId: [
      { required: true, message: "分类不能为空", trigger: "blur" }
    ],
    title: [
      { required: true, message: "标题不能为空", trigger: "blur" }
    ],
    subtitle: [
      { required: true, message: "副标题不能为空", trigger: "blur" }
    ],
    cover: [
      { required: true, message: "封面不能为空", trigger: "blur" }
    ],
    banner: [
      { required: true, message: "横幅图不能为空", trigger: "blur" }
    ],
    episodeCount: [
      { required: true, message: "小节数不能为空", trigger: "blur" }
    ],
    recommendDuration: [
      { required: true, message: "建议时长不能为空", trigger: "blur" }
    ]
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询冥想系列列表 */
const getList = async () => {
  loading.value = true;
  const res = await listSeries(queryParams.value);
  seriesList.value = res.rows;
  total.value = res.total;
  loading.value = false;
}

/** 获取分类列表 */
const getCategoryList = async () => {
  const res = await listCategory();
  categoryList.value = res.data;
}

/** 获取标签列表 */
const getTagList = async () => {
  const res = await getAllAvailableTags();
  tagList.value = res.data;
}

/** 取消按钮 */
const cancel = () => {
  reset();
  dialog.visible = false;
}

/** 表单重置 */
const reset = () => {
  form.value = {...initFormData};
  seriesFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: SeriesVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加冥想系列";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: SeriesVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getSeries(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改冥想系列";
}

/** 提交按钮 */
const submitForm = () => {
  seriesFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateSeries(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addSeries(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: SeriesVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除冥想系列编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delSeries(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('meditation/series/export', {
    ...queryParams.value
  }, `series_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
  getCategoryList();
  getTagList();
});
</script>

<style lang="scss" scoped>
.tag-list {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
  justify-content: center;
  
  .el-tag {
    margin: 2px;
  }
  
  .text-gray-400 {
    color: #9ca3af;
    font-size: 12px;
  }
}

.mx-1 {
  margin: 0 4px;
}
</style>