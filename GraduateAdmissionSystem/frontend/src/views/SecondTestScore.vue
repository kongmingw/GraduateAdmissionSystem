<template>
  <div class="page-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>复试成绩管理</span>
        </div>
      </template>

      <el-table :data="tableData" border stripe>
        <el-table-column prop="examId" label="考号" width="120" />
        <el-table-column prop="majorTest" label="复试专业" width="100" />
        <el-table-column prop="interview" label="面试成绩" width="100" />
        <el-table-column prop="computerTest" label="上机成绩" width="100" />
        <el-table-column prop="totalSecond" label="复试总分" width="100" />
        <el-table-column label="操作" width="120">
          <template #default="scope">
            <el-button size="small" @click="showEditDialog(scope.row)">录入/编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog title="录入复试成绩" v-model="dialogVisible" width="400px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="考号">
          <el-input v-model="form.examId" />
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
import axios from 'axios'
import { ElMessage } from 'element-plus'

const tableData = ref([])
const dialogVisible = ref(false)
const form = reactive({
  examId: '',
  majorTest: 0,
  interview: 0,
  computerTest: 0
})

async function loadData() {
  try {
    const res = await axios.get('/api/second-test/list')
    tableData.value = res.data.data || []
  } catch (e) {
    ElMessage.error('加载数据失败')
  }
}

function showEditDialog(row) {
  if (row) {
    form.examId = row.examId
    form.majorTest = row.majorTest || 0
    form.interview = row.interview || 0
    form.computerTest = row.computerTest || 0
  }
  dialogVisible.value = true
}

async function handleSave() {
  try {
    const check = await axios.get(`/api/second-test/${form.examId}`)
    if (check.data.data) {
      await axios.put('/api/second-test/update', form)
    } else {
      await axios.post('/api/second-test/add', form)
    }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    loadData()
  } catch (e) {
    try {
      await axios.post('/api/second-test/add', form)
      ElMessage.success('保存成功')
      dialogVisible.value = false
      loadData()
    } catch (err) {
      ElMessage.error('操作失败，请检查考号是否正确')
    }
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.page-container {
  max-width: 900px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>