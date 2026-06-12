package com.admission.service;

import com.admission.entity.FirstTestScore;
import com.admission.mapper.FirstTestScoreMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 初试成绩业务
 */
@Service
@RequiredArgsConstructor
public class FirstTestScoreService {

    private final FirstTestScoreMapper firstTestScoreMapper;

    /** 查询所有初试成绩 */
    public List<FirstTestScore> findAll() {
        return firstTestScoreMapper.findAll();
    }

    /** 根据考号查询 */
    public FirstTestScore findByExamId(String examId) {
        return firstTestScoreMapper.findByExamId(examId);
    }

    /** 录入初试成绩 */
    @Transactional
    public int add(FirstTestScore score) {
        return firstTestScoreMapper.insert(score);
    }

    /** 更新初试成绩 */
    @Transactional
    public int update(FirstTestScore score) {
        return firstTestScoreMapper.update(score);
    }

    /** 删除初试成绩 */
    @Transactional
    public int delete(String examId) {
        return firstTestScoreMapper.deleteByExamId(examId);
    }
}
