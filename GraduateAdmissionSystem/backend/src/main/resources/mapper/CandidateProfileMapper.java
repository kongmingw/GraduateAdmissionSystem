package com.admission.mapper;

import com.admission.entity.CandidateProfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 考生档案 Mapper 接口
 */
@Mapper
public interface CandidateProfileMapper {

    /** 查询所有考生 */
    List<CandidateProfile> findAll();

    /** 根据考号查询 */
    CandidateProfile findByExamId(@Param("examId") String examId);

    /** 根据报考专业查询 */
    List<CandidateProfile> findByMajor(@Param("targetMajor") String targetMajor);

    /** 新增考生 */
    int insert(CandidateProfile candidate);

    /** 更新考生信息 */
    int update(CandidateProfile candidate);

    /** 删除考生 */
    int deleteByExamId(@Param("examId") String examId);
}