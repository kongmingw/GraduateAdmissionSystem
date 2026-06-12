package com.admission.service;

import com.admission.entity.CandidateProfile;
import com.admission.mapper.CandidateProfileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 考生档案业务
 */
@Service
@RequiredArgsConstructor
public class CandidateProfileService {

    private final CandidateProfileMapper candidateMapper;

    public List<CandidateProfile> findAll() {
        return candidateMapper.findAll();
    }

    public CandidateProfile findByExamId(String examId) {
        return candidateMapper.findByExamId(examId);
    }

    public List<CandidateProfile> findByMajor(String majorCode) {
        return candidateMapper.findByMajor(majorCode);
    }

    @Transactional
    public int register(CandidateProfile candidate) {
        return candidateMapper.insert(candidate);
    }

    @Transactional
    public int update(CandidateProfile candidate) {
        return candidateMapper.update(candidate);
    }

    @Transactional
    public int delete(String examId) {
        return candidateMapper.deleteByExamId(examId);
    }
}
