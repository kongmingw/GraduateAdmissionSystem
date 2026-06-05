<template>
  <div class="page-container">
    <!-- 已录取名单 -->
    <el-card class="mb-20">
      <template #header>
        <div class="card-header">
          <span>录取名单（{{ tableData.length }}人）</span>
        </div>
      </template>
      <el-table :data="tableData" border stripe>
        <el-table-column prop="examId" label="考号" width="120" />
        <el-table-column prop="admittedMajor" label="录取专业" width="120" />
        <el-table-column prop="finalFirstScore" label="初试成绩" width="100" />
        <el-table-column prop="finalSecondScore" label="复试成绩" width="100" />
        <el-table-column prop="totalScore" label="综合总分" width="100" />
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="scope">
            <el-button size="small" type="danger" @click="handleCancel(scope.row.examId)">取消录取</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 可录取（复试已完成+综合达线） -->
    <el-card class="mb-20">
      <template #header>
        <div class="card-header">
          <span>可录取考生（复试已完成，综合≥{{ admissionLine }}）</span>
        </div>
      </template>
      <el-table :data="qualifiedList" border stripe v-loading="loadingCandidates">
        <el-table-column prop="examId" label="考号" width="120" />
        <el-table-column prop="name" label="姓名" width="80" />
        <el-table-column prop="targetMajor" label="报考专业" width="120" />
        <el-table-column prop="finalFirstScore" label="初试总分" width="100" />
        <el-table-column prop="finalSecondScore" label="复试总分" width="100" />
        <el-table-column prop="totalScore" label="综合总分" width="100">
          <template #default="scope">
            <span style="font-weight:bold;color:#67C23A;">{{ scope.row.totalScore }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="scope">
            <el-button size="small" type="success" @click="quickAdmit(scope.row)">录取</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div v-if="qualifiedList.length === 0 && !loadingCandidates" style="text-align:center;color:#909399;padding:20px;">
        暂无符合录取条件的考生
      </div>
    </el-card>

    <!-- 复试未达标（综合不够线） -->
    <el-card>
      <template #header>
        <span style="color:#E6A23C;">⚠ 以下考生复试已完成，但综合总分未达录取线（{{ admissionLine }}）</span>
      </template>
      <el-table :data="unqualifiedList" border stripe v-loading="loadingCandidates">
        <el-table-column prop="examId" label="考号" width="120" />
        <el-table-column prop="name" label="姓名" width="80" />
        <el-table-column prop="targetMajor" label="报考专业" width="120" />
        <el-table-column prop="finalFirstScore" label="初试总分" width="100" />
        <el-table-column prop="finalSecondScore" label="复试总分" width="100" />
        <el-table-column prop="totalScore" label="综合总分" width="100">
          <template #default="scope">
            <span style="font-weight:bold;color:#F56C6C;">{{ scope.row.totalScore }}</span>
          </template>
        </el-table-column>
        <el-table-column label="差额" width="100">
          <template #default="scope">
            <span style="color:#F56C6C;">差{{ (admissionLine - scope.row.totalScore).toFixed(1) }}</span>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 录取对话框 -->
    <el-dialog title="录取考生" v-model="dialogVisible" width="480px">
      <el-form :model="form" label-width="120px">
        <el-form-item label="考号">
          <el-input v-model="form.examId" disabled />
        </el-form-item>
        <el-form-item label="录取专业">
          <el-select v-model="form.admittedMajor" placeholder="请选择录取专业">
            <el-option
              v-for="m in majorList"
              :key="m.majorCode"
              :label="m.majorName + '(' + m.majorCode + ')'"
              :value="m.majorCode"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAdmit">确认录取</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getAdmissionList, admitCandidate, cancelAdmission } from '../api/admission'
import { getCandidateList } from '../api/candidate'
import { getMajorList } from '../api/major'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'

const tableData = ref([])
const qualifiedList = ref([])
const unqualifiedList = ref([])
const majorList = ref([])
const loadingCandidates = ref(false)
const dialogVisible = ref(false)
const admissionLine = ref(450)

const form = reactive({
  examId: '',
  admittedMajor: ''
})

async function loadAdmissions() {
  try {
    const res = await getAdmissionList()
    tableData.value = res.data || []
  } catch (e) {
    ElMessage.error('加载录取名单失败')
  }
}

async function loadCandidates() {
  loadingCandidates.value = true
  try {
    const [candidateRes, admissionRes, firstRes, secondRes, lineRes] = await Promise.all([
      getCandidateList(),
      getAdmissionList(),
      axios.get('/api/first-test/list'),
      axios.get('/api/second-test/list'),
      axios.get('/api/score-line/year/2026')
    ])
    const allCandidates = candidateRes.data || []
    const admittedIds = new Set((admissionRes.data || []).map(a => a.examId))
    const firstMap = {}
    ;(firstRes.data.data || []).forEach(f => { firstMap[f.examId] = f.totalFirst })
    const secondMap = {}
    ;(secondRes.data.data || []).forEach(s => { secondMap[s.examId] = s.totalSecond })
    // 录取线
    if (lineRes.data.data) {
      admissionLine.value = lineRes.data.data.admissionTotalLine || 450
    }

    // 筛选：有初试+有复试+未被录取
    const withBoth = allCandidates.filter(c =>
      !admittedIds.has(c.examId) && firstMap[c.examId] != null && secondMap[c.examId] != null
    ).map(c => ({
      ...c,
      finalFirstScore: firstMap[c.examId],
      finalSecondScore: secondMap[c.examId],
      totalScore: firstMap[c.examId] + secondMap[c.examId]
    }))

    // 分组
    qualifiedList.value = withBoth.filter(c => c.totalScore >= admissionLine.value)
    unqualifiedList.value = withBoth.filter(c => c.totalScore < admissionLine.value)
  } catch (e) {
    ElMessage.error('加载数据失败')
  } finally {
    loadingCandidates.value = false
  }
}

async function loadMajors() {
  try {
    const res = await getMajorList()
    majorList.value = res.data || []
  } catch (e) { console.error('加载专业列表失败') }
}

function showAdmitDialog() {
  form.examId = ''
  form.admittedMajor = ''
  dialogVisible.value = true
}

function quickAdmit(row) {
  form.examId = row.examId
  form.admittedMajor = row.targetMajor || ''
  dialogVisible.value = true
}

async function handleAdmit() {
  if (!form.examId || !form.admittedMajor) {
    ElMessage.warning('请填写考号和录取专业')
    return
  }
  try {
    await admitCandidate({ examId: form.examId, admittedMajor: form.admittedMajor })
    ElMessage.success('录取成功')
    dialogVisible.value = false
    loadAdmissions()
    loadCandidates()
  } catch (e) {
    ElMessage.error(e.message || '录取失败')
  }
}

async function handleCancel(examId) {
  try {
    await ElMessageBox.confirm('确认取消该考生的录取资格？', '提示', { type: 'warning' })
    await cancelAdmission(examId)
    ElMessage.success('取消录取成功')
    loadAdmissions()
    loadCandidates()
  } catch (e) {}
}

onMounted(() => {
  loadAdmissions()
  loadCandidates()
  loadMajors()
})
</script>

<style scoped>
.page-container { max-width: 1200px; }
.mb-20 { margin-bottom: 20px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
</style>
