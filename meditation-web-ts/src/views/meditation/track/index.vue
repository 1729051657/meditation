<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="所属系列id" prop="seriesId">
              <el-input v-model="queryParams.seriesId" placeholder="请输入所属系列id" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="分类id" prop="categoryId">
              <el-input v-model="queryParams.categoryId" placeholder="请输入分类id" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="标题" prop="title">
              <el-input v-model="queryParams.title" placeholder="请输入标题" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="封面文件id" prop="cover">
              <el-input v-model="queryParams.cover" placeholder="请输入封面文件id" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="音频文件id" prop="audio">
              <el-input v-model="queryParams.audio" placeholder="请输入音频文件id" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="时长" prop="durationSec">
              <el-input v-model="queryParams.durationSec" placeholder="请输入时长" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="简介" prop="intro">
              <el-input v-model="queryParams.intro" placeholder="请输入简介" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="在系列内排序" prop="orderIndex">
              <el-input v-model="queryParams.orderIndex" placeholder="请输入在系列内排序" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="是否免费" prop="isFree">
              <el-input v-model="queryParams.isFree" placeholder="请输入是否免费" clearable @keyup.enter="handleQuery" />
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
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['meditation:track:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['meditation:track:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['meditation:track:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['meditation:track:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" border :data="trackList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="主键" align="center" prop="id" v-if="true" />
        <el-table-column label="所属系列id" align="center" prop="seriesId" />
        <el-table-column label="分类id" align="center" prop="categoryId" />
        <el-table-column label="标题" align="center" prop="title" />
        <el-table-column label="封面文件id" align="center" prop="cover" />
        <el-table-column label="音频文件id" align="center" prop="audio" />
        <el-table-column label="时长" align="center" prop="durationSec" />
        <el-table-column label="简介" align="center" prop="intro" />
        <el-table-column label="在系列内排序" align="center" prop="orderIndex" />
        <el-table-column label="是否免费" align="center" prop="isFree" />
        <el-table-column label="状态" align="center" prop="status" />
        <el-table-column label="备注" align="center" prop="remark" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['meditation:track:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['meditation:track:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改冥想单集对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="trackFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="所属系列id" prop="seriesId">
          <el-input v-model="form.seriesId" placeholder="请输入所属系列id" />
        </el-form-item>
        <el-form-item label="分类id" prop="categoryId">
          <el-input v-model="form.categoryId" placeholder="请输入分类id" />
        </el-form-item>
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="封面文件id" prop="cover">
          <el-input v-model="form.cover" placeholder="请输入封面文件id" />
        </el-form-item>
        <el-form-item label="音频文件id" prop="audio">
          <el-input v-model="form.audio" placeholder="请输入音频文件id" />
        </el-form-item>
        <el-form-item label="时长" prop="durationSec">
          <el-input v-model="form.durationSec" placeholder="请输入时长" />
        </el-form-item>
        <el-form-item label="简介" prop="intro">
            <el-input v-model="form.intro" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="在系列内排序" prop="orderIndex">
          <el-input v-model="form.orderIndex" placeholder="请输入在系列内排序" />
        </el-form-item>
        <el-form-item label="是否免费" prop="isFree">
          <el-input v-model="form.isFree" placeholder="请输入是否免费" />
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

<script setup name="Track" lang="ts">
import { listTrack, getTrack, delTrack, addTrack, updateTrack } from '@/api/meditation/track';
import { TrackVO, TrackQuery, TrackForm } from '@/api/meditation/track/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const trackList = ref<TrackVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const trackFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: TrackForm = {
  id: undefined,
  seriesId: undefined,
  categoryId: undefined,
  title: undefined,
  cover: undefined,
  audio: undefined,
  durationSec: undefined,
  intro: undefined,
  orderIndex: undefined,
  isFree: undefined,
  status: undefined,
  remark: undefined
}
const data = reactive<PageData<TrackForm, TrackQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    seriesId: undefined,
    categoryId: undefined,
    title: undefined,
    cover: undefined,
    audio: undefined,
    durationSec: undefined,
    intro: undefined,
    orderIndex: undefined,
    isFree: undefined,
    status: undefined,
    params: {
    }
  },
  rules: {
    seriesId: [
      { required: true, message: "所属系列id不能为空", trigger: "blur" }
    ],
    categoryId: [
      { required: true, message: "分类id不能为空", trigger: "blur" }
    ],
    cover: [
      { required: true, message: "封面文件id不能为空", trigger: "blur" }
    ],
    durationSec: [
      { required: true, message: "时长不能为空", trigger: "blur" }
    ],
    intro: [
      { required: true, message: "简介不能为空", trigger: "blur" }
    ],
    orderIndex: [
      { required: true, message: "在系列内排序不能为空", trigger: "blur" }
    ],
    isFree: [
      { required: true, message: "是否免费不能为空", trigger: "blur" }
    ],
    status: [
      { required: true, message: "状态不能为空", trigger: "change" }
    ],
    remark: [
      { required: true, message: "备注不能为空", trigger: "blur" }
    ]
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询冥想单集列表 */
const getList = async () => {
  loading.value = true;
  const res = await listTrack(queryParams.value);
  trackList.value = res.rows;
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
  trackFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: TrackVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加冥想单集";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: TrackVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getTrack(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改冥想单集";
}

/** 提交按钮 */
const submitForm = () => {
  trackFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateTrack(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addTrack(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: TrackVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除冥想单集编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delTrack(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('meditation/track/export', {
    ...queryParams.value
  }, `track_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
});
</script>
