package com.admission.service;

import com.admission.entity.SecondTestScore;
import com.admission.mapper.SecondTestScoreMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 复试成绩业务
 */
@Service
@RequiredArgsConstructor
public class SecondTestScoreService {

    private final SecondTestScoreMapper secondTestScoreMapper;

    /** 查询所有复试成绩 */
    public List<SecondTestScore> findAll() {
        return secondTestScoreMapper.findAll();
    }

    /** 根据考号查询 */
    public SecondTestScore findByExamId(String examId) {
        return secondTestScoreMapper.findByExamId(examId);
    }

    /** 录入复试成绩 */
    @Transactional
    public int add(SecondTestScore score) {
        return secondTestScoreMapper.insert(score);
    }

    /** 更新复试成绩 */
    @Transactional
    public int update(SecondTestScore score) {
        return secondTestScoreMapper.update(score);
    }

    /** 删除复试成绩 */
    @Transactional
    public int delete(String examId) {
        return secondTestScoreMapper.deleteByExamId(examId);
    }
}
