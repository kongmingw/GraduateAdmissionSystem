<template>
  <div class="page-container">
    <el-card class="mb-20">
      <template #header>
        <div class="card-header">
          <span>成绩分析统计</span>
        </div>
      </template>

      <!-- 各科成绩概览 -->
      <h3 class="section-title">各科成绩概览</h3>
      <el-table :data="scoreOverviewTable" border stripe v-if="scoreOverview">
        <el-table-column prop="subject" label="科目" width="120" />
        <el-table-column prop="avg" label="平均分" width="100" />
        <el-table-column prop="max" label="最高分" width="100" />
        <el-table-column prop="min" label="最低分" width="100" />
      </el-table>

      <!-- 及格率统计 -->
      <h3 class="section-title">各科及格率（≥60分）</h3>
      <el-table :data="passRateData" border stripe v-if="passRateData.length">
        <el-table-column prop="subject" label="科目" width="120" />
        <el-table-column prop="passRate" label="及格率(%)" width="120" />
        <el-table-column prop="failRate" label="不及格率(%)" width="120" />
      </el-table>

      <!-- 初试总分分数段 -->
      <h3 class="section-title">初试总分分数段分布</h3>
      <el-table :data="firstDistribution" border stripe v-if="firstDistribution.length">
        <el-table-column prop="scoreRange" label="分数段" width="200" />
        <el-table-column prop="count" label="人数" width="100" />
      </el-table>

      <!-- 复试总分分数段 -->
      <h3 class="section-title">复试总分分数段分布</h3>
      <el-table :data="secondDistribution" border stripe v-if="secondDistribution.length">
        <el-table-column prop="scoreRange" label="分数段" width="200" />
        <el-table-column prop="count" label="人数" width="100" />
      </el-table>
    </el-card>

    <!-- 录取统计 -->
    <el-card>
      <template #header>
        <div class="card-header">
          <span>录取情况分析</span>
        </div>
      </template>

      <h3 class="section-title">各专业录取人数</h3>
      <el-table :data="countByMajor" border stripe v-if="countByMajor.length">
        <el-table-column prop="majorName" label="专业名称" width="200" />
        <el-table-column prop="count" label="录取人数" width="120" />
      </el-table>

      <h3 class="section-title">计划与实际招生对比</h3>
      <el-table :data="planVsActual" border stripe v-if="planVsActual.length">
        <el-table-column prop="majorName" label="专业名称" width="180" />
        <el-table-column prop="plannedInside" label="计划内" width="80" />
        <el-table-column prop="plannedOutside" label="计划外" width="80" />
        <el-table-column prop="totalPlanned" label="计划总数" width="100" />
        <el-table-column prop="actualCount" label="实际录取" width="100" />
      </el-table>

      <h3 class="section-title">录取生来源分布</h3>
      <el-table :data="countByOrigin" border stripe v-if="countByOrigin.length">
        <el-table-column prop="name" label="来源地" width="200" />
        <el-table-column prop="value" label="人数" width="120" />
      </el-table>

      <h3 class="section-title">录取生学历分布</h3>
      <el-table :data="countByEducation" border stripe v-if="countByEducation.length">
        <el-table-column prop="name" label="学历" width="200" />
        <el-table-column prop="value" label="人数" width="120" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'

const scoreOverview = ref(null)
const scoreOverviewTable = ref([])
const passRateData = ref([])
const firstDistribution = ref([])
const secondDistribution = ref([])
const countByMajor = ref([])
const countByOrigin = ref([])
const countByEducation = ref([])
const planVsActual = ref([])

async function loadStatistics() {
  try {
    const res = await axios.get('/api/statistics/all')
    const data = res.data.data

    // 成绩概览
    if (data.scoreOverview) {
      scoreOverview.value = data.scoreOverview
      scoreOverviewTable.value = [
        { subject: '政治', avg: data.scoreOverview.avgPolitics, max: data.scoreOverview.maxPolitics, min: data.scoreOverview.minPolitics },
        { subject: '外语', avg: data.scoreOverview.avgForeign, max: data.scoreOverview.maxForeign, min: data.scoreOverview.minForeign },
        { subject: '专业基础', avg: data.scoreOverview.avgMajorBasis, max: data.scoreOverview.maxMajorBasis, min: data.scoreOverview.minMajorBasis }
      ]
    }

    passRateData.value = data.passRate || []
    firstDistribution.value = data.firstScoreDistribution || []
    secondDistribution.value = data.secondScoreDistribution || []
    countByMajor.value = data.countByMajor || []
    countByOrigin.value = data.countByOrigin || []
    countByEducation.value = data.countByEducation || []
    planVsActual.value = data.planVsActual || []
  } catch (e) {
    ElMessage.error('加载统计数据失败')
  }
}

onMounted(() => {
  loadStatistics()
})
</script>

<style scoped>
.page-container {
  max-width: 1200px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.section-title {
  margin: 20px 0 10px;
  padding-bottom: 8px;
  border-bottom: 2px solid #409EFF;
  color: #303133;
}
.mb-20 {
  margin-bottom: 20px;
}
</style>