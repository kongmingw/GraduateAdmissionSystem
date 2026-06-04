# 研究生招生信息管理系统

## 1. 项目概述
本系统旨在实现研究生招生工作的信息化管理，覆盖考生报名、初试复试筛选、录取分析统计三个核心阶段。系统采用前后端分离架构，后端负责业务逻辑与数据持久化，前端负责用户交互与数据展示。

## 2. 功能模块
- **考生报名管理**：考生档案的录入、修改与查询。
- **初试成绩管理**：初试成绩录入、单科/总分分数线设定、复试资格自动筛选。
- **复试成绩管理**：复试成绩录入、综合总分计算（初试+复试）、录取分数线设定及录取判定。
- **统计分析**：
    - 成绩分析：计算总分/平均分、各科及格率、分数段统计。
    - 录取分析：录取生源分析（年龄、来源、学历分布等），计划与实际招生数对比。

## 3. 技术栈
- **后端**：Spring Boot 2.x + MyBatis + MySQL 8.0
- **前端**：Vue 3 + Element Plus + Axios
- **工具**：Maven/Gradle, npm/yarn, Git
- **可选加分项**：
    - 事务管理 (`@Transactional`)
    - Redis 缓存
    - 分布式锁 (Redisson)
    - HTTPS
    - 多线程处理 (统计任务)
    - 敏感数据加解密 (如身份证/成绩加密存储)

## 4. 数据库设计 (MySQL)
**数据库名称：** `postgraduate_admission`

```sql
-- 专业字典表
CREATE TABLE `major_dict` (
    `major_code` VARCHAR(10) PRIMARY KEY COMMENT '专业代码',
    `major_name` VARCHAR(50) NOT NULL COMMENT '专业名称',
    `planned_inside` INT DEFAULT 0 COMMENT '计划内招生数',
    `planned_outside` INT DEFAULT 0 COMMENT '计划外招生数'
) COMMENT='专业字典';

-- 考生档案表
CREATE TABLE `candidate_profile` (
    `exam_id` VARCHAR(15) PRIMARY KEY COMMENT '考号',
    `name` VARCHAR(30) NOT NULL COMMENT '姓名',
    `gender` CHAR(1) COMMENT '性别',
    `age` INT COMMENT '年龄',
    `political_status` VARCHAR(20) COMMENT '政治面貌',
    `is_fresh_graduate` BOOLEAN COMMENT '是否应届',
    `education` VARCHAR(20) COMMENT '学历',
    `origin` VARCHAR(50) COMMENT '来源地',
    `target_major` VARCHAR(10) COMMENT '报考专业代码',
    `category` VARCHAR(20) COMMENT '报考类别',
    FOREIGN KEY (`target_major`) REFERENCES `major_dict`(`major_code`)
) COMMENT='考生档案';

-- 初试成绩表
CREATE TABLE `first_test_score` (
    `exam_id` VARCHAR(15) PRIMARY KEY COMMENT '考号',
    `politics` DECIMAL(5,2) COMMENT '政治成绩',
    `foreign_lang` DECIMAL(5,2) COMMENT '外语成绩',
    `major_basis` DECIMAL(5,2) COMMENT '专业基础科目',
    `total_first` DECIMAL(6,2) GENERATED ALWAYS AS (politics + foreign_lang + major_basis) VIRTUAL COMMENT '初试总分',
    FOREIGN KEY (`exam_id`) REFERENCES `candidate_profile`(`exam_id`)
) COMMENT='初试成绩';

-- 复试成绩表
CREATE TABLE `second_test_score` (
    `exam_id` VARCHAR(15) PRIMARY KEY COMMENT '考号',
    `major_test` DECIMAL(5,2) COMMENT '复试专业科目',
    `interview` DECIMAL(5,2) COMMENT '面试成绩',
    `computer_test` DECIMAL(5,2) COMMENT '上机成绩',
    `total_second` DECIMAL(6,2) GENERATED ALWAYS AS (major_test + interview + computer_test) VIRTUAL COMMENT '复试总分',
    FOREIGN KEY (`exam_id`) REFERENCES `candidate_profile`(`exam_id`)
) COMMENT='复试成绩';

-- 录取名单表
CREATE TABLE `admission_list` (
    `exam_id` VARCHAR(15) PRIMARY KEY COMMENT '考号',
    `admitted_major` VARCHAR(10) COMMENT '录取系别(专业代码)',
    `final_first_score` DECIMAL(6,2) COMMENT '最终初试成绩',
    `final_second_score` DECIMAL(6,2) COMMENT '最终复试成绩',
    `total_score` DECIMAL(6,2) COMMENT '综合总分',
    FOREIGN KEY (`exam_id`) REFERENCES `candidate_profile`(`exam_id`),
    FOREIGN KEY (`admitted_major`) REFERENCES `major_dict`(`major_code`)
) COMMENT='录取名单';
```

## 5. 项目目录结构
```
GraduateAdmissionSystem/
├── README.md                   # 项目整体说明与部署文档
├── database/                   # 1. 数据库相关
│   └── schema.sql              # 建表语句与初始数据
├── backend/                    # 2. 后端服务代码
│   ├── src/main/java/com/admission/
│   ├── src/main/resources/
│   └── pom.xml
├── frontend/                   # 3. 前端代码
│   ├── src/
│   ├── public/
│   └── package.json
└── log/                        # 4. 开发日志
    ├── 2026-06-01.md           # 每天记录，如环境搭建
    ├── 2026-06-02.md
    └── ...
```

## 6. 部署说明
### 6.1 环境要求
- JDK 11+
- Maven 3.6+
- Node.js 16+
- MySQL 8.0+
- Redis (可选)

### 6.2 后端部署
1. 执行 `database/schema.sql` 初始化数据库。
2. 修改 `backend/src/main/resources/application.yml` 中的数据源配置、Redis配置。
3. 进入 `backend` 目录，运行 `mvn clean package`。
4. 启动后端服务：`java -jar target/admission-0.0.1-SNAPSHOT.jar`。

### 6.3 前端部署
1. 进入 `frontend` 目录，运行 `npm install` 安装依赖。
2. 修改 `vite.config.js` 中的后端代理地址。
3. 运行 `npm run dev` 启动开发服务器。
4. 生产环境打包：`npm run build`，将 `dist` 目录部署到 Nginx。

### 6.4 访问地址
- 后端 API 文档：`http://localhost:8080/swagger-ui.html`
- 前端页面：`http://localhost:5173`

## 7. 答辩材料
- **报告**：详细阐述需求分析、系统设计、实现细节及难点解决。
- **验收评分表**：按课设要求整理的功能点、技术点自评。
- **日志**：位于 `log/` 目录下，每日记录开发进度、技术方案与问题解决。
