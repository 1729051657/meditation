<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="推荐位编码" prop="code">
              <el-input v-model="queryParams.code" placeholder="请输入推荐位编码" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="推荐位名称" prop="name">
              <el-input v-model="queryParams.name" placeholder="请输入推荐位名称" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="所属页面" prop="page">
              <el-input v-model="queryParams.page" placeholder="请输入所属页面" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="显示顺序" prop="orderNum">
              <el-input v-model="queryParams.orderNum" placeholder="请输入显示顺序" clearable @keyup.enter="handleQuery" />
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
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['meditation:recommendSlot:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['meditation:recommendSlot:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['meditation:recommendSlot:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['meditation:recommendSlot:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" border :data="recommendSlotList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="主键" align="center" prop="id" v-if="true" />
        <el-table-column label="推荐位编码" align="center" prop="code" />
        <el-table-column label="推荐位名称" align="center" prop="name" />
        <el-table-column label="所属页面" align="center" prop="page" />
        <el-table-column label="状态" align="center" prop="status" />
        <el-table-column label="显示顺序" align="center" prop="orderNum" />
        <el-table-column label="备注" align="center" prop="remark" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['meditation:recommendSlot:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['meditation:recommendSlot:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改推荐位对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="recommendSlotFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="推荐位编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入推荐位编码" />
        </el-form-item>
        <el-form-item label="推荐位名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入推荐位名称" />
        </el-form-item>
        <el-form-item label="所属页面" prop="page">
          <el-input v-model="form.page" placeholder="请输入所属页面" />
        </el-form-item>
        <el-form-item label="显示顺序" prop="orderNum">
          <el-input v-model="form.orderNum" placeholder="请输入显示顺序" />
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

<script setup name="RecommendSlot" lang="ts">
import { listRecommendSlot, getRecommendSlot, delRecommendSlot, addRecommendSlot, updateRecommendSlot } from '@/api/meditation/recommendSlot';
import { RecommendSlotVO, RecommendSlotQuery, RecommendSlotForm } from '@/api/meditation/recommendSlot/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const recommendSlotList = ref<RecommendSlotVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const recommendSlotFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: RecommendSlotForm = {
  id: undefined,
  code: undefined,
  name: undefined,
  page: undefined,
  status: undefined,
  orderNum: undefined,
  remark: undefined,
}
const data = reactive<PageData<RecommendSlotForm, RecommendSlotQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    code: undefined,
    name: undefined,
    page: undefined,
    status: undefined,
    orderNum: undefined,
    params: {
    }
  },
  rules: {
    page: [
      { required: true, message: "所属页面不能为空", trigger: "blur" }
    ],
    status: [
      { required: true, message: "状态不能为空", trigger: "change" }
    ],
    orderNum: [
      { required: true, message: "显示顺序不能为空", trigger: "blur" }
    ],
    remark: [
      { required: true, message: "备注不能为空", trigger: "blur" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询推荐位列表 */
const getList = async () => {
  loading.value = true;
  const res = await listRecommendSlot(queryParams.value);
  recommendSlotList.value = res.rows;
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
  recommendSlotFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: RecommendSlotVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加推荐位";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: RecommendSlotVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getRecommendSlot(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改推荐位";
}

/** 提交按钮 */
const submitForm = () => {
  recommendSlotFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateRecommendSlot(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addRecommendSlot(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: RecommendSlotVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除推荐位编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delRecommendSlot(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('meditation/recommendSlot/export', {
    ...queryParams.value
  }, `recommendSlot_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
});
</script>
