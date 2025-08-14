<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="父分类id" prop="parentId">
              <el-input v-model="queryParams.parentId" placeholder="请输入父分类id" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="分类名称" prop="name">
              <el-input v-model="queryParams.name" placeholder="请输入分类名称" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="分类编码" prop="code">
              <el-input v-model="queryParams.code" placeholder="请输入分类编码" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="图标文件id" prop="icon">
              <el-input v-model="queryParams.icon" placeholder="请输入图标文件id" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="简介" prop="description">
              <el-input v-model="queryParams.description" placeholder="请输入简介" clearable @keyup.enter="handleQuery" />
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
            <el-button type="primary" plain icon="Plus" @click="handleAdd()" v-hasPermi="['meditation:category:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="info" plain icon="Sort" @click="handleToggleExpandAll">展开/折叠</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>
      <el-table
        ref="categoryTableRef"
        v-loading="loading"
        :data="categoryList"
        row-key="id"
        border
        :default-expand-all="isExpandAll"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      >
        <el-table-column label="父分类id" align="center" prop="parentId" />
        <el-table-column label="分类名称" align="center" prop="name" />
        <el-table-column label="分类编码" align="center" prop="code" />
        <el-table-column label="图标文件id" align="center" prop="icon" />
        <el-table-column label="简介" align="center" prop="description" />
        <el-table-column label="显示顺序" align="center" prop="orderNum" />
        <el-table-column label="状态" align="center" prop="status" />
        <el-table-column label="备注" align="center" prop="remark" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['meditation:category:edit']" />
            </el-tooltip>
            <el-tooltip content="新增" placement="top">
              <el-button link type="primary" icon="Plus" @click="handleAdd(scope.row)" v-hasPermi="['meditation:category:add']" />
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['meditation:category:remove']" />
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <!-- 添加或修改冥想分类对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="categoryFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="父分类id" prop="parentId">
          <el-tree-select
            v-model="form.parentId"
            :data="categoryOptions"
            :props="{ value: 'id', label: 'name', children: 'children' }"
            value-key="id"
            placeholder="请选择父分类id"
            check-strictly
          />
        </el-form-item>
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="分类编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入分类编码" />
        </el-form-item>
        <el-form-item label="图标文件id" prop="icon">
          <el-input v-model="form.icon" placeholder="请输入图标文件id" />
        </el-form-item>
        <el-form-item label="简介" prop="description">
          <el-input v-model="form.description" placeholder="请输入简介" />
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

<script setup name="Category" lang="ts">
import { listCategory, getCategory, delCategory, addCategory, updateCategory } from "@/api/meditation/category";
import { CategoryVO, CategoryQuery, CategoryForm } from '@/api/meditation/category/types';

type CategoryOption = {
  id: number;
  name: string;
  children?: CategoryOption[];
}

const { proxy } = getCurrentInstance() as ComponentInternalInstance;;


const categoryList = ref<CategoryVO[]>([]);
const categoryOptions = ref<CategoryOption[]>([]);
const buttonLoading = ref(false);
const showSearch = ref(true);
const isExpandAll = ref(true);
const loading = ref(false);

const queryFormRef = ref<ElFormInstance>();
const categoryFormRef = ref<ElFormInstance>();
const categoryTableRef = ref<ElTableInstance>()

const dialog = reactive<DialogOption>({
    visible: false,
    title: ''
});


const initFormData: CategoryForm = {
    id: undefined,
    parentId: undefined,
    name: undefined,
    code: undefined,
    icon: undefined,
    description: undefined,
    orderNum: undefined,
    status: undefined,
    remark: undefined
}

const data = reactive<PageData<CategoryForm, CategoryQuery>>({
  form: {...initFormData},
  queryParams: {
    parentId: undefined,
    name: undefined,
    code: undefined,
    icon: undefined,
    description: undefined,
    orderNum: undefined,
    status: undefined,
    params: {
    }
  },
  rules: {
    parentId: [
      { required: true, message: "父分类id不能为空", trigger: "blur" }
    ],
    code: [
      { required: true, message: "分类编码不能为空", trigger: "blur" }
    ],
    icon: [
      { required: true, message: "图标文件id不能为空", trigger: "blur" }
    ],
    description: [
      { required: true, message: "简介不能为空", trigger: "blur" }
    ],
    orderNum: [
      { required: true, message: "显示顺序不能为空", trigger: "blur" }
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

/** 查询冥想分类列表 */
const getList = async () => {
  loading.value = true;
  const res = await listCategory(queryParams.value);
  const data = proxy?.handleTree<CategoryVO>(res.data, "id", "parentId");
  if (data) {
    categoryList.value = data;
    loading.value = false;
  }
}

/** 查询冥想分类下拉树结构 */
const getTreeselect = async () => {
  const res = await listCategory();
  categoryOptions.value = [];
  const data: CategoryOption = { id: 0, name: '顶级节点', children: [] };
  data.children = proxy?.handleTree<CategoryOption>(res.data, "id", "parentId");
  categoryOptions.value.push(data);
}

// 取消按钮
const cancel = () => {
  reset();
  dialog.visible = false;
}

// 表单重置
const reset = () => {
  form.value = {...initFormData}
  categoryFormRef.value?.resetFields();
}

/** 搜索按钮操作 */
const handleQuery = () => {
  getList();
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value?.resetFields();
  handleQuery();
}

/** 新增按钮操作 */
const handleAdd = (row?: CategoryVO) => {
  reset();
  getTreeselect();
  if (row != null && row.id) {
    form.value.parentId = row.id;
  } else {
    form.value.parentId = 0;
  }
  dialog.visible = true;
  dialog.title = "添加冥想分类";
}

/** 展开/折叠操作 */
const handleToggleExpandAll = () => {
  isExpandAll.value = !isExpandAll.value;
  toggleExpandAll(categoryList.value, isExpandAll.value)
}

/** 展开/折叠操作 */
const toggleExpandAll = (data: CategoryVO[], status: boolean) => {
  data.forEach((item) => {
    categoryTableRef.value?.toggleRowExpansion(item, status)
    if (item.children && item.children.length > 0) toggleExpandAll(item.children, status)
  })
}

/** 修改按钮操作 */
const handleUpdate = async (row: CategoryVO) => {
  reset();
  await getTreeselect();
  if (row != null) {
    form.value.parentId = row.parentId;
  }
  const res = await getCategory(row.id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改冥想分类";
}

/** 提交按钮 */
const submitForm = () => {
  categoryFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateCategory(form.value).finally(() => buttonLoading.value = false);
      } else {
        await addCategory(form.value).finally(() => buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row: CategoryVO) => {
  await proxy?.$modal.confirm('是否确认删除冥想分类编号为"' + row.id + '"的数据项？');
  loading.value = true;
  await delCategory(row.id).finally(() => loading.value = false);
  await getList();
  proxy?.$modal.msgSuccess("删除成功");
}

onMounted(() => {
  getList();
});
</script>
