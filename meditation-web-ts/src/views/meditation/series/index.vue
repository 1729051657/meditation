<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="分类" prop="categoryId">
              <el-input v-model="queryParams.categoryId" placeholder="请输入分类" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="标题" prop="title">
              <el-input v-model="queryParams.title" placeholder="请输入标题" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="副标题" prop="subtitle">
              <el-input v-model="queryParams.subtitle" placeholder="请输入副标题" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="封面" prop="cover">
              <el-input v-model="queryParams.cover" placeholder="请输入封面" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="横幅图" prop="banner">
              <el-input v-model="queryParams.banner" placeholder="请输入横幅图" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="简介" prop="intro">
              <el-input v-model="queryParams.intro" placeholder="请输入简介" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="小节数" prop="episodeCount">
              <el-input v-model="queryParams.episodeCount" placeholder="请输入小节数" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="建议时长" prop="recommendDuration">
              <el-input v-model="queryParams.recommendDuration" placeholder="请输入建议时长" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="是否免费" prop="isFree">
              <el-input v-model="queryParams.isFree" placeholder="请输入是否免费" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="显示顺序" prop="orderNum">
              <el-input v-model="queryParams.orderNum" placeholder="请输入显示顺序" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="发布时间" prop="publishTime">
              <el-date-picker clearable
                v-model="queryParams.publishTime"
                type="date"
                value-format="YYYY-MM-DD"
                placeholder="请选择发布时间"
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
        <el-table-column label="分类" align="center" prop="categoryId" />
        <el-table-column label="标题" align="center" prop="title" />
        <el-table-column label="副标题" align="center" prop="subtitle" />
        <el-table-column label="封面" align="center" prop="cover" />
        <el-table-column label="横幅图" align="center" prop="banner" />
        <el-table-column label="简介" align="center" prop="intro" />
        <el-table-column label="小节数" align="center" prop="episodeCount" />
        <el-table-column label="建议时长" align="center" prop="recommendDuration" />
        <el-table-column label="是否免费" align="center" prop="isFree" />
        <el-table-column label="显示顺序" align="center" prop="orderNum" />
        <el-table-column label="状态" align="center" prop="status" />
        <el-table-column label="发布时间" align="center" prop="publishTime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.publishTime, '{y}-{m}-{d}') }}</span>
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
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="seriesFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="分类" prop="categoryId">
          <el-input v-model="form.categoryId" placeholder="请输入分类" />
        </el-form-item>
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="副标题" prop="subtitle">
          <el-input v-model="form.subtitle" placeholder="请输入副标题" />
        </el-form-item>
        <el-form-item label="封面" prop="cover">
          <el-input v-model="form.cover" placeholder="请输入封面" />
        </el-form-item>
        <el-form-item label="横幅图" prop="banner">
          <el-input v-model="form.banner" placeholder="请输入横幅图" />
        </el-form-item>
        <el-form-item label="简介" prop="intro">
            <el-input v-model="form.intro" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="小节数" prop="episodeCount">
          <el-input v-model="form.episodeCount" placeholder="请输入小节数" />
        </el-form-item>
        <el-form-item label="建议时长" prop="recommendDuration">
          <el-input v-model="form.recommendDuration" placeholder="请输入建议时长" />
        </el-form-item>
        <el-form-item label="是否免费" prop="isFree">
          <el-input v-model="form.isFree" placeholder="请输入是否免费" />
        </el-form-item>
        <el-form-item label="显示顺序" prop="orderNum">
          <el-input v-model="form.orderNum" placeholder="请输入显示顺序" />
        </el-form-item>
        <el-form-item label="发布时间" prop="publishTime">
          <el-date-picker clearable
            v-model="form.publishTime"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="请选择发布时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
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
import { SeriesVO, SeriesQuery, SeriesForm } from '@/api/meditation/series/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const seriesList = ref<SeriesVO[]>([]);
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
  isFree: undefined,
  orderNum: undefined,
  status: undefined,
  publishTime: undefined,
  remark: undefined
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
    isFree: undefined,
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
    subtitle: [
      { required: true, message: "副标题不能为空", trigger: "blur" }
    ],
    cover: [
      { required: true, message: "封面不能为空", trigger: "blur" }
    ],
    banner: [
      { required: true, message: "横幅图不能为空", trigger: "blur" }
    ],
    intro: [
      { required: true, message: "简介不能为空", trigger: "blur" }
    ],
    episodeCount: [
      { required: true, message: "小节数不能为空", trigger: "blur" }
    ],
    recommendDuration: [
      { required: true, message: "建议时长不能为空", trigger: "blur" }
    ],
    isFree: [
      { required: true, message: "是否免费不能为空", trigger: "blur" }
    ],
    orderNum: [
      { required: true, message: "显示顺序不能为空", trigger: "blur" }
    ],
    status: [
      { required: true, message: "状态不能为空", trigger: "change" }
    ],
    publishTime: [
      { required: true, message: "发布时间不能为空", trigger: "blur" }
    ],
    remark: [
      { required: true, message: "备注不能为空", trigger: "blur" }
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
});
</script>
