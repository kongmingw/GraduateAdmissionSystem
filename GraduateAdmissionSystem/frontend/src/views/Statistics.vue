<template>
  <div class="page-container">

    <!-- 顶部概览卡片 -->
    <el-row :gutter="16" class="mb-20">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card stat-blue">
          <div class="stat-num">{{ overview.candidates }}</div>
          <div class="stat-label">考生总数</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card stat-green">
          <div class="stat-num">{{ overview.admitted }}</div>
          <div class="stat-label">已录取</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card stat-orange">
          <div class="stat-num">{{ overview.majors }}</div>
          <div class="stat-label">招生专业</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card stat-purple">
          <div class="stat-num">{{ overview.avgTotalFirst }}</div>
          <div class="stat-label">初试均分</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 成绩分析 -->
    <el-card class="mb-20">
      <template #header><span class="card-title">📊 成绩分析</span></template>

      <!-- 及格率进度条 -->
      <h3 class="section-title">各科及格率</h3>
      <el-row :gutter="20">
        <el-col :span="8" v-for="item in passRateData" :key="item.subject">
          <div class="pass-rate-item">
            <div class="pass-label">{{ item.subject }}</div>
            <el-progress :percentage="Number(item.passRate)" :color="passColor(item.passRate)" :stroke-width="18" />
            <div class="pass-text">
              <span class="rate-high">{{ item.passRate }}% 及格</span>
              <span class="rate-low">{{ item.failRate }}% 不及格</span>
            </div>
          </div>
        </el-col>
      </el-row>

      <!-- 成绩概览 -->
      <h3 class="section-title">各科成绩概览</h3>
      <el-row :gutter="16">
        <el-col :span="8" v-for="sub in scoreOverviewTable" :key="sub.subject">
          <div class="score-card">
            <div class="score-subject">{{ sub.subject }}</div>
            <el-row :gutter="8">
              <el-col :span="8"><div class="score-item"><span class="val-high">{{ sub.max }}</span><span class="lbl">最高</span></div></el-col>
              <el-col :span="8"><div class="score-item"><span class="val-mid">{{ sub.avg }}</span><span class="lbl">平均</span></div></el-col>
              <el-col :span="8"><div class="score-item"><span class="val-low">{{ sub.min }}</span><span class="lbl">最低</span></div></el-col>
            </el-row>
          </div>
        </el-col>
      </el-row>

      <!-- 分数段分布 -->
      <h3 class="section-title">初试分数段分布</h3>
      <div class="bar-chart">
        <div class="bar-row" v-for="item in firstDistribution" :key="item.scoreRange">
          <span class="bar-label">{{ item.scoreRange }}</span>
          <div class="bar-track">
            <div class="bar-fill" :style="{ width: barWidth(item.count, firstMax) }"></div>
          </div>
          <span class="bar-count">{{ item.count }}人</span>
        </div>
      </div>

      <h3 class="section-title">复试分数段分布</h3>
      <div class="bar-chart">
        <div class="bar-row" v-for="item in secondDistribution" :key="item.scoreRange">
          <span class="bar-label">{{ item.scoreRange }}</span>
          <div class="bar-track">
            <div class="bar-fill bar-fill-blue" :style="{ width: barWidth(item.count, secondMax) }"></div>
          </div>
          <span class="bar-count">{{ item.count }}人</span>
        </div>
      </div>
    </el-card>

    <!-- 录取分析 -->
    <el-card>
      <template #header><span class="card-title">🎓 录取分析</span></template>

      <!-- 计划 vs 实际 -->
      <h3 class="section-title">计划与实际招生对比</h3>
      <div class="bar-chart">
        <div class="bar-row" v-for="item in planVsActual" :key="item.majorName">
          <span class="bar-label">{{ item.majorName }}</span>
          <div class="bar-track multi">
            <div class="bar-fill" :style="{ width: barWidth(item.totalPlanned, planMax) }"></div>
            <div class="bar-fill bar-fill-blue bar-overlap" :style="{ width: barWidth(item.actualCount, planMax) }"></div>
          </div>
          <span class="bar-count">{{ item.actualCount }}<span class="bar-total">/{{ item.totalPlanned }}</span></span>
        </div>
      </div>
      <div class="legend">
        <span><i class="dot dot-green"></i>实际录取</span>
        <span><i class="dot dot-gray"></i>计划招生</span>
      </div>

      <!-- 来源 + 学历 饼图 -->
      <el-row :gutter="20">
        <el-col :span="12">
          <h3 class="section-title">录取生来源分布</h3>
          <div class="pie-wrap">
            <div class="pie" :style="{ background: originPie }"></div>
            <div class="pie-legend">
              <div class="pie-legend-item" v-for="(item, idx) in countByOrigin" :key="item.name">
                <i class="pie-dot" :style="{ background: pieColors[idx % pieColors.length] }"></i>
                {{ item.name || '未知' }} {{ item.value }}人
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="12">
          <h3 class="section-title">录取生学历分布</h3>
          <div class="pie-wrap">
            <div class="pie" :style="{ background: eduPie }"></div>
            <div class="pie-legend">
              <div class="pie-legend-item" v-for="(item, idx) in countByEducation" :key="item.name">
                <i class="pie-dot" :style="{ background: pieColors[idx % pieColors.length] }"></i>
                {{ item.name || '未知' }} {{ item.value }}人
              </div>
            </div>
          </div>
        </el-col>
      </el-row>

      <!-- 及格率饼图 -->
      <h3 class="section-title">各科及格率概览</h3>
      <el-row :gutter="20">
        <el-col :span="8" v-for="(item, idx) in passPieData" :key="item.subject">
          <div class="pie-wrap small">
            <div class="pie pie-sm" :style="{ background: passPies[idx] }"></div>
            <div class="pie-center">{{ item.subject }}<br>{{ item.passRate }}%</div>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { getAllStatistics, getScreening } from '../api/statistics'
import { getCandidateList } from '../api/candidate'
import { getAdmissionList } from '../api/admission'
import { getMajorList } from '../api/major'
import { ElMessage } from 'element-plus'

const scoreOverviewTable = ref([])
const passRateData = ref([])
const firstDistribution = ref([])
const secondDistribution = ref([])
const countByMajor = ref([])
const countByOrigin = ref([])
const countByEducation = ref([])
const planVsActual = ref([])

const overview = reactive({
  candidates: 0,
  admitted: 0,
  majors: 0,
  avgTotalFirst: 0
})

const currentYear = new Date().getFullYear().toString()

// 计算各图表的最大值
const firstMax = computed(() => maxCount(firstDistribution.value))
const secondMax = computed(() => maxCount(secondDistribution.value))
const planMax = computed(() => Math.max(maxPlan(planVsActual.value), 1))
const pieColors = ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#9B59B6', '#36CFC9', '#FF85C0', '#597EF7']

function makePie(data, key) {
  if (!data.length) return 'conic-gradient(#eee 0deg 360deg)'
  const total = data.reduce((s, i) => s + (i[key] || 0), 0)
  if (total === 0) return 'conic-gradient(#eee 0deg 360deg)'
  let deg = 0
  const parts = data.map((item, idx) => {
    const pct = (item[key] || 0) / total * 360
    const start = deg
    deg += pct
    const c = pieColors[idx % pieColors.length]
    return `${c} ${start}deg ${deg}deg`
  })
  return `conic-gradient(${parts.join(',')})`
}

const originPie = computed(() => makePie(countByOrigin.value, 'value'))
const eduPie = computed(() => makePie(countByEducation.value, 'value'))
const passPieData = computed(() => passRateData.value.map(p => ({
  subject: p.subject,
  passRate: Number(p.passRate || 0).toFixed(1)
})))
const passPies = computed(() => passPieData.value.map(p => {
  const rate = Number(p.passRate)
  const color = rate >= 60 ? '#67C23A' : '#F56C6C'
  return `conic-gradient(${color} 0deg ${rate * 3.6}deg, #f0f0f0 ${rate * 3.6}deg 360deg)`
}))

function maxCount(arr) { return Math.max(...arr.map(i => i.count || 0), 1) }
function maxVal(arr) { return Math.max(...arr.map(i => i.value || 0), 1) }
function maxPlan(arr) { return Math.max(...arr.map(i => i.totalPlanned || 0), 1) }
function barWidth(val, max) { return (val / max * 100) + '%' }
function passColor(rate) { return Number(rate) >= 60 ? '#67C23A' : '#F56C6C' }

async function loadStatistics() {
  try {
    const res = await getAllStatistics()
    const data = res.data

    if (data.scoreOverview) {
      const so = data.scoreOverview
      scoreOverviewTable.value = [
        { subject: '政治', avg: so.avgPolitics, max: so.maxPolitics, min: so.minPolitics },
        { subject: '外语', avg: so.avgForeign, max: so.maxForeign, min: so.minForeign },
        { subject: '专业基础', avg: so.avgMajorBasis, max: so.maxMajorBasis, min: so.minMajorBasis }
      ]
    }
    passRateData.value = data.passRate || []
    firstDistribution.value = data.firstScoreDistribution || []
    secondDistribution.value = data.secondScoreDistribution || []
    countByMajor.value = data.countByMajor || []
    countByOrigin.value = data.countByOrigin || []
    countByEducation.value = data.countByEducation || []
    planVsActual.value = data.planVsActual || []

    // 并行获取概览数据
    const [candidatesRes, admissionRes, majorRes] = await Promise.all([
      getCandidateList(),
      getAdmissionList(),
      getMajorList()
    ])
    overview.candidates = (candidatesRes.data || []).length
    overview.admitted = (admissionRes.data || []).length
    overview.majors = (majorRes.data || []).length

    // 初试均分直接取三科均分的平均值
    if (data.scoreOverview) {
      const so = data.scoreOverview
      overview.avgTotalFirst = Math.round(
        (Number(so.avgPolitics || 0) + Number(so.avgForeign || 0) + Number(so.avgMajorBasis || 0)) / 3 * 10
      ) / 10
    }
  } catch (e) {
    ElMessage.error('加载统计数据失败：' + (e.message || '网络异常'))
  }
}

onMounted(() => { loadStatistics() })
</script>

<style scoped>
.page-container { max-width: 1200px; }
.mb-20 { margin-bottom: 20px; }
.card-title { font-size: 16px; font-weight: bold; }

/* 顶部统计卡片 */
.stat-card { text-align: center; padding: 10px 0; }
.stat-num { font-size: 32px; font-weight: bold; }
.stat-label { font-size: 13px; color: #909399; margin-top: 4px; }
.stat-blue .stat-num { color: #409EFF; }
.stat-green .stat-num { color: #67C23A; }
.stat-orange .stat-num { color: #E6A23C; }
.stat-purple .stat-num { color: #9B59B6; }

.section-title {
  margin: 20px 0 14px;
  padding-bottom: 6px;
  border-bottom: 2px solid #EBEEF5;
  font-size: 15px;
  color: #303133;
}

/* 及格率 */
.pass-rate-item { padding: 10px 0; }
.pass-label { font-weight: bold; margin-bottom: 8px; font-size: 14px; }
.pass-text { margin-top: 4px; font-size: 12px; }
.rate-high { color: #67C23A; margin-right: 12px; }
.rate-low { color: #F56C6C; }

/* 成绩卡片 */
.score-card {
  background: #f5f7fa; border-radius: 8px; padding: 16px;
  text-align: center; margin-bottom: 10px;
}
.score-subject { font-weight: bold; font-size: 15px; margin-bottom: 10px; }
.score-item { display: flex; flex-direction: column; }
.score-item span { font-weight: bold; }
.val-high { color: #67C23A; font-size: 20px; }
.val-mid { color: #409EFF; font-size: 20px; }
.val-low { color: #F56C6C; font-size: 20px; }
.lbl { color: #909399; font-size: 11px; margin-top: 2px; }

/* 柱状图 */
.bar-chart { padding: 4px 0; }
.bar-row { display: flex; align-items: center; margin: 8px 0; }
.bar-label { width: 110px; font-size: 13px; text-align: right; padding-right: 10px; flex-shrink: 0; }
.bar-track { flex: 1; height: 22px; background: #f0f2f5; border-radius: 4px; overflow: hidden; position: relative; min-width: 80px; }
.bar-track.multi { position: relative; height: 28px; }
.bar-fill { height: 100%; background: #409EFF; border-radius: 4px; min-width: 2px; transition: width 0.5s; }
.bar-fill-blue { background: #67C23A; }
.bar-fill-green { background: #67C23A; }
.bar-fill-purple { background: #9B59B6; }
.bar-overlap { position: absolute; top: 0; left: 0; height: 50%; top: 25%; border-radius: 3px; }
.bar-count { width: 60px; font-size: 13px; font-weight: bold; padding-left: 8px; flex-shrink: 0; }
.bar-total { color: #909399; font-weight: normal; }

.legend { margin-top: 8px; font-size: 12px; }
.legend span { margin-right: 16px; }
.dot { display: inline-block; width: 10px; height: 10px; border-radius: 2px; margin-right: 4px; vertical-align: middle; }
.dot-green { background: #67C23A; }
.dot-gray { background: #B0B0B0; }

/* 饼图 */
.pie-wrap { display: flex; align-items: center; gap: 20px; padding: 10px 0; }
.pie-wrap.small { flex-direction: column; text-align: center; }
.pie {
  width: 160px; height: 160px; border-radius: 50%; flex-shrink: 0;
  position: relative;
}
.pie::after {
  content: ''; position: absolute; width: 70px; height: 70px;
  background: #fff; border-radius: 50%; top: 50%; left: 50%;
  transform: translate(-50%, -50%);
}
.pie-sm { width: 130px; height: 130px; }
.pie-sm::after { width: 50px; height: 50px; }
.pie-center {
  font-size: 14px; font-weight: bold; color: #303133; line-height: 1.5;
  margin-top: 8px;
}
.pie-legend { flex: 1; }
.pie-legend-item { font-size: 13px; margin: 4px 0; color: #606266; }
.pie-dot { display: inline-block; width: 10px; height: 10px; border-radius: 50%; margin-right: 6px; vertical-align: middle; }
</style>
