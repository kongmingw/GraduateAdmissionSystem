<template>
  <div class="statistics-container">
    <h2>统计分析</h2>

    <!-- 数据卡片 -->
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="8">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-number">{{ stats.totalStudents }}</div>
            <div class="stat-label">考生总人数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-number" style="color: #67C23A">{{ stats.admittedCount }}</div>
            <div class="stat-label">录取人数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-number" style="color: #E6A23C">{{ stats.avgFinalScore }}</div>
            <div class="stat-label">录取平均分</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 分数段分布 -->
    <el-card style="margin-top: 20px">
      <template #header>
        <span>初试总分分数段分布</span>
      </template>
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="dist-item">
            <div class="dist-number">{{ stats.scoreDistribution?.below270 || 0 }}</div>
            <div class="dist-label">270分以下</div>
            <el-progress 
              :percentage="getPercentage(stats.scoreDistribution?.below270)" 
              :color="'#F56C6C'"
            />
          </div>
        </el-col>
        <el-col :span="8">
          <div class="dist-item">
            <div class="dist-number">{{ stats.scoreDistribution?.between270_300 || 0 }}</div>
            <div class="dist-label">270-300分</div>
            <el-progress 
              :percentage="getPercentage(stats.scoreDistribution?.between270_300)" 
              :color="'#E6A23C'"
            />
          </div>
        </el-col>
        <el-col :span="8">
          <div class="dist-item">
            <div class="dist-number">{{ stats.scoreDistribution?.above300 || 0 }}</div>
            <div class="dist-label">300分以上</div>
            <el-progress 
              :percentage="getPercentage(stats.scoreDistribution?.above300)" 
              :color="'#67C23A'"
            />
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 录取率 -->
    <el-card style="margin-top: 20px">
      <template #header>
        <span>录取率</span>
      </template>
      <div style="text-align: center; padding: 30px">
        <el-progress 
          type="circle" 
          :percentage="admissionRate" 
          :color="'#409EFF'"
          :width="200"
        />
        <p style="margin-top: 15px; color: #666; font-size: 16px">
          录取 {{ stats.admittedCount }} 人 / 报考 {{ stats.totalStudents }} 人
        </p>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getStatistics } from '../api/statistics'

const stats = ref({
  totalStudents: 0,
  admittedCount: 0,
  avgFinalScore: 0,
  scoreDistribution: {
    below270: 0,
    between270_300: 0,
    above300: 0
  }
})

// 录取率
const admissionRate = computed(() => {
  if (stats.value.totalStudents === 0) return 0
  return Math.round((stats.value.admittedCount / stats.value.totalStudents) * 100)
})

// 计算进度条百分比
const getPercentage = (value) => {
  if (!stats.value.totalStudents || stats.value.totalStudents === 0) return 0
  return Math.round((value / stats.value.totalStudents) * 100)
}

onMounted(async () => {
  try {
    const res = await getStatistics()
    stats.value = res.data.data
  } catch (error) {
    console.error('获取统计数据失败', error)
  }
})
</script>

<style scoped>
.statistics-container {
  padding: 20px;
}

.stat-card {
  text-align: center;
  padding: 20px 0;
}

.stat-number {
  font-size: 48px;
  font-weight: bold;
  color: #409EFF;
}

.stat-label {
  font-size: 16px;
  color: #999;
  margin-top: 10px;
}

.dist-item {
  text-align: center;
  padding: 10px;
}

.dist-number {
  font-size: 36px;
  font-weight: bold;
  color: #333;
}

.dist-label {
  font-size: 14px;
  color: #999;
  margin: 8px 0 15px 0;
}
</style>