-- =============================================
-- 研究生招生信息管理系统 - 数据库建表脚本
-- =============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS admission_system 
    DEFAULT CHARACTER SET utf8mb4 
    COLLATE utf8mb4_unicode_ci;

USE admission_system;

-- 1. 专业字典表
DROP TABLE IF EXISTS major_dict;
CREATE TABLE major_dict (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    major_code VARCHAR(10) NOT NULL COMMENT '专业代码',
    major_name VARCHAR(100) NOT NULL COMMENT '专业名称',
    planned_enrollment INT DEFAULT 0 COMMENT '计划内招生数',
    extra_enrollment INT DEFAULT 0 COMMENT '计划外招生数',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_major_code (major_code)
) COMMENT '专业字典表';

-- 2. 考生档案表
DROP TABLE IF EXISTS student_profile;
CREATE TABLE student_profile (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    exam_number VARCHAR(20) NOT NULL COMMENT '考号',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    gender TINYINT COMMENT '性别：1-男，2-女',
    age INT COMMENT '年龄',
    political_status VARCHAR(20) COMMENT '政治面貌',
    is_fresh_graduate TINYINT DEFAULT 1 COMMENT '是否应届：1-是，0-否',
    education VARCHAR(20) COMMENT '学历',
    source VARCHAR(100) COMMENT '考生来源',
    major_code VARCHAR(10) COMMENT '报考专业代码',
    category VARCHAR(20) COMMENT '报考类别',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_exam_number (exam_number),
    KEY idx_major_code (major_code)
) COMMENT '考生档案表';

-- 3. 初试成绩表
DROP TABLE IF EXISTS initial_exam_score;
CREATE TABLE initial_exam_score (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    exam_number VARCHAR(20) NOT NULL COMMENT '考号',
    politics_score DECIMAL(5,1) COMMENT '政治成绩',
    foreign_language_score DECIMAL(5,1) COMMENT '外语成绩',
    professional_basic_score DECIMAL(5,1) COMMENT '专业基础科目成绩',
    total_score DECIMAL(6,1) GENERATED ALWAYS AS 
        (politics_score + foreign_language_score + professional_basic_score) STORED COMMENT '总分（计算列）',
    average_score DECIMAL(5,2) GENERATED ALWAYS AS 
        ((politics_score + foreign_language_score + professional_basic_score) / 3.0) STORED COMMENT '平均分（计算列）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_exam_number (exam_number)
) COMMENT '初试成绩表';

-- 4. 复试成绩表
DROP TABLE IF EXISTS reexam_score;
CREATE TABLE reexam_score (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    exam_number VARCHAR(20) NOT NULL COMMENT '考号',
    professional_subject_score DECIMAL(5,1) COMMENT '复试专业科目成绩',
    interview_score DECIMAL(5,1) COMMENT '面试成绩',
    computer_test_score DECIMAL(5,1) COMMENT '上机成绩',
    total_score DECIMAL(6,1) GENERATED ALWAYS AS 
        (professional_subject_score + interview_score + computer_test_score) STORED COMMENT '复试总分（计算列）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_exam_number (exam_number)
) COMMENT '复试成绩表';

-- 5. 分数线表
DROP TABLE IF EXISTS score_line;
CREATE TABLE score_line (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    major_code VARCHAR(10) COMMENT '专业代码',
    politics_line DECIMAL(5,1) COMMENT '政治单科线',
    foreign_language_line DECIMAL(5,1) COMMENT '外语单科线',
    professional_basic_line DECIMAL(5,1) COMMENT '专业基础单科线',
    total_score_line DECIMAL(6,1) COMMENT '总分线',
    year INT COMMENT '年份',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    KEY idx_major_code (major_code)
) COMMENT '分数线表';

-- 6. 录取名单表
DROP TABLE IF EXISTS admission_list;
CREATE TABLE admission_list (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    exam_number VARCHAR(20) NOT NULL COMMENT '考号',
    department VARCHAR(50) COMMENT '录取系别',
    initial_total_score DECIMAL(6,1) COMMENT '初试总成绩',
    reexam_total_score DECIMAL(6,1) COMMENT '复试总成绩',
    final_score DECIMAL(6,1) COMMENT '最终成绩',
    is_admitted TINYINT DEFAULT 0 COMMENT '是否录取：1-是，0-否',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_exam_number (exam_number)
) COMMENT '录取名单表';
