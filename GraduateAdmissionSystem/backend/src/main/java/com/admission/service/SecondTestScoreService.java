package com.admission.service;

import com.admission.entity.SecondTestScore;
import com.admission.mapper.SecondTestScoreMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SecondTestScoreService {

    private final SecondTestScoreMapper secondTestScoreMapper;

    public List<SecondTestScore> findAll() {
        return secondTestScoreMapper.findAll();
    }

    public SecondTestScore findByExamId(String examId) {
        return secondTestScoreMapper.findByExamId(examId);
    }

    @Transactional
    public int add(SecondTestScore score) {
        return secondTestScoreMapper.insert(score);
    }

    @Transactional
    public int update(SecondTestScore score) {
        return secondTestScoreMapper.update(score);
    }

    @Transactional
    public int delete(String examId) {
        return secondTestScoreMapper.deleteByExamId(examId);
    }
}
