<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="搜索关键字" prop="keyword">
              <el-input 
                v-model="queryParams.keyword" 
                placeholder="请输入搜索关键字" 
                clearable 
                @keyup.enter="handleQuery"
                style="width: 200px;"
              />
            </el-form-item>
            <el-form-item label="搜索次数" prop="times">
              <el-select 
                v-model="queryParams.times" 
                placeholder="请选择搜索次数" 
                clearable
                style="width: 150px;"
                @change="handleTimesFilter"
              >
                <el-option label="1次" :value="1" />
                <el-option label="2-5次" :value="2" />
                <el-option label="6-10次" :value="6" />
                <el-option label="10次以上" :value="11" />
              </el-select>
            </el-form-item>
            <el-form-item label="搜索时间范围" prop="dateRange">
              <el-date-picker
                v-model="dateRange"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                value-format="YYYY-MM-DD"
                @change="handleDateRangeChange"
                style="width: 300px;"
              />
            </el-form-item>
            <el-form-item label="排序方式" prop="orderBy">
              <el-select 
                v-model="queryParams.orderBy" 
                placeholder="请选择排序方式" 
                clearable
                style="width: 150px;"
              >
                <el-option label="按搜索次数降序" value="times_desc" />
                <el-option label="按搜索次数升序" value="times_asc" />
                <el-option label="按最后搜索时间降序" value="lastTime_desc" />
                <el-option label="按最后搜索时间升序" value="lastTime_asc" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
              <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
          
          <!-- 快速筛选按钮 -->
          <div class="mt-3 flex gap-2">
            <el-button size="small" @click="quickFilter('today')">今日搜索</el-button>
            <el-button size="small" @click="quickFilter('week')">本周搜索</el-button>
            <el-button size="small" @click="quickFilter('month')">本月搜索</el-button>
            <el-button size="small" @click="quickFilter('hot')">热门搜索</el-button>
          </div>
        </el-card>
      </div>
    </transition>

    <!-- 统计信息卡片 -->
    <el-card shadow="hover" class="mb-[10px]">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="text-center">
            <div class="text-2xl font-bold text-blue-600">{{ total }}</div>
            <div class="text-gray-500">总记录数</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="text-center">
            <div class="text-2xl font-bold text-green-600">{{ uniqueKeywords }}</div>
            <div class="text-gray-500">唯一关键字</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="text-center">
            <div class="text-2xl font-bold text-orange-600">{{ avgSearchTimes }}</div>
            <div class="text-gray-500">平均搜索次数</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="text-center">
            <div class="text-2xl font-bold text-purple-600">{{ todaySearches }}</div>
            <div class="text-gray-500">今日搜索</div>
          </div>
        </el-col>
      </el-row>
    </el-card>

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

      <el-table v-loading="loading" border :data="searchHistoryList" @selection-change="handleSelectionChange" stripe>
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="主键" align="center" prop="id" v-if="false" />
        <el-table-column label="搜索关键字" align="center" prop="keyword" min-width="200">
          <template #default="scope">
            <el-tag type="info" effect="plain" class="cursor-pointer" @click="copyKeyword(scope.row.keyword)">
              {{ scope.row.keyword }}
              <el-icon class="ml-1"><CopyDocument /></el-icon>
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="搜索次数" align="center" prop="times" width="120">
          <template #default="scope">
            <el-tag :type="getTimesTagType(scope.row.times)" effect="dark">
              {{ scope.row.times }}次
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="最后搜索时间" align="center" prop="lastTime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.lastTime, '{y}-{m}-{d} {h}:{i}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
          <template #default="scope">
            <el-tooltip content="查看详情" placement="top">
              <el-button link type="primary" icon="View" @click="handleView(scope.row)"></el-button>
            </el-tooltip>
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
      <el-form ref="searchHistoryFormRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户ID" />
        </el-form-item>
        <el-form-item label="搜索关键字" prop="keyword">
          <el-input v-model="form.keyword" placeholder="请输入搜索关键字" />
        </el-form-item>
        <el-form-item label="搜索次数" prop="times">
          <el-input-number v-model="form.times" :min="1" :max="999" placeholder="请输入搜索次数" />
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
const dateRange = ref<[string, string] | null>(null);

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
    keyword: [
      { required: true, message: "搜索关键字不能为空", trigger: "blur" }
    ],
    times: [
      { required: true, message: "搜索次数不能为空", trigger: "blur" }
    ],
    lastTime: [
      { required: true, message: "最后搜索时间不能为空", trigger: "blur" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

// 统计数据
const uniqueKeywords = ref(0);
const avgSearchTimes = ref(0);
const todaySearches = ref(0);

/** 查询搜索历史列表 */
const getList = async () => {
  loading.value = true;
  try {
    const res = await listSearchHistory(queryParams.value);
    searchHistoryList.value = res.rows;
    total.value = res.total;
    
    // 计算统计数据
    calculateStatistics();
  } catch (error) {
    console.error('获取搜索历史列表失败:', error);
  } finally {
    loading.value = false;
  }
}

/** 计算统计数据 */
const calculateStatistics = () => {
  if (searchHistoryList.value.length === 0) {
    uniqueKeywords.value = 0;
    avgSearchTimes.value = 0;
    todaySearches.value = 0;
    return;
  }
  
  // 计算唯一关键字数量
  const uniqueSet = new Set(searchHistoryList.value.map(item => item.keyword));
  uniqueKeywords.value = uniqueSet.size;
  
  // 计算平均搜索次数
  const totalTimes = searchHistoryList.value.reduce((sum, item) => sum + item.times, 0);
  avgSearchTimes.value = Math.round(totalTimes / searchHistoryList.value.length);
  
  // 计算今日搜索数量
  const today = new Date().toISOString().split('T')[0];
  todaySearches.value = searchHistoryList.value.filter(item => 
    item.lastTime && item.lastTime.startsWith(today)
  ).length;
}

/** 处理日期范围变化 */
const handleDateRangeChange = (dates: [string, string] | null) => {
  if (dates) {
    queryParams.value.params = {
      ...queryParams.value.params,
      beginTime: dates[0],
      endTime: dates[1]
    };
  } else {
    queryParams.value.params = {
      ...queryParams.value.params,
      beginTime: undefined,
      endTime: undefined
    };
  }
}

/** 处理搜索次数筛选 */
const handleTimesFilter = (value: number) => {
  if (value === 1) {
    queryParams.value.params = {
      ...queryParams.value.params,
      timesMin: 1,
      timesMax: 1
    };
  } else if (value === 2) {
    queryParams.value.params = {
      ...queryParams.value.params,
      timesMin: 2,
      timesMax: 5
    };
  } else if (value === 6) {
    queryParams.value.params = {
      ...queryParams.value.params,
      timesMin: 6,
      timesMax: 10
    };
  } else if (value === 11) {
    queryParams.value.params = {
      ...queryParams.value.params,
      timesMin: 11,
      timesMax: undefined
    };
  } else {
    queryParams.value.params = {
      ...queryParams.value.params,
      timesMin: undefined,
      timesMax: undefined
    };
  }
}

/** 快速筛选 */
const quickFilter = (type: string) => {
  // 重置其他筛选条件
  queryParams.value.keyword = undefined;
  queryParams.value.times = undefined;
  dateRange.value = null;
  queryParams.value.orderBy = undefined;
  
  const today = new Date();
  const todayStr = today.toISOString().split('T')[0];
  
  switch (type) {
    case 'today':
      queryParams.value.params = {
        beginTime: todayStr,
        endTime: todayStr
      };
      break;
    case 'week':
      const weekStart = new Date(today);
      weekStart.setDate(today.getDate() - today.getDay());
      const weekEnd = new Date(weekStart);
      weekEnd.setDate(weekStart.getDate() + 6);
      queryParams.value.params = {
        beginTime: weekStart.toISOString().split('T')[0],
        endTime: weekEnd.toISOString().split('T')[0]
      };
      break;
    case 'month':
      const monthStart = new Date(today.getFullYear(), today.getMonth(), 1);
      const monthEnd = new Date(today.getFullYear(), today.getMonth() + 1, 0);
      queryParams.value.params = {
        beginTime: monthStart.toISOString().split('T')[0],
        endTime: monthEnd.toISOString().split('T')[0]
      };
      break;
    case 'hot':
      queryParams.value.orderBy = 'times_desc';
      queryParams.value.params = {
        timesMin: 5
      };
      break;
  }
  
  handleQuery();
}

/** 复制关键字到剪贴板 */
const copyKeyword = async (keyword: string) => {
  try {
    await navigator.clipboard.writeText(keyword);
    proxy?.$modal.msgSuccess('关键字已复制到剪贴板');
  } catch (error) {
    console.error('复制失败:', error);
    proxy?.$modal.msgError('复制失败');
  }
}

/** 查看详情 */
const handleView = (row: SearchHistoryVO) => {
  proxy?.$modal.msgSuccess(`关键字: ${row.keyword}\n搜索次数: ${row.times}次\n最后搜索时间: ${proxy?.parseTime(row.lastTime, '{y}-{m}-{d} {h}:{i}')}`);
}

/** 获取搜索次数标签类型 */
const getTimesTagType = (times: number) => {
  if (times === 1) return 'info';
  if (times <= 5) return 'success';
  if (times <= 10) return 'warning';
  return 'danger';
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
  dateRange.value = null;
  queryFormRef.value?.resetFields();
  queryParams.value.params = {};
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
  try {
    const res = await getSearchHistory(_id);
    Object.assign(form.value, res.data);
    dialog.visible = true;
    dialog.title = "修改搜索历史";
  } catch (error) {
    console.error('获取搜索历史详情失败:', error);
  }
}

/** 提交按钮 */
const submitForm = () => {
  searchHistoryFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      try {
        if (form.value.id) {
          await updateSearchHistory(form.value);
        } else {
          await addSearchHistory(form.value);
        }
        proxy?.$modal.msgSuccess("操作成功");
        dialog.visible = false;
        await getList();
      } catch (error) {
        console.error('提交表单失败:', error);
      } finally {
        buttonLoading.value = false;
      }
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: SearchHistoryVO) => {
  const _ids = row?.id || ids.value;
  try {
    await proxy?.$modal.confirm('是否确认删除搜索历史编号为"' + _ids + '"的数据项？');
    await delSearchHistory(_ids);
    proxy?.$modal.msgSuccess("删除成功");
    await getList();
  } catch (error) {
    console.error('删除失败:', error);
  }
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

<style scoped>
.mg-home {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.cursor-pointer {
  cursor: pointer;
}

.cursor-pointer:hover {
  opacity: 0.8;
}

.text-2xl {
  font-size: 1.5rem;
  line-height: 2rem;
}

.font-bold {
  font-weight: 700;
}

.text-blue-600 {
  color: #2563eb;
}

.text-green-600 {
  color: #16a34a;
}

.text-orange-600 {
  color: #ea580c;
}

.text-purple-600 {
  color: #9333ea;
}

.text-gray-500 {
  color: #6b7280;
}

.text-center {
  text-align: center;
}

.mb-\[10px\] {
  margin-bottom: 10px;
}

.mt-3 {
  margin-top: 0.75rem;
}

.flex {
  display: flex;
}

.gap-2 {
  gap: 0.5rem;
}

.ml-1 {
  margin-left: 0.25rem;
}
</style>
