<template>
  <div class="page-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>初试成绩管理</span>
        </div>
      </template>

      <el-table :data="tableData" border stripe>
        <el-table-column prop="examId" label="考号" width="120" />
        <el-table-column prop="politics" label="政治" width="80" />
        <el-table-column prop="foreignLang" label="外语" width="80" />
        <el-table-column prop="majorBasis" label="专业基础" width="100" />
        <el-table-column prop="totalFirst" label="初试总分" width="100" />
        <el-table-column label="操作" width="120">
          <template #default="scope">
            <el-button size="small" @click="showEditDialog(scope.row)">录入/编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog title="录入初试成绩" v-model="dialogVisible" width="400px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="考号">
          <el-input v-model="form.examId" />
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
import { ElMessage } from 'element-plus'

const tableData = ref([])
const dialogVisible = ref(false)
const form = reactive({
  examId: '',
  politics: 0,
  foreignLang: 0,
  majorBasis: 0
})

async function loadData() {
  try {
    const res = await axios.get('/api/first-test/list')
    tableData.value = res.data.data || []
  } catch (e) {
    ElMessage.error('加载数据失败')
  }
}

function showEditDialog(row) {
  if (row) {
    form.examId = row.examId
    form.politics = row.politics || 0
    form.foreignLang = row.foreignLang || 0
    form.majorBasis = row.majorBasis || 0
  }
  dialogVisible.value = true
}

async function handleSave() {
  try {
    // 先查是否存在
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
    // 不存在则新增
    try {
      await axios.post('/api/first-test/add', form)
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