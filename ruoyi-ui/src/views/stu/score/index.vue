<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
      <el-form-item label="学生姓名" prop="studentName">
        <el-input v-model="queryParams.params.studentName" placeholder="请输入学生姓名" clearable style="width: 220px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="变动类型" prop="changeType">
        <el-select v-model="queryParams.params.changeType" placeholder="全部" clearable style="width: 180px">
          <el-option label="加分" value="add" />
          <el-option label="扣分" value="sub" />
        </el-select>
      </el-form-item>
      <el-form-item label="操作时间">
        <el-date-picker
          v-model="dateRange"
          value-format="YYYY-MM-DD"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          style="width: 300px"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['stu:score:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['stu:score:export']">导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="scoreList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="学生姓名" align="center" prop="studentName" min-width="100" />
      <el-table-column label="学生编号" align="center" prop="studentNo" min-width="100" />
      <el-table-column label="变动分值" align="center" prop="changeScore" width="110">
        <template #default="scope">
          <span :class="scope.row.changeScore >= 0 ? 'score-up' : 'score-down'">
            {{ scope.row.changeScore > 0 ? '+' + scope.row.changeScore : scope.row.changeScore }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="变动后积分" align="center" prop="scoreAfter" width="110" />
      <el-table-column label="加/减分项目" align="center" prop="reason" min-width="140" :show-overflow-tooltip="true" />
      <el-table-column label="备注" align="center" prop="remark" min-width="160" :show-overflow-tooltip="true" />
      <el-table-column label="操作老师" align="center" min-width="90">
        <template #default="scope">{{ scope.row.createByName || scope.row.createBy }}</template>
      </el-table-column>
      <el-table-column label="操作时间" align="center" prop="createTime" min-width="160" />
      <el-table-column label="操作" width="90" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['stu:score:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
  </div>
</template>

<script setup name="StuScore">
import { listScore, delScore } from "@/api/stu/score"

const { proxy } = getCurrentInstance()

const scoreList = ref([])
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const multiple = ref(true)
const total = ref(0)
const dateRange = ref([])

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    params: {
      studentName: undefined,
      changeType: undefined
    }
  }
})

const { queryParams } = toRefs(data)

/** 查询积分流水列表 */
function getList() {
  loading.value = true
  listScore(proxy.addDateRange(queryParams.value, dateRange.value)).then(response => {
    scoreList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  dateRange.value = []
  proxy.resetForm("queryRef")
  queryParams.value.params = { studentName: undefined, changeType: undefined }
  handleQuery()
}

/** 多选框选中数据 */
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.logId)
  multiple.value = !selection.length
}

/** 删除按钮操作 */
function handleDelete(row) {
  const logIds = row.logId || ids.value
  proxy.$modal.confirm('是否确认删除选中的积分流水记录？').then(function () {
    return delScore(logIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download("stu/score/export", {
    ...queryParams.value
  }, `积分流水_${new Date().getTime()}.xlsx`)
}

getList()
</script>

<style scoped>
.score-up {
  color: #2e6b5f;
  font-weight: 600;
}
.score-down {
  color: #d9534f;
  font-weight: 600;
}
</style>
