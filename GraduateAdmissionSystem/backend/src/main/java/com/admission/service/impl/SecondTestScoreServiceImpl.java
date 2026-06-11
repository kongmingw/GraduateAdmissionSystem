package com.admission.service.impl;

import com.admission.entity.SecondTestScore;
import com.admission.mapper.SecondTestScoreMapper;
import com.admission.service.SecondTestScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SecondTestScoreServiceImpl implements SecondTestScoreService {

    private final SecondTestScoreMapper secondTestScoreMapper;

    @Override
    public List<SecondTestScore> findAll() {
        return secondTestScoreMapper.findAll();
    }

    @Override
    public SecondTestScore findByExamId(String examId) {
        return secondTestScoreMapper.findByExamId(examId);
    }

    @Override
    @Transactional
    public int add(SecondTestScore score) {
        return secondTestScoreMapper.insert(score);
    }

    @Override
    @Transactional
    public int update(SecondTestScore score) {
        return secondTestScoreMapper.update(score);
    }

    @Override
    @Transactional
    public int delete(String examId) {
        return secondTestScoreMapper.deleteByExamId(examId);
    }
}