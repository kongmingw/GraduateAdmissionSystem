<template>
  <div class="page-container">
    <!-- 分数线管理 -->
    <el-card class="mb-20">
      <template #header>
        <div class="card-header">
          <span>分数线管理</span>
          <el-button type="primary" @click="showAddDialog">新增分数线</el-button>
        </div>
      </template>

      <el-table :data="tableData" border stripe @row-click="onRowClick" highlight-current-row>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="year" label="招生年份" width="100" />
        <el-table-column prop="politicsLine" label="政治单科线" width="110" />
        <el-table-column prop="foreignLangLine" label="外语单科线" width="110" />
        <el-table-column prop="majorBasisLine" label="专业基础单科线" width="130" />
        <el-table-column prop="totalFirstLine" label="初试总分线" width="110" />
        <el-table-column prop="admissionTotalLine" label="录取总分线" width="110" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="showEditDialog(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
            <el-button size="small" type="success" @click="runScreening(scope.row.year)">筛选</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div style="margin-top: 15px; padding: 12px; background: #ecf5ff; border-radius: 4px;">
        <strong>📋 筛选规则：</strong>
        考生需同时满足（政治≥单科线、外语≥单科线、专业基础≥单科线、初试总分≥初试总分线）四个条件，方可进入复试。
        复试后综合总分（初试+复试）≥ 录取总分线，方可被录取。
      </div>
    </el-card>

    <!-- 初试筛选结果 -->
    <el-card v-if="screeningResult">
      <template #header>
        <div class="card-header">
          <span>初试筛选结果（{{ screeningResult.year }}年）</span>
          <el-tag type="danger">单科线：{{ screeningResult.scoreLine.politicsLine }}/{{ screeningResult.scoreLine.foreignLangLine }}/{{ screeningResult.scoreLine.majorBasisLine }} | 总分线：{{ screeningResult.scoreLine.totalFirstLine }}</el-tag>
        </div>
      </template>

      <!-- 统计概览 -->
      <el-row :gutter="20" style="margin-bottom: 20px;">
        <el-col :span="8">
          <el-statistic title="考生总数" :value="screeningResult.qualifiedCount + screeningResult.disqualifiedCount" />
        </el-col>
        <el-col :span="8">
          <el-statistic title="合格进复试" :value="screeningResult.qualifiedCount">
            <template #suffix><span style="color:#67C23A;font-size:14px;">人</span></template>
          </el-statistic>
        </el-col>
        <el-col :span="8">
          <el-statistic title="不合格淘汰" :value="screeningResult.disqualifiedCount">
            <template #suffix><span style="color:#F56C6C;font-size:14px;">人</span></template>
          </el-statistic>
        </el-col>
      </el-row>

      <el-tabs v-model="activeTab">
        <!-- 合格列表 -->
        <el-tab-pane label="合格考生（进复试）" name="qualified">
          <el-table :data="screeningResult.qualified" border stripe>
            <el-table-column prop="examId" label="考号" width="120" />
            <el-table-column prop="politics" label="政治" width="80" />
            <el-table-column prop="foreignLang" label="外语" width="80" />
            <el-table-column prop="majorBasis" label="专业基础" width="100" />
            <el-table-column prop="totalFirst" label="初试总分" width="100">
              <template #default="scope">
                <span style="color:#67C23A;font-weight:bold;">{{ scope.row.totalFirst }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="80">
              <template #default>
                <el-tag type="success" size="small">合格</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <!-- 不合格列表 -->
        <el-tab-pane label="不合格考生（淘汰）" name="disqualified">
          <el-table :data="screeningResult.disqualified" border stripe>
            <el-table-column prop="examId" label="考号" width="120" />
            <el-table-column prop="politics" label="政治" width="80">
              <template #default="scope">
                <span :style="{color: scope.row.reasons.some(r=>r.includes('政治')) ? '#F56C6C' : ''}">{{ scope.row.politics }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="foreignLang" label="外语" width="80">
              <template #default="scope">
                <span :style="{color: scope.row.reasons.some(r=>r.includes('外语')) ? '#F56C6C' : ''}">{{ scope.row.foreignLang }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="majorBasis" label="专业基础" width="100">
              <template #default="scope">
                <span :style="{color: scope.row.reasons.some(r=>r.includes('专业基础')) ? '#F56C6C' : ''}">{{ scope.row.majorBasis }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="totalFirst" label="初试总分" width="100">
              <template #default="scope">
                <span :style="{color: scope.row.reasons.some(r=>r.includes('总分')) ? '#F56C6C' : ''}">{{ scope.row.totalFirst }}</span>
              </template>
            </el-table-column>
            <el-table-column label="淘汰原因" min-width="280">
              <template #default="scope">
                <el-tag v-for="r in scope.row.reasons" :key="r" type="danger" size="small" style="margin-right:5px;margin-bottom:2px;">{{ r }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 复试后综合筛选结果 -->
    <el-card v-if="admissionResult" class="mb-20">
      <template #header>
        <div class="card-header">
          <span>复试后综合筛选（初试合格考生 → 综合总分 vs 录取线{{ admissionResult.line }}）</span>
          <el-tag type="warning">录取线：{{ admissionResult.line }}</el-tag>
        </div>
      </template>

      <el-row :gutter="20" style="margin-bottom:20px;">
        <el-col :span="6">
          <el-statistic title="初试合格" :value="admissionResult.admitted.length + admissionResult.failed.length" suffix="人" />
        </el-col>
        <el-col :span="6">
          <el-statistic title="可录取" :value="admissionResult.admitted.length">
            <template #suffix><span style="color:#67C23A;font-size:14px;">人</span></template>
          </el-statistic>
        </el-col>
        <el-col :span="6">
          <el-statistic title="未达标/未复试" :value="admissionResult.failed.length">
            <template #suffix><span style="color:#F56C6C;font-size:14px;">人</span></template>
          </el-statistic>
        </el-col>
      </el-row>

      <el-tabs v-model="admissionTab">
        <el-tab-pane label="可录取考生" name="admitted">
          <el-table :data="admissionResult.admitted" border stripe>
            <el-table-column prop="examId" label="考号" width="120" />
            <el-table-column prop="name" label="姓名" width="80" />
            <el-table-column prop="totalFirst" label="初试总分" width="100" />
            <el-table-column prop="totalSecond" label="复试总分" width="100" />
            <el-table-column prop="totalScore" label="综合总分" width="100">
              <template #default="scope">
                <span style="font-weight:bold;color:#67C23A;">{{ scope.row.totalScore }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="80">
              <template #default>
                <el-tag type="success" size="small">可录取</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="未达标/未复试" name="failed">
          <el-table :data="admissionResult.failed" border stripe>
            <el-table-column prop="examId" label="考号" width="120" />
            <el-table-column prop="name" label="姓名" width="80" />
            <el-table-column prop="totalFirst" label="初试总分" width="100" />
            <el-table-column prop="totalSecond" label="复试总分" width="100" />
            <el-table-column prop="totalScore" label="综合总分" width="100">
              <template #default="scope">
                <span :style="{fontWeight:'bold',color:scope.row.status==='未复试'?'#909399':'#F56C6C'}">{{ scope.row.totalScore }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="gap" label="差额" width="100">
              <template #default="scope">
                <span style="color:#F56C6C;" v-if="scope.row.gap">差{{ scope.row.gap }}</span>
                <el-tag v-else type="info" size="small">未复试</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 对话框 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="480px">
      <el-form :model="form" label-width="130px">
        <el-form-item label="招生年份">
          <el-input v-model="form.year" placeholder="如：2026" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="政治单科线">
          <el-input-number v-model="form.politicsLine" :min="0" :max="100" :precision="1" />
        </el-form-item>
        <el-form-item label="外语单科线">
          <el-input-number v-model="form.foreignLangLine" :min="0" :max="100" :precision="1" />
        </el-form-item>
        <el-form-item label="专业基础单科线">
          <el-input-number v-model="form.majorBasisLine" :min="0" :max="100" :precision="1" />
        </el-form-item>
        <el-form-item label="初试总分线">
          <el-input-number v-model="form.totalFirstLine" :min="0" :max="300" :precision="1" />
        </el-form-item>
        <el-form-item label="录取总分线">
          <el-input-number v-model="form.admissionTotalLine" :min="0" :max="600" :precision="1" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { getScoreLineList, addScoreLine, updateScoreLine, deleteScoreLine } from '../api/scoreline'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'

const tableData = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const activeTab = ref('qualified')
const admissionTab = ref('admitted')
const screeningResult = ref(null)
const admissionResult = ref(null)
const dialogTitle = computed(() => isEdit.value ? '编辑分数线' : '新增分数线')

const form = reactive({
  id: null,
  year: '',
  politicsLine: 60,
  foreignLangLine: 60,
  majorBasisLine: 60,
  totalFirstLine: 180,
  admissionTotalLine: 300
})

async function loadData() {
  try {
    const res = await getScoreLineList()
    tableData.value = res.data || []
  } catch (e) {
    ElMessage.error('加载分数线数据失败')
  }
}

// 执行筛选
async function runScreening(year) {
  try {
    const res = await axios.get('/api/statistics/screening', { params: { year } })
    if (res.data.code === 200) {
      screeningResult.value = res.data.data
      // 继续做复试后综合筛选
      await runAdmissionScreening(res.data.data)
    } else {
      ElMessage.error(res.data.message || '筛选失败')
    }
  } catch (e) {
    ElMessage.error('执行筛选失败')
  }
}

// 复试后综合筛选
async function runAdmissionScreening(firstResult) {
  const line = firstResult.scoreLine
  const qualified = firstResult.qualified
  try {
    const secondRes = await axios.get('/api/second-test/list')
    const secondScores = {}
    ;(secondRes.data.data || []).forEach(s => { secondScores[s.examId] = s.totalSecond })
    // 获取考生姓名
    const candidateRes = await axios.get('/api/candidate/list')
    const nameMap = {}
    ;(candidateRes.data.data || []).forEach(c => { nameMap[c.examId] = c.name })

    const admitted = []   // 综合达线可录取
    const failed = []     // 综合不够线
    qualified.forEach(q => {
      const totalFirst = q.totalFirst
      const totalSecond = secondScores[q.examId]
      const entry = {
        examId: q.examId,
        name: nameMap[q.examId] || '',
        totalFirst: totalFirst
      }
      if (totalSecond == null) {
        entry.totalSecond = '--'
        entry.totalScore = '--'
        entry.status = '未复试'
        failed.push(entry)
      } else {
        const total = totalFirst + totalSecond
        entry.totalSecond = totalSecond
        entry.totalScore = total
        if (total >= line.admissionTotalLine) {
          entry.status = '可录取'
          admitted.push(entry)
        } else {
          entry.status = '不达标'
          entry.gap = (line.admissionTotalLine - total).toFixed(1)
          failed.push(entry)
        }
      }
    })
    admissionResult.value = {
      line: line.admissionTotalLine,
      admitted,
      failed
    }
  } catch (e) {
    console.error('复试筛选失败', e)
  }
}

// 点击行也触发筛选
function onRowClick(row) {
  runScreening(row.year)
}

function showAddDialog() {
  isEdit.value = false
  form.id = null
  form.year = ''
  form.politicsLine = 60
  form.foreignLangLine = 60
  form.majorBasisLine = 60
  form.totalFirstLine = 180
  form.admissionTotalLine = 300
  dialogVisible.value = true
}

function showEditDialog(row) {
  isEdit.value = true
  Object.assign(form, row)
  dialogVisible.value = true
}

async function handleSave() {
  if (!form.year) {
    ElMessage.warning('请填写招生年份')
    return
  }
  try {
    if (isEdit.value) {
      await updateScoreLine(form)
      ElMessage.success('更新成功')
    } else {
      await addScoreLine(form)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

async function handleDelete(id) {
  try {
    await ElMessageBox.confirm('确认删除该分数线？', '提示', { type: 'warning' })
    await deleteScoreLine(id)
    ElMessage.success('删除成功')
    loadData()
  } catch (e) {
    // 用户取消
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.page-container {
  max-width: 1200px;
}
.mb-20 {
  margin-bottom: 20px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
