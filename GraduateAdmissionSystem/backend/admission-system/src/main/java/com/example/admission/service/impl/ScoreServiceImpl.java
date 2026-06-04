package com.example.admission.service.impl;

import com.example.admission.entity.InitialExamScore;
import com.example.admission.entity.ReexamScore;
import com.example.admission.mapper.InitialExamScoreMapper;
import com.example.admission.mapper.ReexamScoreMapper;
import com.example.admission.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ScoreServiceImpl implements ScoreService {
    
    @Autowired
    private InitialExamScoreMapper initialExamScoreMapper;
    
    @Autowired
    private ReexamScoreMapper reexamScoreMapper;
    
    @Override
    public InitialExamScore getInitialScore(String examNumber) {
        return initialExamScoreMapper.findByExamNumber(examNumber);
    }
    
    @Override
    @Transactional
    public int saveInitialScore(InitialExamScore score) {
        InitialExamScore exist = initialExamScoreMapper.findByExamNumber(score.getExamNumber());
        if (exist != null) {
            return initialExamScoreMapper.update(score);
        }
        return initialExamScoreMapper.insert(score);
    }
    
    @Override
    public ReexamScore getReexamScore(String examNumber) {
        return reexamScoreMapper.findByExamNumber(examNumber);
    }
    
    @Override
    @Transactional
    public int saveReexamScore(ReexamScore score) {
        ReexamScore exist = reexamScoreMapper.findByExamNumber(score.getExamNumber());
        if (exist != null) {
            return reexamScoreMapper.update(score);
        }
        return reexamScoreMapper.insert(score);
    }
}