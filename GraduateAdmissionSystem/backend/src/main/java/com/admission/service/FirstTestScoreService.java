package com.admission.service;

import com.admission.entity.FirstTestScore;
import com.admission.mapper.FirstTestScoreMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FirstTestScoreService {

    private final FirstTestScoreMapper firstTestScoreMapper;

    public List<FirstTestScore> findAll() {
        return firstTestScoreMapper.findAll();
    }

    public FirstTestScore findByExamId(String examId) {
        return firstTestScoreMapper.findByExamId(examId);
    }

    @Transactional
    public int add(FirstTestScore score) {
        return firstTestScoreMapper.insert(score);
    }

    @Transactional
    public int update(FirstTestScore score) {
        return firstTestScoreMapper.update(score);
    }

    @Transactional
    public int delete(String examId) {
        return firstTestScoreMapper.deleteByExamId(examId);
    }
}
