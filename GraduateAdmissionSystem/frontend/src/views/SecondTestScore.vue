<template>
  <div class="page-container">
    <el-card class="mb-20">
      <template #header>
        <div class="card-header">
          <span>复试成绩管理</span>
          <el-button type="primary" @click="showDialog()">录入/编辑</el-button>
        </div>
      </template>

      <el-table :data="tableData" border stripe>
        <el-table-column prop="examId" label="考号" width="120" />
        <el-table-column prop="majorTest" label="复试专业" width="100" />
        <el-table-column prop="interview" label="面试成绩" width="100" />
        <el-table-column prop="computerTest" label="上机成绩" width="100" />
        <el-table-column prop="totalSecond" label="复试总分" width="100">
          <template #default="scope">
            <span style="font-weight:bold;color:#409EFF;">{{ scope.row.totalSecond }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="showDialog(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row.examId)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 待录入复试成绩 -->
    <el-card v-if="pendingList.length > 0">
      <template #header>
        <span style="color:#E6A23C;">⚠ 以下初试合格考生尚无复试成绩（{{ pendingList.length }}人）</span>
      </template>
      <el-table :data="pendingList" border stripe>
        <el-table-column prop="examId" label="考号" width="120" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="targetMajor" label="报考专业" width="120" />
        <el-table-column label="操作" width="120">
          <template #default="scope">
            <el-button size="small" type="primary" @click="showDialog(scope.row)">录入</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 对话框 -->
    <el-dialog title="复试成绩" v-model="dialogVisible" width="400px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="考生">
          <el-select v-model="form.examId" filterable placeholder="搜索考号或姓名"
            :disabled="!!editExamId" style="width:100%"
            @change="onCandidateChange">
            <el-option v-for="c in allCandidates" :key="c.examId"
              :label="c.examId + ' - ' + c.name" :value="c.examId" />
          </el-select>
        </el-form-item>
        <el-form-item label="复试专业">
          <el-input-number v-model="form.majorTest" :min="0" :max="100" :precision="1" />
        </el-form-item>
        <el-form-item label="面试成绩">
          <el-input-number v-model="form.interview" :min="0" :max="100" :precision="1" />
        </el-form-item>
        <el-form-item label="上机成绩">
          <el-input-number v-model="form.computerTest" :min="0" :max="100" :precision="1" />
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
import { ref, reactive, onMounted } from 'vue'
import { getSecondTestList, getSecondTestByExamId, addSecondTest, updateSecondTest, deleteSecondTest } from '../api/secondTest'
import { getCandidateList } from '../api/candidate'
import { getScreening } from '../api/statistics'
import { ElMessage, ElMessageBox } from 'element-plus'

const tableData = ref([])
const pendingList = ref([])
const allCandidates = ref([])
const dialogVisible = ref(false)
const editExamId = ref('')
const currentYear = new Date().getFullYear().toString()

const form = reactive({
  examId: '',
  majorTest: 0,
  interview: 0,
  computerTest: 0
})

async function loadData() {
  try {
    const [scoreRes, candidateRes, screeningRes] = await Promise.all([
      getSecondTestList(),
      getCandidateList(),
      getScreening(currentYear)
    ])
    tableData.value = scoreRes.data || []
    const allCand = candidateRes.data || []
    const qualifiedIds = new Set((screeningRes.data?.qualified || []).map(q => q.examId))
    allCandidates.value = allCand.filter(c => qualifiedIds.has(c.examId))
    const scoredIds = new Set(tableData.value.map(s => s.examId))
    pendingList.value = allCandidates.value.filter(c => !scoredIds.has(c.examId))
  } catch (e) {
    ElMessage.error('加载数据失败：' + (e.message || '网络异常'))
  }
}

async function onCandidateChange(examId) {
  if (!examId) return
  try {
    const res = await getSecondTestByExamId(examId)
    if (res.data) {
      form.majorTest = res.data.majorTest || 0
      form.interview = res.data.interview || 0
      form.computerTest = res.data.computerTest || 0
    } else {
      form.majorTest = 0
      form.interview = 0
      form.computerTest = 0
    }
  } catch (e) {
    form.majorTest = 0
    form.interview = 0
    form.computerTest = 0
  }
}

function showDialog(row) {
  if (row) {
    if (row.majorTest != null) {
      editExamId.value = row.examId
      form.examId = row.examId
      form.majorTest = row.majorTest || 0
      form.interview = row.interview || 0
      form.computerTest = row.computerTest || 0
    } else {
      editExamId.value = ''
      form.examId = row.examId
      form.majorTest = 0
      form.interview = 0
      form.computerTest = 0
    }
  } else {
    editExamId.value = ''
    form.examId = ''
    form.majorTest = 0
    form.interview = 0
    form.computerTest = 0
  }
  dialogVisible.value = true
}

async function handleSave() {
  if (!form.examId) { ElMessage.warning('请选择考生'); return }
  try {
    let exists = false
    try {
      const existing = await getSecondTestByExamId(form.examId)
      exists = !!existing.data
    } catch (e) { /* 查不到说明无成绩，走新增 */ }
    if (exists) {
      await updateSecondTest(form)
      ElMessage.success('更新成功')
    } else {
      await addSecondTest(form)
      ElMessage.success('录入成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (e) {
    ElMessage.error('操作失败：' + (e.message || '请检查网络或数据'))
  }
}

async function handleDelete(examId) {
  try {
    await ElMessageBox.confirm('确认删除该复试成绩？', '提示', { type: 'warning' })
    await deleteSecondTest(examId)
    ElMessage.success('删除成功')
    loadData()
  } catch (e) {
    if (e !== 'cancel' && e?.message) {
      ElMessage.error('删除失败：该考生可能有录取记录')
    }
  }
}

onMounted(() => { loadData() })
</script>

<style scoped>
.page-container { max-width: 1000px; }
.mb-20 { margin-bottom: 20px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
</style>
