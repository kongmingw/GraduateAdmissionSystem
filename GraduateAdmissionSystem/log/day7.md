# 第七天开发日志

## 日期：2026-06-12

---

## 一、完成内容

### 1. 架构重构：Controller 不再越层调 Mapper

**问题**：AdmissionListController、StatisticsController、MajorDictController、SecondTestScoreController 都直接注入 Mapper，绕过了 Service 层。

**修复**：
- 新建 `AdmissionListService`：admit()/cancel()/getCandidates() 业务逻辑
- 新建 `StatisticsService`：screening()/admissionScreening() 统计逻辑
- 增强 `SecondTestScoreService`：add() 增加初试过线校验
- 增强 `MajorDictService`：update() 增加名额校验
- 4 个 Controller 全部改为一对一注入 Service，零 Mapper 注入

**收益**：7 个 Controller 全部统一为 `Controller → Service → Mapper`，分层清晰。

### 2. 后端业务逻辑收归

**问题**：ScoreLine.vue 和 AdmissionList.vue 前端自己调多个接口、手动算分、手动分组——业务逻辑跑在前端。

**修复**：
- 新增 `GET /api/statistics/admission-screening`：后端完成初试筛选+复试成绩计算+综合分 vs 录取线分组
- 新增 `GET /api/admission/candidates`：后端完成待录取候选列表（有初试+有复试+未录取，按线分组）
- ScoreLine.vue `runAdmissionScreening()`：-53 行，改为 1 行 API 调用
- AdmissionList.vue `loadCandidates()`：-35 行，从 5 个 API 改为 1 个

### 3. 全局异常处理

- 新增 `GlobalExceptionHandler`（@RestControllerAdvice）：RuntimeException 统一转 Result.error
- 4 个 Controller 删除手写 try-catch，代码更简洁

### 4. 注释统一

- 补齐 3 个 Controller、5 个 Mapper、3 个 Service、3 个 XML 映射文件的类注释和方法注释
- Result.java 启用 Lombok，删除手写 Getter/Setter
- ScoreLine.java 补字段注释
- 全部 Java 文件和 XML 文件注释风格统一

### 5. 前端防御性修复

- 成绩录入 handleSave 的 GET 查存在失败 → 不报错，走新增流程
- 复试成绩页分数线未设时 tolerant 回退，不阻塞页面
- 搜索过滤 `toLowerCase()` 加空值保护 `(str || '')`
- 考生表单重置从嵌套三元表达式改为 `getDefaultForm()` 函数

### 6. 清理

- 删除 `HelloWorld.vue`（Vite 模板组件，未使用）
- 删除 `mybatis-config.xml`（空文件，配置在 application.yml）
- 删除 `resources/mapper/` 下误放的 `.java` 文件
- 删除 `java/mapper/` 下多余的 `CandidateProfileMapper.xml` 副本
- 删除 `service/impl/` 目录（合并为直接类）
- `pom.xml` Lombok 升级到 1.18.46 兼容 JDK 26

### 7. Entity 统一

- 7 个 Entity 全部使用 Lombok @Data/@NoArgsConstructor/@AllArgsConstructor
- 6 个 Entity 删除手写 Getter/Setter

### 8. 依赖注入统一

- 12 个文件从 @Autowired 字段注入改为构造器注入（@RequiredArgsConstructor）

---

## 二、改动统计

前后端共涉及约 50 个文件，净删约 500 行冗余代码。

---

## 三、项目当前状态

**架构：**
```
Controller → Service → Mapper   ✅ 7/7 统一
```

**加分项：**

| 加分项 | 状态 |
|--------|:--:|
| 事务管理 @Transactional | ✅ |
| 构造器注入 + 全局异常处理 | ✅ |
| 分层架构规范 | ✅ |
| 多线程 | ❌ |
| 敏感数据加解密 | ❌ |
| Redis | ❌ |
| 分布式锁 | ❌ |
| HTTPS | ❌ |

---

## 四、待完成

1. 答辩报告文档
2. 验收评分表
3. 加分项（多线程统计、数据加解密优先）
