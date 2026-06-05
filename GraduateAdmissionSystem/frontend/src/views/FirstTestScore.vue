<template>
  <div class="page-container">
    <el-card class="mb-20">
      <template #header>
        <div class="card-header">
          <span>初试成绩管理</span>
          <el-button type="primary" @click="showDialog()">录入/编辑</el-button>
        </div>
      </template>

      <el-table :data="tableData" border stripe>
        <el-table-column prop="examId" label="考号" width="120" />
        <el-table-column prop="politics" label="政治" width="80" />
        <el-table-column prop="foreignLang" label="外语" width="80" />
        <el-table-column prop="majorBasis" label="专业基础" width="100" />
        <el-table-column prop="totalFirst" label="初试总分" width="100">
          <template #default="scope">
            <span style="font-weight:bold;color:#409EFF;">{{ scope.row.totalFirst }}</span>
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

    <!-- 待录入 -->
    <el-card v-if="pendingList.length > 0">
      <template #header>
        <span style="color:#E6A23C;">⚠ 以下考生尚无初试成绩（{{ pendingList.length }}人）</span>
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
    <el-dialog title="初试成绩" v-model="dialogVisible" width="400px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="考生">
          <el-select v-model="form.examId" filterable placeholder="搜索考号或姓名"
            :disabled="!!editExamId" style="width:100%"
            @change="onCandidateChange">
            <el-option v-for="c in allCandidates" :key="c.examId"
              :label="c.examId + ' - ' + c.name" :value="c.examId" />
          </el-select>
        </el-form-item>
        <el-form-item label="政治成绩">
          <el-input-number v-model="form.politics" :min="0" :max="100" :precision="1" />
        </el-form-item>
        <el-form-item label="外语成绩">
          <el-input-number v-model="form.foreignLang" :min="0" :max="100" :precision="1" />
        </el-form-item>
        <el-form-item label="专业基础">
          <el-input-number v-model="form.majorBasis" :min="0" :max="100" :precision="1" />
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
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'

const tableData = ref([])
const pendingList = ref([])
const allCandidates = ref([])
const dialogVisible = ref(false)
const editExamId = ref('')

const form = reactive({
  examId: '',
  politics: 0,
  foreignLang: 0,
  majorBasis: 0
})

async function loadData() {
  try {
    const [scoreRes, candidateRes] = await Promise.all([
      axios.get('/api/first-test/list'),
      axios.get('/api/candidate/list')
    ])
    tableData.value = scoreRes.data.data || []
    allCandidates.value = candidateRes.data.data || []
    const scoredIds = new Set(tableData.value.map(s => s.examId))
    pendingList.value = allCandidates.value.filter(c => !scoredIds.has(c.examId))
  } catch (e) {
    ElMessage.error('加载数据失败')
  }
}

async function onCandidateChange(examId) {
  if (!examId) return
  try {
    const res = await axios.get(`/api/first-test/${examId}`)
    if (res.data.data) {
      // 已有成绩自动回填
      form.politics = res.data.data.politics || 0
      form.foreignLang = res.data.data.foreignLang || 0
      form.majorBasis = res.data.data.majorBasis || 0
    } else {
      // 无成绩保持空白
      form.politics = 0
      form.foreignLang = 0
      form.majorBasis = 0
    }
  } catch (e) {
    form.politics = 0
    form.foreignLang = 0
    form.majorBasis = 0
  }
}

function showDialog(row) {
  if (row) {
    if (row.politics != null) {
      // 已有成绩：编辑模式
      editExamId.value = row.examId
      form.examId = row.examId
      form.politics = row.politics || 0
      form.foreignLang = row.foreignLang || 0
      form.majorBasis = row.majorBasis || 0
    } else {
      // 待录入：新增模式
      editExamId.value = ''
      form.examId = row.examId
      form.politics = 0
      form.foreignLang = 0
      form.majorBasis = 0
    }
  } else {
    editExamId.value = ''
    form.examId = ''
    form.politics = 0
    form.foreignLang = 0
    form.majorBasis = 0
  }
  dialogVisible.value = true
}

async function handleSave() {
  if (!form.examId) { ElMessage.warning('请选择考生'); return }
  try {
    const check = await axios.get(`/api/first-test/${form.examId}`)
    if (check.data.data) {
      await axios.put('/api/first-test/update', form)
    } else {
      await axios.post('/api/first-test/add', form)
    }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    loadData()
  } catch (e) {
    try {
      await axios.post('/api/first-test/add', form)
      ElMessage.success('保存成功')
      dialogVisible.value = false
      loadData()
    } catch (err) {
      ElMessage.error('操作失败')
    }
  }
}

async function handleDelete(examId) {
  try {
    await ElMessageBox.confirm('确认删除该初试成绩？', '提示', { type: 'warning' })
    await axios.delete(`/api/first-test/delete/${examId}`)
    ElMessage.success('删除成功')
    loadData()
  } catch (e) {}
}

onMounted(() => { loadData() })
</script>

<style scoped>
.page-container { max-width: 1000px; }
.mb-20 { margin-bottom: 20px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
</style>
