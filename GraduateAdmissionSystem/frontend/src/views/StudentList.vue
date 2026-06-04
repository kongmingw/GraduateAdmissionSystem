<template>
  <div class="student-container">
    <h2>考生信息管理</h2>
    
    <!-- 操作按钮 -->
    <el-button type="primary" @click="showAddDialog">新增考生</el-button>
    
    <!-- 表格 -->
    <el-table :data="studentList" border style="margin-top: 20px">
      <el-table-column prop="examNumber" label="考号" width="120" />
      <el-table-column prop="name" label="姓名" width="100" />
      <el-table-column label="性别" width="80">
        <template #default="scope">
          {{ scope.row.gender === 1 ? '男' : '女' }}
        </template>
      </el-table-column>
      <el-table-column prop="age" label="年龄" width="80" />
      <el-table-column prop="education" label="学历" width="80" />
      <el-table-column prop="source" label="来源" width="150" />
      <el-table-column prop="majorCode" label="专业代码" width="120" />
      <el-table-column prop="category" label="类别" width="100" />
      <el-table-column label="操作" width="180">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.row.examNumber)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getStudents, deleteStudent } from '../api/student'
import { ElMessage, ElMessageBox } from 'element-plus'

const studentList = ref([])

// 加载考生列表
const loadStudents = async () => {
  const res = await getStudents()
  studentList.value = res.data.data
}

// 删除考生
const handleDelete = async (examNumber) => {
  ElMessageBox.confirm('确定删除该考生吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await deleteStudent(examNumber)
    ElMessage.success('删除成功')
    loadStudents()
  })
}

const showAddDialog = () => {
  ElMessage.info('新增功能待完善')
}

const handleEdit = (row) => {
  ElMessage.info('编辑功能待完善')
}

onMounted(() => {
  loadStudents()
})
</script>

<style scoped>
.student-container {
  padding: 20px;
}
</style>