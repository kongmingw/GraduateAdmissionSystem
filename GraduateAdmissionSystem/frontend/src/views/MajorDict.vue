<template>
  <div class="page-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>专业字典管理</span>
          <el-button type="primary" @click="showAddDialog">新增专业</el-button>
        </div>
      </template>

      <!-- 搜索栏 -->
      <el-form :inline="true" style="margin-bottom:10px;">
        <el-form-item>
          <el-input v-model="searchKeyword" placeholder="搜索专业代码或名称" clearable @input="doFilter" style="width:260px;" />
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border stripe>
        <el-table-column prop="majorCode" label="专业代码" width="120" />
        <el-table-column prop="majorName" label="专业名称" width="200" />
        <el-table-column prop="plannedInside" label="计划内招生数" width="120" />
        <el-table-column prop="plannedOutside" label="计划外招生数" width="120" />
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button size="small" @click="showEditDialog(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row.majorCode)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 对话框 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="400px">
      <el-form :model="form" label-width="120px">
        <el-form-item label="专业代码">
          <el-input v-model="form.majorCode" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="专业名称">
          <el-input v-model="form.majorName" />
        </el-form-item>
        <el-form-item label="计划内招生数">
          <el-input-number v-model="form.plannedInside" :min="0" />
        </el-form-item>
        <el-form-item label="计划外招生数">
          <el-input-number v-model="form.plannedOutside" :min="0" />
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
import { getMajorList, addMajor, updateMajor, deleteMajor } from '../api/major'
import { ElMessage, ElMessageBox } from 'element-plus'

const tableData = ref([])
const allData = ref([])
const searchKeyword = ref('')
const dialogVisible = ref(false)
const isEdit = ref(false)
const dialogTitle = computed(() => isEdit.value ? '编辑专业' : '新增专业')

const form = reactive({
  majorCode: '',
  majorName: '',
  plannedInside: 0,
  plannedOutside: 0
})

async function loadData() {
  try {
    const res = await getMajorList()
    allData.value = res.data || []
    doFilter()
  } catch (e) {
    ElMessage.error('加载专业数据失败')
  }
}

function doFilter() {
  if (!searchKeyword.value) {
    tableData.value = allData.value
    return
  }
  const kw = searchKeyword.value.toLowerCase()
  tableData.value = allData.value.filter(m =>
    m.majorCode.toLowerCase().includes(kw) || m.majorName.toLowerCase().includes(kw)
  )
}

function showAddDialog() {
  isEdit.value = false
  form.majorCode = ''
  form.majorName = ''
  form.plannedInside = 0
  form.plannedOutside = 0
  dialogVisible.value = true
}

function showEditDialog(row) {
  isEdit.value = true
  Object.assign(form, row)
  dialogVisible.value = true
}

async function handleSave() {
  if (!form.majorCode || !form.majorName) {
    ElMessage.warning('请填写专业代码和专业名称')
    return
  }
  try {
    if (isEdit.value) {
      await updateMajor(form)
      ElMessage.success('更新成功')
    } else {
      await addMajor(form)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

async function handleDelete(majorCode) {
  try {
    await ElMessageBox.confirm('确认删除该专业？', '提示', { type: 'warning' })
    await deleteMajor(majorCode)
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
  max-width: 900px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>