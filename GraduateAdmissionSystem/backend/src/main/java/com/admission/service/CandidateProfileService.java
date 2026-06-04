package com.admission.service;

import com.admission.entity.CandidateProfile;
import java.util.List;

/**
 * 考生档案业务接口
 */
public interface CandidateProfileService {

    /** 查询所有考生 */
    List<CandidateProfile> findAll();

    /** 根据考号查询 */
    CandidateProfile findByExamId(String examId);

    /** 根据报考专业查询 */
    List<CandidateProfile> findByMajor(String majorCode);

    /** 新增考生（报名） */
    int register(CandidateProfile candidate);

    /** 更新考生信息 */
    int update(CandidateProfile candidate);

    /** 删除考生 */
    int delete(String examId);
}