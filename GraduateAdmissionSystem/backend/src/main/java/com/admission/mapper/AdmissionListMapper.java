package com.admission.mapper;

import com.admission.entity.AdmissionList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 录取名单 Mapper
 */
@Mapper
public interface AdmissionListMapper {

    /** 查询所有录取考生 */
    List<AdmissionList> findAll();

    /** 根据考号查询 */
    AdmissionList findByExamId(@Param("examId") String examId);

    /** 新增录取 */
    int insert(AdmissionList admission);

    /** 更新录取信息 */
    int update(AdmissionList admission);

    /** 取消录取 */
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