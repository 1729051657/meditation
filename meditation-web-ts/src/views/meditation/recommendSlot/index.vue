<template>
  <div class="p-2">
    <!-- 搜索区域：用于筛选推荐位列表 -->
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <!-- 推荐位编码搜索：用于快速定位特定推荐位 -->
            <el-form-item label="编码" prop="code">
              <el-input v-model="queryParams.code" placeholder="请输入推荐位编码" clearable @keyup.enter="handleQuery" style="width: 180px" />
            </el-form-item>
            <!-- 推荐位名称搜索：支持模糊搜索推荐位名称 -->
            <el-form-item label="名称" prop="name">
              <el-input v-model="queryParams.name" placeholder="请输入推荐位名称" clearable @keyup.enter="handleQuery" style="width: 180px" />
            </el-form-item>
            <!-- 所属页面筛选：按页面类型筛选推荐位 -->
            <el-form-item label="所属页面" prop="page">
              <el-select v-model="queryParams.page" placeholder="请选择页面" clearable style="width: 150px">
                <el-option label="首页" value="home" />
                <el-option label="发现页" value="discover" />
                <el-option label="分类页" value="category" />
                <el-option label="我的页" value="mine" />
                <el-option label="播放页" value="player" />
                <el-option label="搜索结果页" value="search" />
              </el-select>
            </el-form-item>
            <!-- 状态筛选：按启用/停用状态筛选 -->
            <el-form-item label="状态" prop="status">
              <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 120px">
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

    <!-- 推荐位列表展示区域 -->
    <el-card shadow="never">
      <template #header>
        <el-row :gutter="10" class="mb8">
          <!-- 新增推荐位：创建新的推荐位定义 -->
          <el-col :span="1.5">
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['meditation:recommendSlot:add']">新增推荐位</el-button>
          </el-col>
          <!-- 修改推荐位：编辑现有推荐位的配置 -->
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['meditation:recommendSlot:edit']">修改</el-button>
          </el-col>
          <!-- 删除推荐位：删除不需要的推荐位 -->
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['meditation:recommendSlot:remove']">删除</el-button>
          </el-col>
          <!-- 导出推荐位：导出推荐位配置数据 -->
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['meditation:recommendSlot:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <!-- 推荐位数据表格 -->
      <el-table v-loading="loading" border :data="recommendSlotList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="主键" align="center" prop="id" v-if="false" />
        <!-- 推荐位编码：前端代码中使用的唯一标识符 -->
        <el-table-column label="推荐位编码" align="center" prop="code" width="150">
          <template #default="scope">
            <el-tag type="info" size="small">{{ scope.row.code }}</el-tag>
          </template>
        </el-table-column>
        <!-- 推荐位名称：用户友好的显示名称 -->
        <el-table-column label="推荐位名称" align="center" prop="name" width="180" />
        <!-- 所属页面：推荐位在哪个页面显示 -->
        <el-table-column label="所属页面" align="center" prop="page" width="120">
          <template #default="scope">
            <el-tag v-if="scope.row.page === 'home'" type="primary">首页</el-tag>
            <el-tag v-else-if="scope.row.page === 'discover'" type="success">发现页</el-tag>
            <el-tag v-else-if="scope.row.page === 'category'" type="warning">分类页</el-tag>
            <el-tag v-else-if="scope.row.page === 'mine'" type="info">我的页</el-tag>
            <el-tag v-else-if="scope.row.page === 'player'" type="danger">播放页</el-tag>
            <el-tag v-else-if="scope.row.page === 'search'" type="warning">搜索结果页</el-tag>
            <el-tag v-else>{{ scope.row.page }}</el-tag>
          </template>
        </el-table-column>
        <!-- 推荐位类型：决定内容的展示方式（横幅、列表、网格、轮播等） -->
        <el-table-column label="推荐位类型" align="center" prop="type" width="120">
          <template #default="scope">
            <el-tag v-if="scope.row.type === 'banner'" type="success">横幅</el-tag>
            <el-tag v-else-if="scope.row.type === 'list'" type="warning">列表</el-tag>
            <el-tag v-else-if="scope.row.type === 'grid'" type="info">网格</el-tag>
            <el-tag v-else-if="scope.row.type === 'carousel'" type="danger">轮播</el-tag>
            <el-tag v-else type="info">{{ scope.row.type || '未设置' }}</el-tag>
          </template>
        </el-table-column>
        <!-- 状态：启用或停用该推荐位 -->
        <el-table-column label="状态" align="center" prop="status" width="80">
          <template #default="scope">
            <el-tag :type="scope.row.status === '0' ? 'success' : 'danger'">
              {{ scope.row.status === '0' ? '启用' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <!-- 显示顺序：同一页面中多个推荐位的排序 -->
        <el-table-column label="显示顺序" align="center" prop="orderNum" width="100" />
        <!-- 创建时间：推荐位的创建时间 -->
        <el-table-column label="创建时间" align="center" prop="createTime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}') }}</span>
          </template>
        </el-table-column>
        <!-- 备注：推荐位的详细说明 -->
        <el-table-column label="备注" align="center" prop="remark" show-overflow-tooltip />
        <!-- 操作列：修改、删除等操作 -->
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150">
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
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="700px" append-to-body>
      <el-form ref="recommendSlotFormRef" :model="form" :rules="rules" label-width="120px">
        <!-- 第一行：基本信息 -->
        <el-row :gutter="20">
          <el-col :span="12">
            <!-- 推荐位编码：前端代码中使用的唯一标识符，建议使用下划线分隔的英文 -->
            <el-form-item label="推荐位编码" prop="code">
              <el-input v-model="form.code" placeholder="请输入推荐位编码，如：home_banner" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <!-- 推荐位名称：用户友好的显示名称，用于后台管理 -->
            <el-form-item label="推荐位名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入推荐位名称，如：首页横幅" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <!-- 第二行：页面和类型 -->
        <el-row :gutter="20">
          <el-col :span="12">
            <!-- 所属页面：推荐位在哪个页面显示，影响前端路由和权限控制 -->
            <el-form-item label="所属页面" prop="page">
              <el-select v-model="form.page" placeholder="请选择所属页面" style="width: 100%">
                <el-option label="首页" value="home" />
                <el-option label="发现页" value="discover" />
                <el-option label="分类页" value="category" />
                <el-option label="我的页" value="mine" />
                <el-option label="播放页" value="player" />
                <el-option label="搜索结果页" value="search" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <!-- 推荐位类型：决定内容的展示方式，影响前端组件的渲染 -->
            <el-form-item label="推荐位类型" prop="type">
              <el-select v-model="form.type" placeholder="请选择推荐位类型" style="width: 100%">
                <el-option label="横幅" value="banner" />
                <el-option label="列表" value="list" />
                <el-option label="网格" value="grid" />
                <el-option label="轮播" value="carousel" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <!-- 第三行：显示顺序和状态 -->
        <el-row :gutter="20">
          <el-col :span="12">
            <!-- 显示顺序：同一页面中多个推荐位的排序，数字越小越靠前 -->
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
            <!-- 状态：启用或停用该推荐位，停用后不会在前端显示 -->
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio value="0">启用</el-radio>
                <el-radio value="1">停用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        
        <!-- 第四行：尺寸和样式 -->
        <el-row :gutter="20">
          <el-col :span="12">
            <!-- 宽度：推荐位的宽度，支持百分比和像素值 -->
            <el-form-item label="宽度" prop="width">
              <el-input v-model="form.width" placeholder="如：100% 或 750px" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <!-- 高度：推荐位的高度，支持像素值和auto -->
            <el-form-item label="高度" prop="height">
              <el-input v-model="form.height" placeholder="如：200px 或 auto" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <!-- 第五行：备注 -->
        <el-form-item label="备注" prop="remark">
          <el-input 
            v-model="form.remark" 
            type="textarea" 
            :rows="3"
            placeholder="请输入备注信息，如：用于展示首页顶部的横幅广告" />
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

// 数据列表
const recommendSlotList = ref<RecommendSlotVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

// 表单引用
const queryFormRef = ref<ElFormInstance>();
const recommendSlotFormRef = ref<ElFormInstance>();

// 对话框控制
const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

// 表单初始数据
const initFormData: RecommendSlotForm = {
  id: undefined,
  code: undefined,
  name: undefined,
  page: undefined,
  status: undefined,
  orderNum: undefined,
  type: undefined,
  width: undefined,
  height: undefined,
  remark: undefined,
}

// 页面数据管理
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
    code: [
      { required: true, message: "推荐位编码不能为空", trigger: "blur" }
    ],
    name: [
      { required: true, message: "推荐位名称不能为空", trigger: "blur" }
    ],
    page: [
      { required: true, message: "所属页面不能为空", trigger: "blur" }
    ],
    type: [
      { required: true, message: "推荐位类型不能为空", trigger: "blur" }
    ],
    status: [
      { required: true, message: "状态不能为空", trigger: "change" }
    ],
    orderNum: [
      { required: true, message: "显示顺序不能为空", trigger: "blur" }
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

/** 获取页面标签类型 */
const getPageTagType = (page: string): string => {
  switch (page) {
    case 'home':
      return 'primary';
    case 'discover':
      return 'success';
    case 'category':
      return 'warning';
    case 'mine':
      return 'info';
    case 'player':
      return 'danger';
    case 'search':
      return 'warning';
    default:
      return 'info';
  }
}

/** 获取页面标签文本 */
const getPageLabel = (page: string) => {
  switch (page) {
    case 'home':
      return '首页';
    case 'discover':
      return '发现页';
    case 'category':
      return '分类页';
    case 'mine':
      return '我的页';
    case 'player':
      return '播放页';
    case 'search':
      return '搜索结果页';
    default:
      return page;
  }
}

/** 获取类型标签类型 */
const getTypeTagType = (type: string): string => {
  switch (type) {
    case 'banner':
      return 'success';
    case 'list':
      return 'warning';
    case 'grid':
      return 'info';
    case 'carousel':
      return 'danger';
    default:
      return 'info';
  }
}

/** 获取类型标签文本 */
const getTypeLabel = (type: string) => {
  switch (type) {
    case 'banner':
      return '横幅';
    case 'list':
      return '列表';
    case 'grid':
      return '网格';
    case 'carousel':
      return '轮播';
    default:
      return type || '未设置';
  }
}

onMounted(() => {
  getList();
});
</script>

<style lang="scss" scoped>
</style>
