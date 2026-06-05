# 第三天开发日志

## 日期：2026-06-05

## 完成内容

### 后端
1. 创建成绩统计 Mapper（ScoreStatisticsMapper），实现 SQL 查询：
   - 各科平均分、最高分、最低分（scoreOverview）
   - 各科及格率/不及格率统计（passRate，>=60分为及格）
   - 初试总分分数段分布（<200, 200-249, 250-299, >=300）
   - 复试总分分数段分布（<200, 200-230, 231-260, >260）
2. 创建录取名单 Mapper XML（AdmissionListMapper.xml）：
   - CRUD 基础操作
   - 各专业录取人数统计（countByMajor）
   - 录取生来源分布统计（countByOrigin）
   - 录取生学历分布统计（countByEducation）
   - 计划与实际招生对比（planVsActual）
3. 创建统计 Controller（StatisticsController）：
   - GET /api/statistics/admission — 录取统计数据
   - GET /api/statistics/scores — 成绩分析统计数据
   - GET /api/statistics/all — 全部统计数据
4. 创建录取管理 Controller（AdmissionListController）：
   - GET /api/admission/list — 查询录取名单
   - GET /api/admission/{examId} — 查询单个考生录取状态
   - POST /api/admission/admit — 录取考生（自动获取初试和复试成绩，计算综合总分）
   - DELETE /api/admission/cancel/{examId} — 取消录取
   - 添加 @Transactional 事务管理
5. 创建分数线管理功能（ScoreLine 全套）：
   - ScoreLine 实体类
   - ScoreLineMapper + XML 映射
   - ScoreLineService + 实现类（含 @Transactional）
   - ScoreLineController CRUD 接口
   - GET /api/score-line/list — 所有分数线
   - GET /api/score-line/year/{year} — 按年份查询
   - POST /api/score-line/add — 设定分数线
   - PUT /api/score-line/update — 更新分数线
   - DELETE /api/score-line/delete/{id} — 删除分数线
6. 在 App.vue 侧边栏添加统计分析和录取管理菜单入口

### 前端
1. 创建统计分析页面（Statistics.vue）：
   - 各科成绩概览表格（平均分、最高分、最低分）
   - 各科及格率/不及格率表格
   - 初试总分分数段分布
   - 复试总分分数段分布
   - 各专业录取人数统计
   - 计划与实际招生对比
   - 录取生来源分布和学历分布
2. 创建录取管理页面（AdmissionList.vue）：
   - 已录取考生列表展示
   - 待录取考生列表（自动排除已录取的）
   - 录取对话框（选择考号和录取专业）
   - 快速录取和取消录取功能
3. 创建录取管理 API 模块（api/admission.js）
4. 配置路由 /score-line、/admission 和 /statistics
5. 创建分数线管理 API 模块（api/scoreline.js）

### 数据库
1. 创建 AdmissionListMapper.xml 统计查询 SQL
2. 创建 ScoreStatisticsMapper.xml 成绩分析 SQL
3. 添加 score_line 表（招生年份、政治单科线、外语单科线、专业基础单科线、初试总分线、录取总分线）

## 遇到问题
1. ScoreStatisticsMapper.xml 中 SQL 使用了 `<` 比较运算符，与 XML 标签冲突导致解析失败
   → 使用 `<![CDATA[...]]>` 包裹含比较运算符的 SQL 语句
2. Java 26 与 Tomcat 9.x 兼容性警告（native access）
   → 不影响功能运行，可添加 JVM 参数 --enable-native-access=ALL-UNNAMED 消除警告

## 明日计划
1. 实现初试筛选功能（根据分数线自动判断复试资格）
2. 补充更多测试数据
3. 完善答辩报告和验收评分表
4. 加分项实现（Redis、多线程等）
