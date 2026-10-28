<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
      <el-form-item label="姓名" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入学生姓名" clearable style="width: 220px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="学生编号" prop="studentNo">
        <el-input v-model="queryParams.studentNo" placeholder="请输入学生编号" clearable style="width: 220px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="所在班级" prop="classId">
        <el-select v-model="queryParams.classId" placeholder="请选择班级" clearable style="width: 220px">
          <el-option v-for="item in classOptions" :key="item.classId" :label="item.className" :value="item.classId" />
        </el-select>
      </el-form-item>
      <el-form-item label="家长联系人" prop="parentName">
        <el-input v-model="queryParams.parentName" placeholder="请输入家长姓名" clearable style="width: 220px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="学生状态" clearable style="width: 160px">
          <el-option label="在校" value="0" />
          <el-option label="离校" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['stu:student:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate" v-hasPermi="['stu:student:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['stu:student:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['stu:student:export']">导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="info" plain icon="Upload" @click="handleImport" v-hasPermi="['stu:student:import']">导入</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="studentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="学生编号" align="center" prop="studentNo" min-width="100" />
      <el-table-column label="姓名" align="center" prop="name" min-width="80" />
      <el-table-column label="性别" align="center" prop="gender" width="60">
        <template #default="scope">
          <span>{{ scope.row.gender === '0' ? '男' : '女' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="所在班级" align="center" prop="className" min-width="90" />
      <el-table-column label="当前积分" align="center" prop="totalScore" min-width="90" sortable>
        <template #default="scope">
          <span :class="scoreClass(scope.row.totalScore)">{{ scope.row.totalScore }}</span>
        </template>
      </el-table-column>
      <el-table-column label="家长联系人" align="center" prop="parentName" min-width="90" />
      <el-table-column label="关系" align="center" prop="parentRelation" min-width="60" />
      <el-table-column label="家长手机号" align="center" prop="parentPhone" min-width="112" />
      <el-table-column label="状态" align="center" prop="status" width="72">
        <template #default="scope">
          <el-tag :type="scope.row.status === '0' ? 'success' : 'info'">{{ scope.row.status === '0' ? '在校' : '离校' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="340" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="View" @click="handleDetail(scope.row)">详情</el-button>
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['stu:student:edit']">修改</el-button>
          <el-button link type="primary" icon="Star" @click="handleScore(scope.row)" v-hasPermi="['stu:student:score']">加减分</el-button>
          <el-button link type="primary" icon="Switch" @click="handleTransfer(scope.row)" v-hasPermi="['stu:student:transfer']">调班</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['stu:student:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />

    <!-- 新增/修改学生对话框 -->
    <el-dialog :title="title" v-model="open" width="700px" append-to-body>
      <el-form ref="studentRef" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="学生编号" prop="studentNo">
              <el-input v-model="form.studentNo" placeholder="留空将自动生成" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="form.name" placeholder="请输入学生姓名" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-radio-group v-model="form.gender">
                <el-radio value="0">男</el-radio>
                <el-radio value="1">女</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所在班级" prop="classId">
              <el-select v-model="form.classId" placeholder="请选择班级" clearable style="width: 100%">
                <el-option v-for="item in classOptions" :key="item.classId" :label="item.className" :value="item.classId" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="出生日期" prop="birthDate">
              <el-date-picker v-model="form.birthDate" type="date" value-format="YYYY-MM-DD" placeholder="请选择出生日期" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="入学日期" prop="enrollDate">
              <el-date-picker v-model="form.enrollDate" type="date" value-format="YYYY-MM-DD" placeholder="请选择入学日期" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <div class="section-title">家长联系方式</div>
        <el-row>
          <el-col :span="12">
            <el-form-item label="家长联系人" prop="parentName">
              <el-input v-model="form.parentName" placeholder="请输入家长姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="与学生关系" prop="parentRelation">
              <el-input v-model="form.parentRelation" placeholder="如：父亲 / 母亲" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="家长手机号" prop="parentPhone">
              <el-input v-model="form.parentPhone" placeholder="请输入手机号" maxlength="11" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio value="0">在校</el-radio>
                <el-radio value="1">离校</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 加减分对话框 -->
    <el-dialog title="学生积分增减" v-model="scoreOpen" width="520px" append-to-body>
      <el-form ref="scoreRef" :model="scoreForm" :rules="scoreRules" label-width="100px">
        <el-form-item label="学生">
          <el-input v-model="scoreForm.studentName" disabled />
        </el-form-item>
        <el-form-item label="当前积分">
          <el-input v-model="scoreForm.totalScore" disabled />
        </el-form-item>
        <el-form-item label="变动分值" prop="changeScore">
          <el-input-number v-model="scoreForm.changeScore" :min="-100" :max="100" controls-position="right" style="width: 150px" />
          <span class="tip-text">（正数为加分，负数为扣分）</span>
          <div class="quick-btns">
            <el-button size="small" @click="scoreForm.changeScore = 1">+1</el-button>
            <el-button size="small" @click="scoreForm.changeScore = 2">+2</el-button>
            <el-button size="small" @click="scoreForm.changeScore = 5">+5</el-button>
            <el-button size="small" @click="scoreForm.changeScore = -1">-1</el-button>
            <el-button size="small" @click="scoreForm.changeScore = -2">-2</el-button>
            <el-button size="small" @click="scoreForm.changeScore = -5">-5</el-button>
          </div>
        </el-form-item>
        <el-form-item label="加/减分项目" prop="reason">
          <el-input v-model="scoreForm.reason" placeholder="如：课堂表现优秀 / 违反作息纪律" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="scoreForm.remark" type="textarea" placeholder="可选" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitScore">确 定</el-button>
          <el-button @click="scoreOpen = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 调班对话框 -->
    <el-dialog title="学生调班" v-model="transferOpen" width="520px" append-to-body>
      <el-form ref="transferRef" :model="transferForm" :rules="transferRules" label-width="100px">
        <el-form-item label="学生">
          <el-input v-model="transferForm.studentName" disabled />
        </el-form-item>
        <el-form-item label="原班级">
          <el-input v-model="transferForm.fromClassName" disabled />
        </el-form-item>
        <el-form-item label="新班级" prop="toClassId">
          <el-select v-model="transferForm.toClassId" placeholder="请选择要调入的班级" style="width: 100%">
            <el-option v-for="item in classOptions" :key="item.classId" :label="item.className" :value="item.classId" />
          </el-select>
        </el-form-item>
        <el-form-item label="调班原因" prop="reason">
          <el-input v-model="transferForm.reason" type="textarea" placeholder="如：表现进步，调整至强化班" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitTransfer">确 定</el-button>
          <el-button @click="transferOpen = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 学生详情抽屉 -->
    <el-drawer title="学生详情" v-model="detailOpen" size="620px" append-to-body>
      <div v-if="detail.student" class="detail-wrap">
        <el-descriptions title="基本信息" :column="2" border>
          <el-descriptions-item label="学生编号">{{ detail.student.studentNo }}</el-descriptions-item>
          <el-descriptions-item label="姓名">{{ detail.student.name }}</el-descriptions-item>
          <el-descriptions-item label="性别">{{ detail.student.gender === '0' ? '男' : '女' }}</el-descriptions-item>
          <el-descriptions-item label="所在班级">{{ detail.student.className || '未分班' }}</el-descriptions-item>
          <el-descriptions-item label="出生日期">{{ detail.student.birthDate || '-' }}</el-descriptions-item>
          <el-descriptions-item label="入学日期">{{ detail.student.enrollDate || '-' }}</el-descriptions-item>
          <el-descriptions-item label="当前积分">
            <span :class="scoreClass(detail.student.totalScore)">{{ detail.student.totalScore }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="状态">{{ detail.student.status === '0' ? '在校' : '离校' }}</el-descriptions-item>
          <el-descriptions-item label="家长联系人">{{ detail.student.parentName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="与学生关系">{{ detail.student.parentRelation || '-' }}</el-descriptions-item>
          <el-descriptions-item label="家长手机号">{{ detail.student.parentPhone || '-' }}</el-descriptions-item>
          <el-descriptions-item label="备注">{{ detail.student.remark || '-' }}</el-descriptions-item>
        </el-descriptions>

        <div class="section-title">积分流水</div>
        <el-table :data="detail.scores" size="small" max-height="240">
          <el-table-column label="时间" align="center" prop="createTime" width="160" />
          <el-table-column label="变动" align="center" width="80">
            <template #default="scope">
              <span :class="scope.row.changeScore >= 0 ? 'score-up' : 'score-down'">
                {{ scope.row.changeScore > 0 ? '+' + scope.row.changeScore : scope.row.changeScore }}
              </span>
            </template>
          </el-table-column>
          <el-table-column label="项目" align="center" prop="reason" :show-overflow-tooltip="true" />
          <el-table-column label="操作老师" align="center" width="100">
            <template #default="scope">{{ scope.row.createByName || scope.row.createBy }}</template>
          </el-table-column>
        </el-table>

        <div class="section-title">调班记录</div>
        <el-table :data="detail.transfers" size="small" max-height="200">
          <el-table-column label="时间" align="center" prop="createTime" width="160" />
          <el-table-column label="原班级" align="center" prop="fromClassName" />
          <el-table-column label="新班级" align="center" prop="toClassName" />
          <el-table-column label="原因" align="center" prop="reason" :show-overflow-tooltip="true" />
          <el-table-column label="操作人" align="center" width="100">
            <template #default="scope">{{ scope.row.createByName || scope.row.createBy }}</template>
          </el-table-column>
        </el-table>
      </div>
    </el-drawer>

    <!-- 学生导入对话框 -->
    <el-dialog :title="upload.title" v-model="upload.open" width="400px" append-to-body>
      <el-upload
        ref="uploadRef"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <template #tip>
          <div class="el-upload__tip text-center">
            <div class="el-upload__tip">
              <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的学生数据
            </div>
            <span>仅允许导入 xls、xlsx 格式文件。</span>
            <el-link type="primary" :underline="false" style="font-size: 12px; vertical-align: baseline" @click="importTemplate">下载模板</el-link>
          </div>
        </template>
      </el-upload>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitFileForm">确 定</el-button>
          <el-button @click="upload.open = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="StuStudent">
import { listStudent, getStudent, delStudent, addStudent, updateStudent, changeScore, transferStudent, getStudentScoreLog, getStudentTransferLog } from "@/api/stu/student"
import { optionClass } from "@/api/stu/class"
import { getToken } from "@/utils/auth"

const { proxy } = getCurrentInstance()

const studentList = ref([])
const classOptions = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const scoreOpen = ref(false)
const transferOpen = ref(false)
const detailOpen = ref(false)

const detail = reactive({
  student: null,
  scores: [],
  transfers: []
})

const upload = reactive({
  open: false,
  title: "",
  isUploading: false,
  updateSupport: 0,
  headers: { Authorization: "Bearer " + getToken() },
  url: import.meta.env.VITE_APP_BASE_API + "/stu/student/importData"
})

const data = reactive({
  form: {},
  scoreForm: {},
  transferForm: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: undefined,
    studentNo: undefined,
    classId: undefined,
    parentName: undefined,
    status: undefined
  },
  rules: {
    name: [{ required: true, message: "学生姓名不能为空", trigger: "blur" }],
    studentNo: [{ max: 30, message: "学生编号不能超过 30 个字符", trigger: "blur" }],
    parentPhone: [{ pattern: /^1[3-9]\d{9}$/, message: "请输入正确的手机号", trigger: "blur" }]
  },
  scoreRules: {
    changeScore: [{ required: true, message: "变动分值不能为空", trigger: "blur" }],
    reason: [{ required: true, message: "请填写加/减分项目", trigger: "blur" }]
  },
  transferRules: {
    toClassId: [{ required: true, message: "请选择要调入的班级", trigger: "change" }]
  }
})

const { queryParams, form, scoreForm, transferForm, rules, scoreRules, transferRules } = toRefs(data)

/** 积分颜色 */
function scoreClass(score) {
  if (score === null || score === undefined) return ""
  if (score >= 90) return "score-high"
  if (score < 60) return "score-low"
  return "score-normal"
}

/** 查询学生列表 */
function getList() {
  loading.value = true
  listStudent(queryParams.value).then(response => {
    studentList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

/** 查询班级下拉 */
function getClassOptions() {
  optionClass({ status: "0" }).then(response => {
    classOptions.value = response.data || []
  })
}

/** 取消按钮 */
function cancel() {
  open.value = false
  reset()
}

/** 表单重置 */
function reset() {
  form.value = {
    studentId: undefined,
    studentNo: undefined,
    name: undefined,
    gender: "0",
    birthDate: undefined,
    enrollDate: undefined,
    classId: undefined,
    totalScore: 0,
    parentName: undefined,
    parentRelation: undefined,
    parentPhone: undefined,
    status: "0",
    remark: undefined
  }
  proxy.resetForm("studentRef")
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef")
  handleQuery()
}

/** 多选框选中数据 */
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.studentId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  getClassOptions()
  open.value = true
  title.value = "新增学生"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  getClassOptions()
  const studentId = row.studentId || ids.value
  getStudent(studentId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改学生"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["studentRef"].validate(valid => {
    if (valid) {
      if (form.value.studentId != undefined) {
        updateStudent(form.value).then(() => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addStudent(form.value).then(() => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

/** 删除按钮操作 */
function handleDelete(row) {
  const studentIds = row.studentId || ids.value
  proxy.$modal.confirm('是否确认删除选中的学生？删除后其积分流水与调班记录也会一并清除。').then(function () {
    return delStudent(studentIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 加分/减分 */
function handleScore(row) {
  scoreForm.value = {
    studentId: row.studentId,
    studentName: row.name + "（" + (row.className || "未分班") + "）",
    totalScore: row.totalScore,
    changeScore: 1,
    reason: undefined,
    remark: undefined
  }
  scoreOpen.value = true
}

/** 提交积分变动 */
function submitScore() {
  proxy.$refs["scoreRef"].validate(valid => {
    if (valid) {
      changeScore(scoreForm.value).then(() => {
        proxy.$modal.msgSuccess("积分已更新")
        scoreOpen.value = false
        getList()
      })
    }
  })
}

/** 调班 */
function handleTransfer(row) {
  getClassOptions()
  transferForm.value = {
    studentId: row.studentId,
    studentName: row.name + "（" + row.studentNo + "）",
    fromClassName: row.className || "未分班",
    toClassId: undefined,
    reason: undefined
  }
  transferOpen.value = true
}

/** 提交调班 */
function submitTransfer() {
  proxy.$refs["transferRef"].validate(valid => {
    if (valid) {
      transferStudent(transferForm.value).then(() => {
        proxy.$modal.msgSuccess("调班成功")
        transferOpen.value = false
        getList()
      })
    }
  })
}

/** 查看详情 */
function handleDetail(row) {
  detail.student = row
  detail.scores = []
  detail.transfers = []
  detailOpen.value = true
  getStudentScoreLog(row.studentId).then(response => {
    detail.scores = response.data || []
  })
  getStudentTransferLog(row.studentId).then(response => {
    detail.transfers = response.data || []
  })
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download("stu/student/export", {
    ...queryParams.value
  }, `学生档案_${new Date().getTime()}.xlsx`)
}

/** 导入按钮操作 */
function handleImport() {
  upload.title = "学生数据导入"
  upload.open = true
}

/** 下载模板操作 */
function importTemplate() {
  proxy.download("stu/student/importTemplate", {}, `学生档案导入模板_${new Date().getTime()}.xlsx`)
}

/** 文件上传中处理 */
function handleFileUploadProgress() {
  upload.isUploading = true
}

/** 文件上传成功处理 */
function handleFileSuccess(response) {
  upload.open = false
  upload.isUploading = false
  proxy.$refs["uploadRef"].clearFiles()
  proxy.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true })
  getList()
}

/** 提交上传文件 */
function submitFileForm() {
  proxy.$refs["uploadRef"].submit()
}

getClassOptions()
getList()
</script>

<style scoped>
.tip-text {
  margin-left: 10px;
  color: #909399;
  font-size: 12px;
}
.quick-btns {
  margin-top: 8px;
}
.quick-btns .el-button {
  margin-right: 6px;
}
.score-high {
  color: #2e6b5f;
  font-weight: 600;
}
.score-normal {
  color: #4a5568;
  font-weight: 600;
}
.score-low {
  color: #d9534f;
  font-weight: 600;
}
.score-up {
  color: #2e6b5f;
  font-weight: 600;
}
.score-down {
  color: #d9534f;
  font-weight: 600;
}
.detail-wrap {
  padding: 0 10px;
}
/* 分组小标题：与概览页 panel-title 同一视觉语言（琥珀竖条） */
.section-title {
  font-size: 14px;
  font-weight: 600;
  color: #2f4a44;
  line-height: 1.2;
  padding-left: 10px;
  border-left: 3px solid #e89b3c;
  margin: 18px 0 14px 0;
}
.section-title:first-child {
  margin-top: 4px;
}
</style>
