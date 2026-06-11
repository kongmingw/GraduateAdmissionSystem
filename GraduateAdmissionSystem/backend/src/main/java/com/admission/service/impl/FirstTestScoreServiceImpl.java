package com.admission.service.impl;

import com.admission.entity.FirstTestScore;
import com.admission.mapper.FirstTestScoreMapper;
import com.admission.service.FirstTestScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FirstTestScoreServiceImpl implements FirstTestScoreService {

    private final FirstTestScoreMapper firstTestScoreMapper;

    @Override
    public List<FirstTestScore> findAll() {
        return firstTestScoreMapper.findAll();
    }

    @Override
    public FirstTestScore findByExamId(String examId) {
        return firstTestScoreMapper.findByExamId(examId);
    }

    @Override
    @Transactional
    public int add(FirstTestScore score) {
        return firstTestScoreMapper.insert(score);
    }

    @Override
    @Transactional
    public int update(FirstTestScore score) {
        return firstTestScoreMapper.update(score);
    }

    @Override
    @Transactional
    public int delete(String examId) {
        return firstTestScoreMapper.deleteByExamId(examId);
    }
}