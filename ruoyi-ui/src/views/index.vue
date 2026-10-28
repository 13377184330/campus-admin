<template>
  <div class="app-container dashboard">
    <div class="dash-header">
      <div>
        <div class="dash-title">学员管理概览</div>
        <div class="dash-sub">学生档案 · 积分管理 · 分班调班 数据实时汇总</div>
      </div>
      <el-button icon="Refresh" @click="loadData">刷新数据</el-button>
    </div>

    <!-- 指标卡 -->
    <el-row :gutter="16" class="stat-row">
      <el-col :xs="12" :sm="12" :md="6" v-for="item in statCards" :key="item.label">
        <div class="stat-card" :class="item.tone">
          <div class="stat-label">{{ item.label }}</div>
          <div class="stat-value">{{ item.value }}</div>
          <div class="stat-hint">{{ item.hint }}</div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="16">
      <!-- 各班人数 -->
      <el-col :xs="24" :md="12">
        <div class="panel">
          <div class="panel-title">各班人数</div>
          <div v-if="classCounts.length === 0" class="empty-tip">暂无班级数据</div>
          <div v-else class="class-list">
            <div class="class-item" v-for="c in classCounts" :key="c.classId">
              <div class="class-name">{{ c.className }}</div>
              <div class="class-bar-wrap">
                <div class="class-bar" :style="{ width: barWidth(c.studentCount) }"></div>
              </div>
              <div class="class-count">{{ c.studentCount }} 人</div>
            </div>
          </div>
        </div>
      </el-col>

      <!-- 积分分布 -->
      <el-col :xs="24" :md="12">
        <div class="panel">
          <div class="panel-title">积分分布</div>
          <div class="dist-list">
            <div class="dist-item" v-for="d in distItems" :key="d.label">
              <div class="dist-dot" :style="{ background: d.color }"></div>
              <div class="dist-label">{{ d.label }}</div>
              <div class="dist-value">{{ d.value }} 人</div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="mt16">
      <!-- 最近积分动态 -->
      <el-col :xs="24" :md="12">
        <div class="panel">
          <div class="panel-title">最近积分动态</div>
          <div v-if="recentScores.length === 0" class="empty-tip">暂无积分记录</div>
          <div v-else class="feed">
            <div class="feed-item" v-for="s in recentScores" :key="s.logId">
              <div class="feed-dot" :class="s.changeScore >= 0 ? 'dot-up' : 'dot-down'"></div>
              <div class="feed-body">
                <div class="feed-main">
                  <span class="feed-name">{{ s.studentName }}</span>
                  <span class="feed-reason">{{ s.reason }}</span>
                </div>
                <div class="feed-meta">{{ s.createByName || s.createBy }} · {{ formatTime(s.createTime) }}</div>
              </div>
              <div class="feed-score" :class="s.changeScore >= 0 ? 'score-up' : 'score-down'">
                {{ s.changeScore > 0 ? '+' + s.changeScore : s.changeScore }}
              </div>
            </div>
          </div>
        </div>
      </el-col>

      <!-- 最近调班动态 -->
      <el-col :xs="24" :md="12">
        <div class="panel">
          <div class="panel-title">最近调班动态</div>
          <div v-if="recentTransfers.length === 0" class="empty-tip">暂无调班记录</div>
          <div v-else class="feed">
            <div class="feed-item" v-for="t in recentTransfers" :key="t.transferId">
              <div class="feed-dot dot-move"></div>
              <div class="feed-body">
                <div class="feed-main">
                  <span class="feed-name">{{ t.studentName }}</span>
                  <span class="feed-reason">{{ t.fromClassName }} → {{ t.toClassName }}</span>
                </div>
                <div class="feed-meta">{{ t.createByName || t.createBy }} · {{ formatTime(t.createTime) }}</div>
              </div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup name="Index">
import { getOverview } from "@/api/stu/dashboard"

const overview = ref({})

const classCounts = computed(() => overview.value.classCounts || [])
const recentScores = computed(() => overview.value.recentScores || [])
const recentTransfers = computed(() => overview.value.recentTransfers || [])

const dist = computed(() => {
  const d = overview.value.scoreDistribution || {}
  return {
    excellent: Number(d.excellent || 0),
    good: Number(d.good || 0),
    pass: Number(d.pass || 0),
    warning: Number(d.warning || 0)
  }
})

const statCards = computed(() => [
  { label: "在校学生", value: overview.value.studentTotal || 0, hint: "当前在校学生总数", tone: "tone-primary" },
  { label: "班级数量", value: overview.value.classTotal || 0, hint: "已建立的班级数", tone: "tone-amber" },
  { label: "优秀（90 分以上）", value: dist.value.excellent, hint: "积分表现优秀的学生", tone: "tone-green" },
  { label: "需重点关注（60 分以下）", value: dist.value.warning, hint: "积分偏低的学生", tone: "tone-red" }
])

const distItems = computed(() => [
  { label: "优秀（≥90）", value: dist.value.excellent, color: "#2E6B5F" },
  { label: "良好（70-89）", value: dist.value.good, color: "#5B9E8F" },
  { label: "合格（60-69）", value: dist.value.pass, color: "#E89B3C" },
  { label: "需关注（<60）", value: dist.value.warning, color: "#D9534F" }
])

const maxCount = computed(() => {
  const list = classCounts.value.map(c => Number(c.studentCount || 0))
  return list.length ? Math.max(...list, 1) : 1
})

function barWidth(count) {
  const n = Number(count || 0)
  const pct = (n / maxCount.value) * 100
  return Math.max(pct, n > 0 ? 6 : 0) + "%"
}

function formatTime(t) {
  if (!t) return ""
  return String(t).replace("T", " ").slice(0, 19)
}

function loadData() {
  getOverview().then(response => {
    overview.value = response.data || {}
  })
}

loadData()
</script>

<style scoped>
.dashboard {
  background: transparent;
  min-height: calc(100vh - 130px);
}

.dash-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
}

.dash-title {
  font-size: 20px;
  font-weight: 700;
  color: #1f3b34;
  letter-spacing: 0.5px;
}

.dash-sub {
  font-size: 13px;
  color: #7d938c;
  margin-top: 4px;
}

.stat-row {
  margin-bottom: 16px;
}

.stat-card {
  position: relative;
  background: rgba(255, 255, 255, 0.30);
  border-radius: 14px;
  padding: 18px 20px;
  border: 1px solid rgba(255, 255, 255, 0.55);
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.9), 0 8px 24px rgba(23, 52, 46, 0.08);
  margin-bottom: 8px;
  transition: transform 0.25s ease, box-shadow 0.25s ease;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.95), 0 14px 36px rgba(23, 52, 46, 0.13);
}

.stat-label {
  font-size: 13px;
  color: #6f8680;
}

.stat-value {
  font-size: 30px;
  font-weight: 700;
  line-height: 1.4;
  color: #2e6b5f;
}

.stat-hint {
  font-size: 12px;
  color: #9db0aa;
}

.tone-primary .stat-value {
  color: #2e6b5f;
}
.tone-amber .stat-value {
  color: #e89b3c;
}
.tone-green .stat-value {
  color: #3f8f6f;
}
.tone-red .stat-value {
  color: #d9534f;
}

.panel {
  position: relative;
  background: rgba(255, 255, 255, 0.30);
  border-radius: 14px;
  border: 1px solid rgba(255, 255, 255, 0.55);
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.9), 0 8px 24px rgba(23, 52, 46, 0.08);
  padding: 18px 20px;
  margin-bottom: 16px;
  min-height: 120px;
}

.panel-title {
  font-size: 15px;
  font-weight: 600;
  color: #2f4a44;
  margin-bottom: 14px;
  padding-left: 10px;
  border-left: 3px solid #e89b3c;
  line-height: 1.2;
}

.empty-tip {
  color: #9db0aa;
  font-size: 13px;
  padding: 20px 0;
  text-align: center;
}

.class-item {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}

.class-name {
  width: 110px;
  font-size: 13px;
  color: #4a5568;
}

.class-bar-wrap {
  flex: 1;
  height: 10px;
  background: rgba(31, 59, 54, 0.10);
  border-radius: 6px;
  overflow: hidden;
}

.class-bar {
  height: 100%;
  background: linear-gradient(90deg, #2e6b5f, #5b9e8f);
  border-radius: 6px;
  transition: width 0.4s ease;
}

.class-count {
  width: 60px;
  text-align: right;
  font-size: 13px;
  color: #2e6b5f;
  font-weight: 600;
}

.dist-list {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.dist-item {
  display: flex;
  align-items: center;
}

.dist-dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  margin-right: 10px;
}

.dist-label {
  flex: 1;
  font-size: 13px;
  color: #4a5568;
}

.dist-value {
  font-size: 14px;
  font-weight: 600;
  color: #2e6b5f;
}

.mt16 {
  margin-top: 0;
}

.feed {
  max-height: 300px;
  overflow-y: auto;
}

.feed-item {
  display: flex;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px dashed rgba(31, 59, 54, 0.10);
}

.feed-item:last-child {
  border-bottom: none;
}

.feed-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  margin-right: 12px;
  flex-shrink: 0;
}

.dot-up {
  background: #2e6b5f;
}
.dot-down {
  background: #d9534f;
}
.dot-move {
  background: #e89b3c;
}

.feed-body {
  flex: 1;
  min-width: 0;
}

.feed-main {
  font-size: 13px;
  color: #3d4a45;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.feed-name {
  font-weight: 600;
  margin-right: 8px;
}

.feed-reason {
  color: #7d938c;
}

.feed-meta {
  font-size: 12px;
  color: #9db0aa;
  margin-top: 2px;
}

.feed-score {
  font-size: 15px;
  font-weight: 700;
  margin-left: 10px;
}

.score-up {
  color: #2e6b5f;
}
.score-down {
  color: #d9534f;
}
</style>
