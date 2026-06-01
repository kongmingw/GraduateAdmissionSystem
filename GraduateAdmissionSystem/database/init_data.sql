-- =============================================
-- 研究生招生信息管理系统 - 测试数据
-- =============================================

USE admission_system;

-- 插入专业数据
INSERT INTO major_dict (major_code, major_name, planned_enrollment, extra_enrollment) VALUES
('081200', '计算机科学与技术', 30, 5),
('083500', '软件工程', 25, 5),
('083900', '网络空间安全', 20, 3);

-- 插入考生档案数据
INSERT INTO student_profile (exam_number, name, gender, age, political_status, is_fresh_graduate, education, source, major_code, category) VALUES
('20260001', '张三', 1, 22, '共青团员', 1, '本科', '清华大学', '081200', '学术型'),
('20260002', '李四', 2, 23, '中共党员', 1, '本科', '北京大学', '081200', '学术型'),
('20260003', '王五', 1, 24, '共青团员', 0, '本科', '浙江大学', '083500', '专业型'),
('20260004', '赵六', 2, 22, '群众', 1, '本科', '上海交通大学', '083500', '学术型'),
('20260005', '孙七', 1, 25, '中共党员', 0, '硕士', '某互联网公司', '083900', '学术型');

-- 插入初试成绩数据
INSERT INTO initial_exam_score (exam_number, politics_score, foreign_language_score, professional_basic_score) VALUES
('20260001', 75, 80, 130),
('20260002', 68, 72, 125),
('20260003', 70, 85, 120),
('20260004', 65, 60, 110),
('20260005', 78, 88, 135);

-- 插入复试成绩数据
INSERT INTO reexam_score (exam_number, professional_subject_score, interview_score, computer_test_score) VALUES
('20260001', 85, 90, 88),
('20260002', 78, 85, 80),
('20260003', 82, 88, 85),
('20260004', 70, 75, 72),
('20260005', 90, 92, 95);

-- 插入分数线数据
INSERT INTO score_line (major_code, politics_line, foreign_language_line, professional_basic_line, total_score_line, year) VALUES
('081200', 60, 60, 90, 280, 2026),
('083500', 60, 60, 85, 270, 2026),
('083900', 60, 60, 90, 285, 2026);