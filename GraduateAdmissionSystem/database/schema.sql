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