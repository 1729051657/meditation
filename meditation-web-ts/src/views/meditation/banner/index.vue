<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="所属页面" prop="page">
              <el-select v-model="queryParams.page" placeholder="请选择所属页面" clearable style="width: 150px">
                <el-option label="首页" value="home" />
                <el-option label="发现页" value="discover" />
                <el-option label="分类页" value="category" />
                <el-option label="我的页" value="mine" />
                <el-option label="播放页" value="player" />
                <el-option label="搜索结果页" value="search" />
              </el-select>
            </el-form-item>
            <el-form-item label="链接目标" prop="linkTarget">
              <el-input v-model="queryParams.linkTarget" placeholder="请输入链接目标" clearable @keyup.enter="handleQuery" />
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
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['meditation:banner:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['meditation:banner:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['meditation:banner:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['meditation:banner:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" border :data="bannerList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="主键" align="center" prop="id" v-if="false" />
        <el-table-column label="所属页面" align="center" prop="page" />
        <el-table-column label="图片" align="center" prop="imageUrl" width="100">
          <template #default="scope">
            <image-preview :src="scope.row.imageUrl" :width="50" :height="50"/>
          </template>
        </el-table-column>
        <el-table-column label="链接类型" align="center" prop="linkType">
          <template #default="scope">
            <el-tag v-if="scope.row.linkType === 'none'">无链接</el-tag>
            <el-tag v-else-if="scope.row.linkType === 'url'" type="info">外部链接</el-tag>
            <el-tag v-else-if="scope.row.linkType === 'series'" type="success">系列详情</el-tag>
            <el-tag v-else-if="scope.row.linkType === 'article'" type="warning">文章详情</el-tag>
            <el-tag v-else>{{ scope.row.linkType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="链接目标" align="center" prop="linkTarget" :show-overflow-tooltip="true" />
        <el-table-column label="显示顺序" align="center" prop="orderNum" />
        <el-table-column label="状态" align="center" prop="status">
          <template #default="scope">
            <el-tag :type="scope.row.status === '0' ? 'success' : 'danger'">
              {{ scope.row.status === '0' ? '启用' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
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
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['meditation:banner:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['meditation:banner:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改横幅配置对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="800px" append-to-body>
      <el-form ref="bannerFormRef" :model="form" :rules="rules" label-width="100px">
        <!-- 第一行：所属页面和链接类型 -->
        <el-row :gutter="20">
          <el-col :span="12">
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
            <el-form-item label="链接类型" prop="linkType">
              <el-select v-model="form.linkType" placeholder="请选择链接类型" style="width: 100%">
                <el-option label="无链接" value="none" />
                <el-option label="外部链接" value="url" />
                <el-option label="系列详情" value="series" />
                <el-option label="文章详情" value="article" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <!-- 第二行：显示顺序和状态 -->
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
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio value="0">启用</el-radio>
                <el-radio value="1">停用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        
        <!-- 第三行：生效时间和失效时间 -->
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="生效时间" prop="startTime">
              <el-date-picker clearable
                v-model="form.startTime"
                type="datetime"
                value-format="YYYY-MM-DD HH:mm:ss"
                placeholder="请选择生效时间"
                style="width: 100%">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="失效时间" prop="endTime">
              <el-date-picker clearable
                v-model="form.endTime"
                type="datetime"
                value-format="YYYY-MM-DD HH:mm:ss"
                placeholder="请选择失效时间"
                style="width: 100%">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        
        <!-- 第四行：图片 -->
        <el-form-item label="图片" prop="image">
          <common-upload 
            v-model="form.image" 
            upload-type="image"
            :limit="1"
            :file-size="5"
            :compress-image="true"
            :compress-target-size="500"
          />
        </el-form-item>
        
        <!-- 第五行：链接目标 -->
        <el-form-item label="链接目标" prop="linkTarget">
          <el-input v-model="form.linkTarget" placeholder="请输入链接目标" />
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

<script setup name="Banner" lang="ts">
import { listBanner, getBanner, delBanner, addBanner, updateBanner } from '@/api/meditation/banner';
import { BannerVO, BannerQuery, BannerForm } from '@/api/meditation/banner/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const bannerList = ref<BannerVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const bannerFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: BannerForm = {
  id: undefined,
  page: undefined,
  image: undefined,
  linkType: undefined,
  linkTarget: undefined,
  orderNum: undefined,
  status: undefined,
  startTime: new Date().toISOString().slice(0, 19).replace('T', ' '),
  endTime: '9999-12-31 23:59:59',
}
const data = reactive<PageData<BannerForm, BannerQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    page: undefined,
    image: undefined,
    linkType: undefined,
    linkTarget: undefined,
    orderNum: undefined,
    status: undefined,
    startTime: undefined,
    endTime: undefined,
    params: {
    }
  },
  rules: {
    page: [
      { required: true, message: "所属页面不能为空", trigger: "blur" }
    ],
    image: [
      { required: true, message: "图片不能为空", trigger: "blur" }
    ],
    linkType: [
      { required: true, message: "链接类型不能为空", trigger: "change" }
    ],
    linkTarget: [
      { required: true, message: "链接目标不能为空", trigger: "blur" }
    ]
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询横幅配置列表 */
const getList = async () => {
  loading.value = true;
  const res = await listBanner(queryParams.value);
  bannerList.value = res.rows;
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
  bannerFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: BannerVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加横幅配置";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: BannerVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getBanner(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改横幅配置";
}

/** 提交按钮 */
const submitForm = () => {
  bannerFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateBanner(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addBanner(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: BannerVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除横幅配置编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delBanner(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('meditation/banner/export', {
    ...queryParams.value
  }, `banner_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
});
</script>
