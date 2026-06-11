# 第六天开发日志

## 日期：2026-06-11

---

## 一、完成内容

### 1. 代码规范化重构

**Entity 层统一：**
- 6 个实体类（CandidateProfile、MajorDict、ScoreLine、FirstTestScore、SecondTestScore、AdmissionList）全部统一使用 Lombok `@Data`/`@NoArgsConstructor`/`@AllArgsConstructor`
- 移除所有手写 Getter、Setter 和构造器，代码量减少约 200 行

**依赖注入改造：**
- 7 个 Controller + 5 个 ServiceImpl 全部从 `@Autowired` 字段注入改为构造器注入
- 使用 Lombok `@RequiredArgsConstructor` + `private final`，消除 IDE 警告

**MyBatis 映射清理：**
- 移除 CandidateProfileMapper.xml 和 ScoreLineMapper.xml 中冗余的 `<resultMap>`（全局 `map-underscore-to-camel-case: true` 已处理驼峰转换）
- 删除 `resources/mapper/` 下误放的 CandidateProfileMapper.java、MajorDictMapper.java
- 删除 `java/.../mapper/` 下多余的 CandidateProfileMapper.xml 副本

### 2. 前端架构修复

**API 层补全：**
- 新建 `api/firstTest.js`、`api/secondTest.js`、`api/statistics.js` 三个 API 模块
- 7 个 View 全部改为从 API 模块调用，不再直接 `import axios`

**修复 handleSave 反模式：**
- 初试成绩（FirstTestScore.vue）和复试成绩（SecondTestScore.vue）的保存逻辑，之前 GET 查存在→PUT 更新→失败就盲目 POST 新增
- 改为：GET 查存在 → 有则 PUT 更新，无则 POST 新增 → 失败直接报错

### 3. 消除硬编码

- 后端：SecondTestScoreController、AdmissionListController、StatisticsController 3 处 `"2026"` 改为 `Year.now().getValue()` 动态获取
- 前端：SecondTestScore.vue、AdmissionList.vue 2 处改为 `new Date().getFullYear().toString()`，ScoreLine.vue 和 Statistics.vue 也用上

### 4. 其他修复

- 修正 Statistics.vue 初试均分算法：删除用分数段循环推算的无效代码，直接从 scoreOverview 三科均分取平均
- 错误处理增强：catch 块提示具体 `e.message`，区分用户取消（`e !== 'cancel'`）和外键约束阻止
- 移除 StatisticsController 中未使用的 CandidateProfileMapper 注入

---

## 二、改动统计

- 31 个文件变更，+252 行，-457 行，净减少 205 行
- 3 个文件删除，3 个文件新建

---

## 三、项目当前状态

**加分项进展：**

| 加分项 | 状态 |
|--------|:--:|
| 事务管理 @Transactional | ✅ |
| 构造器注入（代码质量） | ✅ |
| 多线程 | ❌ |
| 敏感数据加解密 | ❌ |
| Redis | ❌ |
| 分布式锁 | ❌ |
| HTTPS | ❌ |

---

## 四、待完成

1. 答辩报告文档
2. 验收评分表
3. 加分项（多线程统计、敏感数据加解密优先）
