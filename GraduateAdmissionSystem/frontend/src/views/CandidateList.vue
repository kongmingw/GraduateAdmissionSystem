<template>
  <div class="page-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>考生档案管理</span>
          <el-button type="primary" @click="showAddDialog">新增考生</el-button>
        </div>
      </template>

      <!-- 搜索栏 -->
      <el-form :inline="true">
        <el-form-item label="搜索">
          <el-input v-model="searchKeyword" placeholder="考号或姓名" clearable @input="doFilter" style="width:200px;" />
        </el-form-item>
        <el-form-item label="报考专业">
          <el-select v-model="searchMajor" placeholder="全部专业" clearable @change="doFilter" style="width:220px;">
            <el-option
              v-for="m in majorList"
              :key="m.majorCode"
              :label="m.majorName"
              :value="m.majorCode"
            >{{ m.majorName }}</el-option>
          </el-select>
        </el-form-item>
      </el-form>

      <!-- 数据表格 -->
      <el-table :data="tableData" border stripe>
        <el-table-column prop="examId" label="考号" width="100" />
        <el-table-column prop="name" label="姓名" width="80" />
        <el-table-column prop="gender" label="性别" width="60" />
        <el-table-column prop="age" label="年龄" width="60" />
        <el-table-column prop="politicalStatus" label="政治面貌" width="100" />
        <el-table-column prop="education" label="学历" width="80" />
        <el-table-column prop="origin" label="来源地" width="100" />
        <el-table-column prop="targetMajor" label="报考专业" width="100" />
        <el-table-column prop="category" label="报考类别" width="80" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="showEditDialog(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row.examId)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px" @close="resetForm">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="考号" prop="examId">
          <el-input v-model="form.examId" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-select v-model="form.gender">
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
          </el-select>
        </el-form-item>
        <el-form-item label="年龄">
          <el-input-number v-model="form.age" :min="18" :max="60" />
        </el-form-item>
        <el-form-item label="政治面貌" prop="politicalStatus">
          <el-select v-model="form.politicalStatus">
            <el-option label="中共党员" value="中共党员" />
            <el-option label="中共预备党员" value="中共预备党员" />
            <el-option label="共青团员" value="共青团员" />
            <el-option label="群众" value="群众" />
            <el-option label="其他党派" value="其他党派" />
          </el-select>
        </el-form-item>
        <el-form-item label="是否应届">
          <el-switch v-model="form.isFreshGraduate" />
        </el-form-item>
        <el-form-item label="学历" prop="education">
          <el-select v-model="form.education">
            <el-option label="本科" value="本科" />
            <el-option label="硕士" value="硕士" />
            <el-option label="博士" value="博士" />
          </el-select>
        </el-form-item>
        <el-form-item label="来源地" prop="origin">
          <el-input v-model="form.origin" />
        </el-form-item>
        <el-form-item label="报考专业" prop="targetMajor">
          <el-select v-model="form.targetMajor">
            <el-option
              v-for="m in majorList"
              :key="m.majorCode"
              :label="m.majorName"
              :value="m.majorCode"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="报考类别" prop="category">
          <el-select v-model="form.category">
            <el-option label="计划内" value="计划内" />
            <el-option label="计划外" value="计划外" />
          </el-select>
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
import { ref, reactive, onMounted, computed } from 'vue'
import { getCandidateList, getCandidateByMajor, registerCandidate, updateCandidate, deleteCandidate } from '../api/candidate'
import { getMajorList } from '../api/major'
import { ElMessage, ElMessageBox } from 'element-plus'

const formRef = ref(null)
const tableData = ref([])
const allData = ref([])
const majorList = ref([])
const searchKeyword = ref('')
const searchMajor = ref('')
const dialogVisible = ref(false)
const isEdit = ref(false)

const rules = {
  examId: [{ required: true, message: '请填写考号', trigger: 'blur' }],
  name: [{ required: true, message: '请填写姓名', trigger: 'blur' }],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  politicalStatus: [{ required: true, message: '请选择政治面貌', trigger: 'change' }],
  education: [{ required: true, message: '请选择学历', trigger: 'change' }],
  origin: [{ required: true, message: '请填写来源地', trigger: 'blur' }],
  targetMajor: [{ required: true, message: '请选择报考专业', trigger: 'change' }],
  category: [{ required: true, message: '请选择报考类别', trigger: 'change' }]
}
const dialogTitle = computed(() => isEdit.value ? '编辑考生' : '新增考生')

const form = reactive({
  examId: '',
  name: '',
  gender: '男',
  age: 22,
  politicalStatus: '',
  isFreshGraduate: true,
  education: '本科',
  origin: '',
  targetMajor: '',
  category: '计划内'
})

// 加载数据
async function loadData() {
  try {
    const res = await getCandidateList()
    allData.value = res.data || []
    doFilter()
  } catch (e) {
    ElMessage.error('加载考生数据失败')
  }
}

async function loadMajors() {
  try {
    const res = await getMajorList()
    majorList.value = res.data || []
  } catch (e) {
    console.error('加载专业列表失败')
  }
}

// 筛选
function doFilter() {
  let list = allData.value
  if (searchMajor.value) {
    list = list.filter(c => c.targetMajor === searchMajor.value)
  }
  if (searchKeyword.value) {
    const kw = searchKeyword.value.toLowerCase()
    list = list.filter(c => (c.examId || '').toLowerCase().includes(kw) || (c.name || '').toLowerCase().includes(kw))
  }
  tableData.value = list
}

function getDefaultForm() {
  return {
    examId: '', name: '', gender: '男', age: 22,
    politicalStatus: '', isFreshGraduate: true, education: '本科',
    origin: '', targetMajor: '', category: '计划内'
  }
}

// 新增
function showAddDialog() {
  isEdit.value = false
  Object.assign(form, getDefaultForm())
  dialogVisible.value = true
}

// 编辑
function showEditDialog(row) {
  isEdit.value = true
  Object.assign(form, row)
  dialogVisible.value = true
}

// 保存
async function handleSave() {
  try {
    await formRef.value.validate()
  } catch {
    return
  }
  try {
    if (isEdit.value) {
      await updateCandidate(form)
      ElMessage.success('更新成功')
    } else {
      await registerCandidate(form)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

// 删除
async function handleDelete(examId) {
  try {
    await ElMessageBox.confirm('确认删除该考生？', '提示', { type: 'warning' })
    await deleteCandidate(examId)
    ElMessage.success('删除成功')
    loadData()
  } catch (e) {
    if (e !== 'cancel') ElMessage.error('删除失败：该考生有关联的成绩或录取记录')
  }
}

function resetForm() {
  formRef.value?.resetFields()
}

onMounted(() => {
  loadData()
  loadMajors()
})
</script>

<style scoped>
.page-container {
  max-width: 1200px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>