package com.admission.service.impl;

import com.admission.entity.CandidateProfile;
import com.admission.mapper.CandidateProfileMapper;
import com.admission.service.CandidateProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 考生档案业务实现类
 */
@Service
@RequiredArgsConstructor
public class CandidateProfileServiceImpl implements CandidateProfileService {

    private final CandidateProfileMapper candidateMapper;

    @Override
    public List<CandidateProfile> findAll() {
        return candidateMapper.findAll();
    }

    @Override
    public CandidateProfile findByExamId(String examId) {
        return candidateMapper.findByExamId(examId);
    }

    @Override
    public List<CandidateProfile> findByMajor(String majorCode) {
        return candidateMapper.findByMajor(majorCode);
    }

    @Override
    @Transactional  // 开启事务管理（加分项）
    public int register(CandidateProfile candidate) {
        return candidateMapper.insert(candidate);
    }

    @Override
    @Transactional
    public int update(CandidateProfile candidate) {
        return candidateMapper.update(candidate);
    }

    @Override
    @Transactional
    public int delete(String examId) {
        return candidateMapper.deleteByExamId(examId);
    }
}