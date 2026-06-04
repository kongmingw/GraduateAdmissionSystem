<template>
  <div class="student-container">
    <h2>考生信息管理</h2>
    
    <!-- 搜索栏 -->
    <el-form :inline="true" style="margin-top: 20px">
      <el-form-item label="考号">
        <el-input v-model="searchForm.examNumber" placeholder="请输入考号" clearable />
      </el-form-item>
      <el-form-item label="姓名">
        <el-input v-model="searchForm.name" placeholder="请输入姓名" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <el-button type="primary" @click="showAddDialog">新增考生</el-button>
    
    <!-- 表格 -->
    <el-table :data="filteredList" border style="margin-top: 20px" v-loading="loading">
      <el-table-column prop="examNumber" label="考号" width="120" />
      <el-table-column prop="name" label="姓名" width="100" />
      <el-table-column label="性别" width="80">
        <template #default="scope">
          {{ scope.row.gender === 1 ? '男' : '女' }}
        </template>
      </el-table-column>
      <el-table-column prop="age" label="年龄" width="80" />
      <el-table-column prop="politicalStatus" label="政治面貌" width="100" />
      <el-table-column label="应届" width="80">
        <template #default="scope">
          {{ scope.row.isFreshGraduate === 1 ? '是' : '否' }}
        </template>
      </el-table-column>
      <el-table-column prop="education" label="学历" width="80" />
      <el-table-column prop="source" label="来源" width="150" />
      <el-table-column prop="majorCode" label="专业代码" width="120" />
      <el-table-column prop="category" label="类别" width="100" />
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.row.examNumber)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="600px">
      <el-form :model="formData" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="考号" prop="examNumber">
          <el-input v-model="formData.examNumber" :disabled="isEdit" placeholder="请输入考号" />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="formData.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="formData.gender">
            <el-radio :label="1">男</el-radio>
            <el-radio :label="2">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input-number v-model="formData.age" :min="18" :max="60" />
        </el-form-item>
        <el-form-item label="政治面貌">
          <el-select v-model="formData.politicalStatus" placeholder="请选择">
            <el-option label="中共党员" value="中共党员" />
            <el-option label="共青团员" value="共青团员" />
            <el-option label="群众" value="群众" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="是否应届">
          <el-switch v-model="formData.isFreshGraduate" :active-value="1" :inactive-value="0" />
        </el-form-item>
        <el-form-item label="学历">
          <el-select v-model="formData.education" placeholder="请选择">
            <el-option label="本科" value="本科" />
            <el-option label="硕士" value="硕士" />
            <el-option label="博士" value="博士" />
          </el-select>
        </el-form-item>
        <el-form-item label="来源" prop="source">
          <el-input v-model="formData.source" placeholder="请输入毕业院校或单位" />
        </el-form-item>
        <el-form-item label="专业代码">
          <el-select v-model="formData.majorCode" placeholder="请选择">
            <el-option label="081200 计算机科学与技术" value="081200" />
            <el-option label="083500 软件工程" value="083500" />
            <el-option label="083900 网络空间安全" value="083900" />
          </el-select>
        </el-form-item>
        <el-form-item label="报考类别">
          <el-radio-group v-model="formData.category">
            <el-radio label="学术型">学术型</el-radio>
            <el-radio label="专业型">专业型</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getStudents, addStudent, updateStudent, deleteStudent } from '../api/student'
import { ElMessage, ElMessageBox } from 'element-plus'

const studentList = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

// 搜索表单
const searchForm = ref({
  examNumber: '',
  name: ''
})

// 表单数据
const formData = ref({
  examNumber: '',
  name: '',
  gender: 1,
  age: 22,
  politicalStatus: '',
  isFreshGraduate: 1,
  education: '',
  source: '',
  majorCode: '',
  category: ''
})

// 表单校验规则
const rules = {
  examNumber: [{ required: true, message: '请输入考号', trigger: 'blur' }],
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  age: [{ required: true, message: '请输入年龄', trigger: 'blur' }],
  source: [{ required: true, message: '请输入来源', trigger: 'blur' }]
}

// 对话框标题
const dialogTitle = computed(() => isEdit.value ? '编辑考生' : '新增考生')

// 过滤后的列表
const filteredList = computed(() => {
  return studentList.value.filter(item => {
    const matchExam = !searchForm.value.examNumber || item.examNumber.includes(searchForm.value.examNumber)
    const matchName = !searchForm.value.name || item.name.includes(searchForm.value.name)
    return matchExam && matchName
  })
})

// 加载考生列表
const loadStudents = async () => {
  loading.value = true
  try {
    const res = await getStudents()
    studentList.value = res.data.data
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  // 前端过滤，不需要重新请求
}

// 重置搜索
const resetSearch = () => {
  searchForm.value.examNumber = ''
  searchForm.value.name = ''
}

// 新增
const showAddDialog = () => {
  isEdit.value = false
  formData.value = {
    examNumber: '',
    name: '',
    gender: 1,
    age: 22,
    politicalStatus: '共青团员',
    isFreshGraduate: 1,
    education: '本科',
    source: '',
    majorCode: '081200',
    category: '学术型'
  }
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  isEdit.value = true
  formData.value = { ...row }
  dialogVisible.value = true
}

// 提交表单
const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  try {
    if (isEdit.value) {
      await updateStudent(formData.value)
      ElMessage.success('更新成功')
    } else {
      await addStudent(formData.value)
      ElMessage.success('添加成功')
    }
    dialogVisible.value = false
    loadStudents()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

// 删除
const handleDelete = async (examNumber) => {
  try {
    await ElMessageBox.confirm('确定删除该考生吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteStudent(examNumber)
    ElMessage.success('删除成功')
    loadStudents()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
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