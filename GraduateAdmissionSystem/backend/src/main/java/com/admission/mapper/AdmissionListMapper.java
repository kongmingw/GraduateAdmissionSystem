package com.admission.mapper;

import com.admission.entity.AdmissionList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdmissionListMapper {

    List<AdmissionList> findAll();

    AdmissionList findByExamId(@Param("examId") String examId);

    int insert(AdmissionList admission);

    int update(AdmissionList admission);

    int deleteByExamId(@Param("examId") String examId);

    /** 统计各专业录取人数 */
    List<Map<String, Object>> countByMajor();

    /** 统计录取生来源分布 */
    List<Map<String, Object>> countByOrigin();

    /** 统计录取生学历分布 */
    List<Map<String, Object>> countByEducation();

    /** 统计计划与实际招生对比 */
    List<Map<String, Object>> planVsActual();
}