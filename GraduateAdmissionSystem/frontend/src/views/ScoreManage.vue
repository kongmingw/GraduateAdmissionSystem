<template>
  <div class="score-container">
    <h2>成绩管理</h2>
    
    <!-- 搜索考生 -->
    <el-form :inline="true" style="margin-top: 20px">
      <el-form-item label="考号">
        <el-input v-model="examNumber" placeholder="请输入考号" clearable style="width: 200px" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="searchScore">查询成绩</el-button>
      </el-form-item>
    </el-form>

    <!-- 成绩展示 -->
    <el-row :gutter="20" v-if="initialScore || reexamScore">
      <!-- 初试成绩 -->
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>初试成绩</span>
              <el-button type="primary" size="small" @click="showInitialDialog">录入/修改</el-button>
            </div>
          </template>
          <el-descriptions :column="1" border v-if="initialScore">
            <el-descriptions-item label="政治成绩">{{ initialScore.politicsScore }}</el-descriptions-item>
            <el-descriptions-item label="外语成绩">{{ initialScore.foreignLanguageScore }}</el-descriptions-item>
            <el-descriptions-item label="专业基础">{{ initialScore.professionalBasicScore }}</el-descriptions-item>
            <el-descriptions-item label="总分">
              <el-tag type="danger">{{ initialScore.totalScore }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="平均分">{{ initialScore.averageScore }}</el-descriptions-item>
          </el-descriptions>
          <el-empty v-else description="暂无初试成绩" />
        </el-card>
      </el-col>

      <!-- 复试成绩 -->
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>复试成绩</span>
              <el-button type="primary" size="small" @click="showReexamDialog">录入/修改</el-button>
            </div>
          </template>
          <el-descriptions :column="1" border v-if="reexamScore">
            <el-descriptions-item label="专业科目">{{ reexamScore.professionalSubjectScore }}</el-descriptions-item>
            <el-descriptions-item label="面试成绩">{{ reexamScore.interviewScore }}</el-descriptions-item>
            <el-descriptions-item label="上机成绩">{{ reexamScore.computerTestScore }}</el-descriptions-item>
            <el-descriptions-item label="复试总分">
              <el-tag type="warning">{{ reexamScore.totalScore }}</el-tag>
            </el-descriptions-item>
          </el-descriptions>
          <el-empty v-else description="暂无复试成绩" />
        </el-card>
      </el-col>
    </el-row>

    <!-- 初试成绩对话框 -->
    <el-dialog title="录入初试成绩" v-model="initialDialogVisible" width="400px">
      <el-form :model="initialForm" label-width="120px">
        <el-form-item label="考号">
          <el-input v-model="initialForm.examNumber" disabled />
        </el-form-item>
        <el-form-item label="政治成绩">
          <el-input-number v-model="initialForm.politicsScore" :min="0" :max="100" :precision="1" />
        </el-form-item>
        <el-form-item label="外语成绩">
          <el-input-number v-model="initialForm.foreignLanguageScore" :min="0" :max="100" :precision="1" />
        </el-form-item>
        <el-form-item label="专业基础成绩">
          <el-input-number v-model="initialForm.professionalBasicScore" :min="0" :max="150" :precision="1" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="initialDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitInitialScore">确定</el-button>
      </template>
    </el-dialog>

    <!-- 复试成绩对话框 -->
    <el-dialog title="录入复试成绩" v-model="reexamDialogVisible" width="400px">
      <el-form :model="reexamForm" label-width="120px">
        <el-form-item label="考号">
          <el-input v-model="reexamForm.examNumber" disabled />
        </el-form-item>
        <el-form-item label="专业科目">
          <el-input-number v-model="reexamForm.professionalSubjectScore" :min="0" :max="100" :precision="1" />
        </el-form-item>
        <el-form-item label="面试成绩">
          <el-input-number v-model="reexamForm.interviewScore" :min="0" :max="100" :precision="1" />
        </el-form-item>
        <el-form-item label="上机成绩">
          <el-input-number v-model="reexamForm.computerTestScore" :min="0" :max="100" :precision="1" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="reexamDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitReexamScore">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { getInitialScore, saveInitialScore, getReexamScore, saveReexamScore } from '../api/score'
import { ElMessage } from 'element-plus'

const examNumber = ref('')
const initialScore = ref(null)
const reexamScore = ref(null)
const initialDialogVisible = ref(false)
const reexamDialogVisible = ref(false)

const initialForm = ref({
  examNumber: '',
  politicsScore: 0,
  foreignLanguageScore: 0,
  professionalBasicScore: 0
})

const reexamForm = ref({
  examNumber: '',
  professionalSubjectScore: 0,
  interviewScore: 0,
  computerTestScore: 0
})

// 查询成绩
const searchScore = async () => {
  if (!examNumber.value) {
    ElMessage.warning('请输入考号')
    return
  }
  try {
    const res1 = await getInitialScore(examNumber.value)
    initialScore.value = res1.data.data
  } catch (e) {
    initialScore.value = null
  }
  try {
    const res2 = await getReexamScore(examNumber.value)
    reexamScore.value = res2.data.data
  } catch (e) {
    reexamScore.value = null
  }
}

// 显示初试录入框
const showInitialDialog = () => {
  initialForm.value = {
    examNumber: examNumber.value,
    politicsScore: initialScore.value?.politicsScore || 0,
    foreignLanguageScore: initialScore.value?.foreignLanguageScore || 0,
    professionalBasicScore: initialScore.value?.professionalBasicScore || 0
  }
  initialDialogVisible.value = true
}

// 提交初试成绩
const submitInitialScore = async () => {
  await saveInitialScore(initialForm.value)
  ElMessage.success('初试成绩保存成功')
  initialDialogVisible.value = false
  searchScore()
}

// 显示复试录入框
const showReexamDialog = () => {
  reexamForm.value = {
    examNumber: examNumber.value,
    professionalSubjectScore: reexamScore.value?.professionalSubjectScore || 0,
    interviewScore: reexamScore.value?.interviewScore || 0,
    computerTestScore: reexamScore.value?.computerTestScore || 0
  }
  reexamDialogVisible.value = true
}

// 提交复试成绩
const submitReexamScore = async () => {
  await saveReexamScore(reexamForm.value)
  ElMessage.success('复试成绩保存成功')
  reexamDialogVisible.value = false
  searchScore()
}
</script>

<style scoped>
.score-container {
  padding: 20px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>