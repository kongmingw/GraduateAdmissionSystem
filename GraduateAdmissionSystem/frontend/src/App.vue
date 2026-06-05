<template>
  <el-container class="layout-container">
    <!-- 侧边栏 -->
    <el-aside width="200px" class="sidebar">
      <div class="logo">招生管理系统</div>
      
      <el-menu
        :default-active="activeMenu"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
      >
        <el-menu-item index="/candidate">
          <el-icon><User /></el-icon>
          <span>考生管理</span>
        </el-menu-item>
        <el-menu-item index="/major">
          <el-icon><Collection /></el-icon>
          <span>专业字典</span>
        </el-menu-item>
        <el-menu-item index="/first-test">
          <el-icon><EditPen /></el-icon>
          <span>初试成绩</span>
        </el-menu-item>
        <el-menu-item index="/second-test">
          <el-icon><Document /></el-icon>
          <span>复试成绩</span>
        </el-menu-item>
        <el-menu-item index="/statistics">
          <el-icon><DataAnalysis /></el-icon>
          <span>统计分析</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <!-- 主内容区 -->
    <el-container>
      <el-header class="header">研究生招生信息管理系统</el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const activeMenu = computed(() => route.path)
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.layout-container {
  height: 100vh;
}

.sidebar {
  background-color: #304156;
}

.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  color: #fff;
  font-size: 18px;
  font-weight: bold;
  background-color: #2b3a4a;
}

.header {
  background-color: #fff;
  border-bottom: 1px solid #e6e6e6;
  line-height: 60px;
  font-size: 20px;
  font-weight: bold;
  padding-left: 20px;
}

.el-main {
  background-color: #f0f2f5;
  padding: 20px;
}
</style>