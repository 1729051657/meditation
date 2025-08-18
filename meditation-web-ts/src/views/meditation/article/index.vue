<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="标题" prop="title">
              <el-input v-model="queryParams.title" placeholder="请输入标题" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="状态" prop="status">
              <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
                <el-option label="启用" value="0" />
                <el-option label="停用" value="1" />
              </el-select>
            </el-form-item>
            <el-form-item label="摘要" prop="summary">
              <el-input v-model="queryParams.summary" placeholder="请输入摘要" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="作者" prop="authorId">
              <el-select v-model="queryParams.authorId" placeholder="请选择作者" clearable>
                <el-option
                  v-for="user in userList"
                  :key="user.userId"
                  :label="user.nickName"
                  :value="user.userId"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="发布时间" prop="publishTime">
              <el-date-picker clearable
                v-model="queryParams.publishTime"
                type="date"
                value-format="YYYY-MM-DD"
                placeholder="请选择发布时间"
              />
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
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['meditation:article:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['meditation:article:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['meditation:article:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['meditation:article:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" border :data="articleList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="主键" align="center" prop="id" v-if="false" />
        <el-table-column label="标题" align="center" prop="title" />
        <el-table-column label="封面" align="center" prop="cover">
          <template #default="scope">
            <image-preview :src="scope.row.cover" :width="50" :height="50"/>
          </template>
        </el-table-column>
        <el-table-column label="摘要" align="center" prop="summary" :show-overflow-tooltip="true" />
        <el-table-column label="作者" align="center" prop="authorId">
          <template #default="scope">
            <span>{{ userList.find(u => u.userId == scope.row.authorId)?.nickName || scope.row.authorId }}</span>
          </template>
        </el-table-column>
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
        <el-table-column label="显示顺序" align="center" prop="orderNum" />
        <el-table-column label="备注" align="center" prop="remark" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['meditation:article:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['meditation:article:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改冥想知识文章对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="800px" append-to-body>
      <el-form ref="articleFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="封面" prop="cover">
          <image-upload v-model="form.cover" :limit="1" />
        </el-form-item>
        <el-form-item label="摘要" prop="summary">
            <el-input v-model="form.summary" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="内容">
          <editor v-model="form.content" :min-height="192"/>
        </el-form-item>
        <el-form-item label="作者" prop="authorId">
          <el-select v-model="form.authorId" placeholder="请选择作者">
            <el-option
              v-for="user in userList"
              :key="user.userId"
              :label="user.nickName"
              :value="user.userId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="发布时间" prop="publishTime">
          <el-date-picker clearable
            v-model="form.publishTime"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="请选择发布时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="显示顺序" prop="orderNum">
          <el-input v-model="form.orderNum" placeholder="请输入显示顺序" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio value="0">启用</el-radio>
            <el-radio value="1">停用</el-radio>
          </el-radio-group>
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

<script setup name="Article" lang="ts">
import { listArticle, getArticle, delArticle, addArticle, updateArticle } from '@/api/meditation/article';
import { ArticleVO, ArticleQuery, ArticleForm } from '@/api/meditation/article/types';
import { listUser } from '@/api/system/user';
import { UserVO } from '@/api/system/user/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const articleList = ref<ArticleVO[]>([]);
const userList = ref<UserVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const articleFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: ArticleForm = {
  id: undefined,
  title: undefined,
  cover: undefined,
  summary: undefined,
  content: undefined,
  authorId: undefined,
  status: undefined,
  publishTime: undefined,
  orderNum: undefined,
  remark: undefined
}
const data = reactive<PageData<ArticleForm, ArticleQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    title: undefined,
    cover: undefined,
    summary: undefined,
    content: undefined,
    authorId: undefined,
    status: undefined,
    publishTime: undefined,
    orderNum: undefined,
    params: {
    }
  },
  rules: {
    cover: [
      { required: true, message: "封面不能为空", trigger: "blur" }
    ],
    summary: [
      { required: true, message: "摘要不能为空", trigger: "blur" }
    ],
    content: [
      { required: true, message: "内容不能为空", trigger: "blur" }
    ],
    authorId: [
      { required: true, message: "作者不能为空", trigger: "blur" }
    ],
    status: [
      { required: true, message: "状态不能为空", trigger: "change" }
    ],
    publishTime: [
      { required: true, message: "发布时间不能为空", trigger: "blur" }
    ],
    orderNum: [
      { required: true, message: "显示顺序不能为空", trigger: "blur" }
    ],
    remark: [
      { required: true, message: "备注不能为空", trigger: "blur" }
    ]
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询冥想知识文章列表 */
const getList = async () => {
  loading.value = true;
  const res = await listArticle(queryParams.value);
  articleList.value = res.rows;
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
  articleFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: ArticleVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加冥想知识文章";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: ArticleVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getArticle(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改冥想知识文章";
}

/** 提交按钮 */
const submitForm = () => {
  articleFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateArticle(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addArticle(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: ArticleVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除冥想知识文章编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delArticle(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('meditation/article/export', {
    ...queryParams.value
  }, `article_${new Date().getTime()}.xlsx`)
}

/** 获取用户列表 */
const getUserList = async () => {
  const res = await listUser({ pageNum: 1, pageSize: 100 });
  userList.value = res.rows;
}

onMounted(() => {
  getList();
  getUserList();
});
</script>
