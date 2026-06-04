<template>
  <div id="app">
    <el-container>
      <!-- 侧边栏 -->
      <el-aside width="200px">
        <el-menu router default-active="/students">
          <el-menu-item index="/students">考生管理</el-menu-item>
          <el-menu-item index="/scores">成绩管理</el-menu-item>
          <el-menu-item index="/admission">录取名单</el-menu-item>
          <el-menu-item index="/statistics">统计分析</el-menu-item>
        </el-menu>
      </el-aside>
      
      <!-- 主内容区 -->
      <el-container>
        <el-header>
          <h1>研究生招生信息管理系统</h1>
        </el-header>
        <el-main>
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
#app {
  height: 100vh;
}
.el-header {
  background-color: #409EFF;
  color: white;
  display: flex;
  align-items: center;
  padding: 0 20px;
}
.el-aside {
  background-color: #f5f7fa;
  border-right: 1px solid #dcdfe6;
}
</style>