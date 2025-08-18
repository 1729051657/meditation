<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="用户ID" prop="userId">
              <el-input v-model="queryParams.userId" placeholder="请输入用户ID" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="系列ID" prop="seriesId">
              <el-input v-model="queryParams.seriesId" placeholder="请输入系列ID" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="最近播放的单集ID" prop="lastTrackId">
              <el-input v-model="queryParams.lastTrackId" placeholder="请输入最近播放的单集ID" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="已完成小节数" prop="completedCount">
              <el-input v-model="queryParams.completedCount" placeholder="请输入已完成小节数" clearable @keyup.enter="handleQuery" />
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
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['meditation:seriesProgress:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['meditation:seriesProgress:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['meditation:seriesProgress:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['meditation:seriesProgress:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" border :data="seriesProgressList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="主键" align="center" prop="id" v-if="false" />
        <el-table-column label="用户ID" align="center" prop="userId" />
        <el-table-column label="系列ID" align="center" prop="seriesId" />
        <el-table-column label="最近播放的单集ID" align="center" prop="lastTrackId" />
        <el-table-column label="已完成小节数" align="center" prop="completedCount" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['meditation:seriesProgress:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['meditation:seriesProgress:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改系列学习进度对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="seriesProgressFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户ID" />
        </el-form-item>
        <el-form-item label="系列ID" prop="seriesId">
          <el-input v-model="form.seriesId" placeholder="请输入系列ID" />
        </el-form-item>
        <el-form-item label="最近播放的单集ID" prop="lastTrackId">
          <el-input v-model="form.lastTrackId" placeholder="请输入最近播放的单集ID" />
        </el-form-item>
        <el-form-item label="已完成小节数" prop="completedCount">
          <el-input v-model="form.completedCount" placeholder="请输入已完成小节数" />
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

<script setup name="SeriesProgress" lang="ts">
import { listSeriesProgress, getSeriesProgress, delSeriesProgress, addSeriesProgress, updateSeriesProgress } from '@/api/meditation/seriesProgress';
import { SeriesProgressVO, SeriesProgressQuery, SeriesProgressForm } from '@/api/meditation/seriesProgress/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const seriesProgressList = ref<SeriesProgressVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const seriesProgressFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: SeriesProgressForm = {
  id: undefined,
  userId: undefined,
  seriesId: undefined,
  lastTrackId: undefined,
  completedCount: undefined,
}
const data = reactive<PageData<SeriesProgressForm, SeriesProgressQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    userId: undefined,
    seriesId: undefined,
    lastTrackId: undefined,
    completedCount: undefined,
    params: {
    }
  },
  rules: {
    lastTrackId: [
      { required: true, message: "最近播放的单集ID不能为空", trigger: "blur" }
    ],
    completedCount: [
      { required: true, message: "已完成小节数不能为空", trigger: "blur" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询系列学习进度列表 */
const getList = async () => {
  loading.value = true;
  const res = await listSeriesProgress(queryParams.value);
  seriesProgressList.value = res.rows;
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
  seriesProgressFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: SeriesProgressVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加系列学习进度";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: SeriesProgressVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getSeriesProgress(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改系列学习进度";
}

/** 提交按钮 */
const submitForm = () => {
  seriesProgressFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateSeriesProgress(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addSeriesProgress(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: SeriesProgressVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除系列学习进度编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delSeriesProgress(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('meditation/seriesProgress/export', {
    ...queryParams.value
  }, `seriesProgress_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
});
</script>
