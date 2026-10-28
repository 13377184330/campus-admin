<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
      <el-form-item label="学生姓名" prop="studentName">
        <el-input v-model="queryParams.params.studentName" placeholder="请输入学生姓名" clearable style="width: 220px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="原班级" prop="fromClassId">
        <el-select v-model="queryParams.fromClassId" placeholder="请选择原班级" clearable style="width: 220px">
          <el-option v-for="item in classOptions" :key="item.classId" :label="item.className" :value="item.classId" />
        </el-select>
      </el-form-item>
      <el-form-item label="新班级" prop="toClassId">
        <el-select v-model="queryParams.toClassId" placeholder="请选择新班级" clearable style="width: 220px">
          <el-option v-for="item in classOptions" :key="item.classId" :label="item.className" :value="item.classId" />
        </el-select>
      </el-form-item>
      <el-form-item label="调班时间">
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
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['stu:transfer:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['stu:transfer:export']">导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="transferList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="学生姓名" align="center" prop="studentName" min-width="100" />
      <el-table-column label="学生编号" align="center" prop="studentNo" min-width="100" />
      <el-table-column label="原班级" align="center" prop="fromClassName" min-width="100" />
      <el-table-column label="新班级" align="center" prop="toClassName" min-width="100" />
      <el-table-column label="调班原因" align="center" prop="reason" min-width="160" :show-overflow-tooltip="true" />
      <el-table-column label="操作人" align="center" min-width="90">
        <template #default="scope">{{ scope.row.createByName || scope.row.createBy }}</template>
      </el-table-column>
      <el-table-column label="调班时间" align="center" prop="createTime" min-width="160" />
      <el-table-column label="操作" width="90" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['stu:transfer:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
  </div>
</template>

<script setup name="StuTransfer">
import { listTransfer, delTransfer } from "@/api/stu/transfer"
import { optionClass } from "@/api/stu/class"

const { proxy } = getCurrentInstance()

const transferList = ref([])
const classOptions = ref([])
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
    fromClassId: undefined,
    toClassId: undefined,
    params: {
      studentName: undefined
    }
  }
})

const { queryParams } = toRefs(data)

/** 查询调班记录列表 */
function getList() {
  loading.value = true
  listTransfer(proxy.addDateRange(queryParams.value, dateRange.value)).then(response => {
    transferList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

/** 查询班级下拉 */
function getClassOptions() {
  optionClass({}).then(response => {
    classOptions.value = response.data || []
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
  queryParams.value.params = { studentName: undefined }
  queryParams.value.fromClassId = undefined
  queryParams.value.toClassId = undefined
  handleQuery()
}

/** 多选框选中数据 */
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.transferId)
  multiple.value = !selection.length
}

/** 删除按钮操作 */
function handleDelete(row) {
  const transferIds = row.transferId || ids.value
  proxy.$modal.confirm('是否确认删除选中的调班记录？').then(function () {
    return delTransfer(transferIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download("stu/transfer/export", {
    ...queryParams.value
  }, `调班记录_${new Date().getTime()}.xlsx`)
}

getClassOptions()
getList()
</script>
