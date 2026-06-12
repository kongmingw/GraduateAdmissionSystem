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

    /** 查询所有考生 */
    public List<CandidateProfile> findAll() {
        return candidateMapper.findAll();
    }

    /** 根据考号查询 */
    public CandidateProfile findByExamId(String examId) {
        return candidateMapper.findByExamId(examId);
    }

    /** 根据报考专业查询 */
    public List<CandidateProfile> findByMajor(String majorCode) {
        return candidateMapper.findByMajor(majorCode);
    }

    /** 考生报名 */
    @Transactional
    public int register(CandidateProfile candidate) {
        return candidateMapper.insert(candidate);
    }

    /** 更新考生信息 */
    @Transactional
    public int update(CandidateProfile candidate) {
        return candidateMapper.update(candidate);
    }

    /** 删除考生 */
    @Transactional
    public int delete(String examId) {
        return candidateMapper.deleteByExamId(examId);
    }
}
