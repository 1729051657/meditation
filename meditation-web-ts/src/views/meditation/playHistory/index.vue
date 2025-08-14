<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="用户ID" prop="userId">
              <el-input v-model="queryParams.userId" placeholder="请输入用户ID" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="单集ID" prop="trackId">
              <el-input v-model="queryParams.trackId" placeholder="请输入单集ID" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="已播放时长" prop="progressSec">
              <el-input v-model="queryParams.progressSec" placeholder="请输入已播放时长" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="最后播放时间" prop="lastPlayTime">
              <el-date-picker clearable
                v-model="queryParams.lastPlayTime"
                type="date"
                value-format="YYYY-MM-DD"
                placeholder="请选择最后播放时间"
              />
            </el-form-item>
            <el-form-item label="播放次数" prop="playCount">
              <el-input v-model="queryParams.playCount" placeholder="请输入播放次数" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="是否已听完" prop="isCompleted">
              <el-input v-model="queryParams.isCompleted" placeholder="请输入是否已听完" clearable @keyup.enter="handleQuery" />
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
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['meditation:playHistory:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['meditation:playHistory:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['meditation:playHistory:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['meditation:playHistory:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" border :data="playHistoryList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="主键" align="center" prop="id" v-if="true" />
        <el-table-column label="用户ID" align="center" prop="userId" />
        <el-table-column label="单集ID" align="center" prop="trackId" />
        <el-table-column label="已播放时长" align="center" prop="progressSec" />
        <el-table-column label="最后播放时间" align="center" prop="lastPlayTime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.lastPlayTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="播放次数" align="center" prop="playCount" />
        <el-table-column label="是否已听完" align="center" prop="isCompleted" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['meditation:playHistory:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['meditation:playHistory:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改音频播放记录对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="playHistoryFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户ID" />
        </el-form-item>
        <el-form-item label="单集ID" prop="trackId">
          <el-input v-model="form.trackId" placeholder="请输入单集ID" />
        </el-form-item>
        <el-form-item label="已播放时长" prop="progressSec">
          <el-input v-model="form.progressSec" placeholder="请输入已播放时长" />
        </el-form-item>
        <el-form-item label="最后播放时间" prop="lastPlayTime">
          <el-date-picker clearable
            v-model="form.lastPlayTime"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="请选择最后播放时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="播放次数" prop="playCount">
          <el-input v-model="form.playCount" placeholder="请输入播放次数" />
        </el-form-item>
        <el-form-item label="是否已听完" prop="isCompleted">
          <el-input v-model="form.isCompleted" placeholder="请输入是否已听完" />
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

<script setup name="PlayHistory" lang="ts">
import { listPlayHistory, getPlayHistory, delPlayHistory, addPlayHistory, updatePlayHistory } from '@/api/meditation/playHistory';
import { PlayHistoryVO, PlayHistoryQuery, PlayHistoryForm } from '@/api/meditation/playHistory/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const playHistoryList = ref<PlayHistoryVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const playHistoryFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: PlayHistoryForm = {
  id: undefined,
  userId: undefined,
  trackId: undefined,
  progressSec: undefined,
  lastPlayTime: undefined,
  playCount: undefined,
  isCompleted: undefined,
}
const data = reactive<PageData<PlayHistoryForm, PlayHistoryQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    userId: undefined,
    trackId: undefined,
    progressSec: undefined,
    lastPlayTime: undefined,
    playCount: undefined,
    isCompleted: undefined,
    params: {
    }
  },
  rules: {
    progressSec: [
      { required: true, message: "已播放时长不能为空", trigger: "blur" }
    ],
    lastPlayTime: [
      { required: true, message: "最后播放时间不能为空", trigger: "blur" }
    ],
    playCount: [
      { required: true, message: "播放次数不能为空", trigger: "blur" }
    ],
    isCompleted: [
      { required: true, message: "是否已听完不能为空", trigger: "blur" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询音频播放记录列表 */
const getList = async () => {
  loading.value = true;
  const res = await listPlayHistory(queryParams.value);
  playHistoryList.value = res.rows;
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
  playHistoryFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: PlayHistoryVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加音频播放记录";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: PlayHistoryVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getPlayHistory(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改音频播放记录";
}

/** 提交按钮 */
const submitForm = () => {
  playHistoryFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updatePlayHistory(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addPlayHistory(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: PlayHistoryVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除音频播放记录编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delPlayHistory(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('meditation/playHistory/export', {
    ...queryParams.value
  }, `playHistory_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
});
</script>
