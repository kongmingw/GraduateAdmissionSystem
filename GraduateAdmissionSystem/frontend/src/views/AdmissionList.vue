<template>
  <div class="admission-container">
    <h2>录取名单管理</h2>

    <!-- 录取操作栏 -->
    <el-card style="margin-top: 20px">
      <el-form :inline="true">
        <el-form-item label="考号">
          <el-input v-model="admitForm.examNumber" placeholder="请输入考号" style="width: 200px" />
        </el-form-item>
        <el-form-item label="录取系别">
          <el-select v-model="admitForm.department" placeholder="请选择系别" style="width: 200px">
            <el-option label="计算机系" value="计算机系" />
            <el-option label="软件学院" value="软件学院" />
            <el-option label="网络空间安全学院" value="网络空间安全学院" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleAdmit">录取</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 录取名单表格 -->
    <el-table :data="admissionList" border style="margin-top: 20px" v-loading="loading">
      <el-table-column prop="examNumber" label="考号" width="120" />
      <el-table-column prop="department" label="录取系别" width="180" />
      <el-table-column prop="initialTotalScore" label="初试总分" width="100" />
      <el-table-column prop="reexamTotalScore" label="复试总分" width="100" />
      <el-table-column label="最终成绩" width="120">
        <template #default="scope">
          <el-tag type="danger">{{ scope.row.finalScore?.toFixed(1) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="录取状态" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.isAdmitted === 1 ? 'success' : 'info'">
            {{ scope.row.isAdmitted === 1 ? '已录取' : '未录取' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="录取时间" width="180" />
      <el-table-column label="操作" width="120" fixed="right">
        <template #default="scope">
          <el-button size="small" type="danger" @click="handleCancel(scope.row.examNumber)">
            取消录取
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAdmissionList, admitStudent, deleteAdmission } from '../api/admission'
import { ElMessage, ElMessageBox } from 'element-plus'

const admissionList = ref([])
const loading = ref(false)

const admitForm = ref({
  examNumber: '',
  department: ''
})

// 加载录取名单
const loadAdmissionList = async () => {
  loading.value = true
  try {
    const res = await getAdmissionList()
    admissionList.value = res.data.data
  } finally {
    loading.value = false
  }
}

// 录取考生
const handleAdmit = async () => {
  if (!admitForm.value.examNumber || !admitForm.value.department) {
    ElMessage.warning('请输入考号和录取系别')
    return
  }
  try {
    await admitStudent(admitForm.value.examNumber, admitForm.value.department)
    ElMessage.success('录取成功')
    admitForm.value = { examNumber: '', department: '' }
    loadAdmissionList()
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '录取失败，请确认考生已有初试和复试成绩')
  }
}

// 取消录取
const handleCancel = async (examNumber) => {
  try {
    await ElMessageBox.confirm('确定取消该考生的录取吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteAdmission(examNumber)
    ElMessage.success('已取消录取')
    loadAdmissionList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('操作失败')
    }
  }
}

onMounted(() => {
  loadAdmissionList()
})
</script>

<style scoped>
.admission-container {
  padding: 20px;
}
</style>