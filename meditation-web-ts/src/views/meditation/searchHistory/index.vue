<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="用户ID" prop="userId">
              <el-input v-model="queryParams.userId" placeholder="请输入用户ID" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="搜索关键字" prop="keyword">
              <el-input v-model="queryParams.keyword" placeholder="请输入搜索关键字" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="搜索次数" prop="times">
              <el-input v-model="queryParams.times" placeholder="请输入搜索次数" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="最后搜索时间" prop="lastTime">
              <el-date-picker clearable
                v-model="queryParams.lastTime"
                type="date"
                value-format="YYYY-MM-DD"
                placeholder="请选择最后搜索时间"
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
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['meditation:searchHistory:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['meditation:searchHistory:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['meditation:searchHistory:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['meditation:searchHistory:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" border :data="searchHistoryList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="主键" align="center" prop="id" v-if="false" />
        <el-table-column label="用户ID" align="center" prop="userId" />
        <el-table-column label="搜索关键字" align="center" prop="keyword" />
        <el-table-column label="搜索次数" align="center" prop="times" />
        <el-table-column label="最后搜索时间" align="center" prop="lastTime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.lastTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['meditation:searchHistory:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['meditation:searchHistory:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改搜索历史对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="searchHistoryFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户ID" />
        </el-form-item>
        <el-form-item label="搜索关键字" prop="keyword">
          <el-input v-model="form.keyword" placeholder="请输入搜索关键字" />
        </el-form-item>
        <el-form-item label="搜索次数" prop="times">
          <el-input v-model="form.times" placeholder="请输入搜索次数" />
        </el-form-item>
        <el-form-item label="最后搜索时间" prop="lastTime">
          <el-date-picker clearable
            v-model="form.lastTime"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="请选择最后搜索时间">
          </el-date-picker>
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

<script setup name="SearchHistory" lang="ts">
import { listSearchHistory, getSearchHistory, delSearchHistory, addSearchHistory, updateSearchHistory } from '@/api/meditation/searchHistory';
import { SearchHistoryVO, SearchHistoryQuery, SearchHistoryForm } from '@/api/meditation/searchHistory/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const searchHistoryList = ref<SearchHistoryVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const searchHistoryFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: SearchHistoryForm = {
  id: undefined,
  userId: undefined,
  keyword: undefined,
  times: undefined,
  lastTime: undefined,
}
const data = reactive<PageData<SearchHistoryForm, SearchHistoryQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    userId: undefined,
    keyword: undefined,
    times: undefined,
    lastTime: undefined,
    params: {
    }
  },
  rules: {
    times: [
      { required: true, message: "搜索次数不能为空", trigger: "blur" }
    ],
    lastTime: [
      { required: true, message: "最后搜索时间不能为空", trigger: "blur" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询搜索历史列表 */
const getList = async () => {
  loading.value = true;
  const res = await listSearchHistory(queryParams.value);
  searchHistoryList.value = res.rows;
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
  searchHistoryFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: SearchHistoryVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加搜索历史";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: SearchHistoryVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getSearchHistory(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改搜索历史";
}

/** 提交按钮 */
const submitForm = () => {
  searchHistoryFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateSearchHistory(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addSearchHistory(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: SearchHistoryVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除搜索历史编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delSearchHistory(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('meditation/searchHistory/export', {
    ...queryParams.value
  }, `searchHistory_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
});
</script>
