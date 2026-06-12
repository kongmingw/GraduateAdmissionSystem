# AI 指挥记录

> 项目：研究生招生信息管理系统
> 日期：2026-06-05

---

## 1. 项目评估请求

**问**：看看我做的怎么样了

**处理**：全面审查项目代码，对比课设需求，出具详细评估报告。关键发现：
- 已完成：数据库5表、Spring Boot + MyBatis 后端、Vue3 前端、4个CRUD模块
- 缺失：录取管理 Controller、分数线管理、App.vue侧边栏缺少统计分析入口、日志只有2天

---

## 2. 补全统计分析功能

**问**：按教程完成统计分析和录取管理功能，终端报错怎么解决

**处理**：发现 `ScoreStatisticsMapper.xml` 中 SQL 使用了 `<` 比较运算符导致 XML 解析失败（`SAXParseException`），用 `<![CDATA[...]]>` 包裹含比较运算符的SQL语句修复。

**根因**：XML 不允许直接在元素内容中使用 `<` 字符，必须转义或用 CDATA 包裹。

---

## 3. App.vue 侧边栏缺统计分析入口

**问**：还差一步，侧边栏没有统计分析菜单

**处理**：在 `App.vue` 的 `<el-menu>` 中添加了统计分析菜单项，同时补充了录取管理菜单项。

---

## 4. 补全录取管理功能

**问**：接着做吧

**处理**：创建完整的录取管理模块：
- `AdmissionListController.java`：录取/取消录取 API，自动查初试复试成绩计算综合总分
- 前端 `AdmissionList.vue`：已录取列表 + 待录取列表 + 录取对话框
- 路由和侧边栏菜单

---

## 5. 数据库建表执行

**问**：数据库记得执行一下 score_line 建表语句，怎么弄

**处理**：用 `mysql` 命令行执行 `CREATE TABLE` 语句创建 `score_line` 表。

---

## 6. 添加测试数据

**问**：给我添加测试数据

**处理**：创建 `database/seed.sql`，包含 5 个专业、8 个考生的完整数据。遇到中文编码问题：需加 `--default-character-set=utf8mb4` 参数。

---

## 7. 数据逻辑问题

**问**：初试没过复试不能参加啊，那数据有点问题。还有数据有点少。

**处理**：重写 `seed.sql`，20 个考生严格按招生流程分 4 类：
- ① 初试淘汰（4人）：单科或总分不过线，无复试成绩
- ② 初试合格但综合不够录取线（5人）：有复试成绩，不在录取名单
- ③ 初试合格+综合达线未录取（3人）：等手动录取
- ④ 已录取（8人）：全程通关

**关键逻辑**：初试不合格的人不应有复试成绩，复试后综合不够的人不应在录取名单中。

---

## 8. 实现分数线筛选业务

**问**：怎么使用分数线来实现课设的录取问题

**处理**：
1. 创建 `GET /api/statistics/screening?year=2026` 初试筛选 API：逐人比对单科线和总分线，返回合格/不合格列表及淘汰原因
2. 更新 `AdmissionListController.admit()`：录取时自动查分数线，综合总分不达录取线则拒绝
3. 更新前端 `ScoreLine.vue`：每条分数线增加「筛选」按钮，展示合格/不合格考生、统计概览

---

## 9. 端口占用问题

**问**：终端报错 Port 8080 already in use

**处理**：用 `netstat -ano | findstr :8080` 找到占用进程 PID，`taskkill` 杀掉后重启。

---

## 10. 生成设计文档

**问**：给我 ai 文件夹下生成数据库设计报告和项目设计说明书

**处理**：生成了两份完整的答辩用文档：
- `数据库设计报告.md`：ER图、表设计、索引、部署说明
- `项目设计说明书.md`：技术架构、功能模块、API设计、业务流程、部署方案

---

## 项目当前状态

| 后端 Controller | 前端页面 | 数据库表 |
|:---|:---|:---|
| CandidateProfile | 考生管理 | major_dict |
| MajorDict | 专业字典 | candidate_profile |
| FirstTestScore | 初试成绩 | first_test_score |
| SecondTestScore | 复试成绩 | second_test_score |
| ScoreLine | 分数线（含筛选） | score_line |
| AdmissionList | 录取管理 | admission_list |
| Statistics | 统计分析 | — |

**待完成**：加分项（Redis、分布式锁、HTTPS、多线程、加解密）、答辩报告、验收评分表。

---

## 11. 成绩录入优化

**问**：我新增加了一个考生，但我的成绩该如何录入。初试没过复试不能录入。待录取的考生甚至还有没复试的。

**处理**：
1. 初试和复试成绩页面考号改为下拉搜索（搜索考号或姓名），不再需要手动输入考号
2. 页面底部显示"待录入"考生列表，新考生自动出现在此处
3. 复试页面只显示初试合格的考生（调用筛选 API 过滤）
4. 后端 `SecondTestScoreController.add()` 加入校验：无初试成绩或初试未过线则拒绝录入
5. 录取页面改为三区展示：已录取 / 可录取（综合达线+有复试）/ 未达标（综合不够线或未复试）
6. 选择考生后自动回填已有成绩

---

## 12. 政治面貌改为下拉选项

**问**：政治面貌应该是选项吧

**处理**：考生表单中政治面貌从文本框改为下拉选择（中共党员/中共预备党员/共青团员/群众/其他党派）。

---

## 13. 措辞修正

**问**：初试总分≥总分线应该改成初试总分≥初试总分线 / 录入成绩说法不准确

**处理**：
1. 筛选规则文字中"总分线"改为"初试总分线"，与"录取总分线"区分
2. 成绩页面按钮文字从"录入成绩"改为"录入/编辑"，对话框标题简洁化

---

## 14. 搜索功能补充

**问**：成绩的录入编辑有查找功能，那考生管理和专业字典是不是也应该有。

**处理**：
1. 考生管理：新增搜索框（考号或姓名），与专业筛选可组合使用，前端本地实时过滤
2. 专业字典：新增搜索框（专业代码或名称），前端本地实时过滤
3. 去掉考生管理中用不到的"刷新"按钮

---

## 15. 专业管理空提交与外键删除

**问**：新增专业没输入专业代码竟然通过了，且无法删除。

**处理**：
- 直接 SQL 删除脏数据（空 major_code 记录）
- 前端加校验：专业代码和名称不能为空，否则提示
- 解释了外键约束：`major_dict.major_code` 被 `candidate_profile.target_major` 和 `admission_list.admitted_major` 引用，有考生报考的专业无法直接删除

---

## 16. 报考专业下拉显示问题

**问**：报考专业筛选选项未显示出当前选择专业

**处理**：`el-select` 选项增加显式内容 + 固定宽度 220px，修复文本不显示的问题。

---

## 17. 复试后综合筛选

**问**：点击分数线跳出的初试筛选结果只有初试吗

**处理**：在分数线页面增加复试后综合筛选卡片——初试合格考生中，自动查询复试成绩，计算综合总分与录取线对比，分为「可录取考生」和「未达标/未复试」两个标签页，显示差额。

---

## 18. 招生名额限制

**问**：专业字典的招生数只是摆设

**处理**：
1. `AdmissionListController.admit()` 增加名额检查：统计目标专业已录计划内/计划外人数，录取时超限则拒绝（含具体数字提示）
2. `MajorDictController.update()` 增加校验：修改招生数不能低于当前已录取人数

---

## 19. 空提交与删除失败

**问**：无考号无名考生添加成功且无法删除 / 删除失败没提示

**处理**：
1. SQL 清理空考号脏数据
2. 考生管理加必填校验（考号+姓名）
3. 考生删除 catch 改为 `if (e !== 'cancel') ElMessage.error('删除失败：该考生有关联的成绩或录取记录')`
4. 专业字典删除同样改造

---

## 20. 表单红框校验

**问**：未填的红框效果

**处理**：考生表单引入 Element Plus `el-form :rules`，8个必填字段（考号、姓名、性别、政治面貌、学历、来源地、报考专业、报考类别）。未填项自动红框+红色提示，关闭对话框自动清除。

---

## 21. 统计分析可视化

**问**：统计分析来电花样 / 来点饼图呗

**处理**：重写 Statistics.vue：
- 顶部 4 个彩色统计卡片
- 及格率进度条（el-progress）
- 各科成绩卡片（最高/平均/最低三色显示）
- 初试/复试分数段柱状图（CSS div 实现）
- 计划vs实际对比柱状图（重叠效果）
- 来源/学历饼图 + 各科及格率环形饼图（CSS conic-gradient）
- 去掉了进度条闪动效果（striped/striped-flow）

---

## 22. 一键启动脚本

**问**：来个一键启动的 java 文件，或者分前后端 / 自动打开前端网页

**处理**：
1. 创建 3 个 .bat 启动脚本（纯英文避免编码问题）
2. 用户移动后更新路径（`%~dp0..\`）
3. start-all.bat 增加自动打开浏览器 `start http://localhost:5173`

---

## 23. 措辞与细节优化

**问**：招生年份是什么作用 / 刷新按钮有什么用 / 及格率太炫了

**处理**：
1. 解释分数线年份字段的多年度设计
2. 去掉考生管理页面不常用的"刷新"按钮
3. 进度条去掉 striped 和 striped-flow 闪动效果

---

## 24. 代码规范化重构（2026-06-12）

**问**：看一下这个项目 / 看完代码

**处理**：全面审查所有代码，发现并修复以下问题：

**Entity 层统一**：6 个类混用 Lombok + 手写 Getter/Setter，全部统一为 @Data/@NoArgsConstructor/@AllArgsConstructor，Result.java 同样启用 Lombok，删除约 200 行冗余代码。

**依赖注入改造**：12 个文件从 @Autowired 字段注入改为构造器注入（@RequiredArgsConstructor + private final），消除 IDE 警告。

**MyBatis 清理**：移除 CandidateProfile 和 ScoreLine 的冗余 resultMap（全局已配驼峰转换）；删除 resources/mapper 下误放的 .java 文件；删除 java/mapper 下多余的 .xml 副本。

**注释统一**：补齐 3 个 Controller、5 个 Mapper、3 个 Service、3 个 XML 的方法注释和字段注释，全部 Java/XML 注释风格一致。

**pom.xml**：Lombok 升级到 1.18.46 兼容 JDK 26，加注解处理器配置，否则 mvn compile 失败。

---

## 25. Service 接口合并

**问**：为什么用接口，不直接实现 / 改

**处理**：5 个 Service 接口纯属过度设计（每个只有一个实现类），删除 service/impl/ 目录，Service 类直接放在 service/ 下。Controller 无需改动（import 路径不变）。净减 5 个文件，-119 行。

---

## 26. Controller 越层调 Mapper

**问**：是不是 controller 把 service 的活抢了 / 修吧

**处理**：4 个 Controller 直接注 Mapper、业务逻辑写在 Controller 里。修复：
- 新建 `AdmissionListService`：admit()/cancel() 逻辑
- 新建 `StatisticsService`：screening()/admissionScreening() 逻辑
- 增强 `SecondTestScoreService.add()`：初试过线校验
- 增强 `MajorDictService.update()`：名额校验
- 4 个 Controller 全部改为只注 Service，零 Mapper 注入
- 7 个 Controller 全部统一为 Controller → Service → Mapper

---

## 27. 前端抢后端工作

**问**：前端是不是把后端的 service 给抢了 / 修

**处理**：ScoreLine.vue 自己调 3 个接口手动算分分组，AdmissionList.vue 自己调 5 个接口建 Map 算分。新增后端接口：
- `GET /api/statistics/admission-screening`：初试合格→复试成绩→综合分 vs 录取线
- `GET /api/admission/candidates`：有初试+有复试+未录取→按录取线分组
- ScoreLine.vue -53 行，AdmissionList.vue -35 行

---

## 28. 防御性编程修复

**问**：录不了成绩了 / 修

**处理**：handleSave 中 GET 查成绩时，后端返 500 被拦截器 reject，不走新增分支。改为 GET 单独包 try，查不到走新增。

**同时修复**：
- 复试成绩页分数线未设时整页崩掉 → tolerant 回退
- 搜索 toLowerCase() 无空值保护 → 加 `(str || '')`
- 表单重置嵌套三元表达式 → `getDefaultForm()` 函数
- 删除空文件 HelloWorld.vue 和 mybatis-config.xml

---

## 29. 全局异常处理

**问**：Controller 异常处理不统一

**处理**：新增 `GlobalExceptionHandler`（@RestControllerAdvice），RuntimeException 统一转 Result.error。
4 个 Controller 删除手写 try-catch，代码更简洁。

---

## 30. 数据库年份字段（已回撤）

**问**：今年这批完了就直接删档重来吗

**处理**：candidate_profile 加 year 列，schema.sql + seed.sql + Entity + Mapper XML + 前端都改了。后来用户决定自己回撤数据库改动，后端和前端也随之回撤。
