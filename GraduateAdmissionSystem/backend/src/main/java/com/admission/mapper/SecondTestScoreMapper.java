package com.admission.mapper;

import com.admission.entity.SecondTestScore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 复试成绩 Mapper
 */
@Mapper
public interface SecondTestScoreMapper {

    /** 查询所有复试成绩 */
    List<SecondTestScore> findAll();

    /** 根据考号查询 */
    SecondTestScore findByExamId(@Param("examId") String examId);

    /** 新增复试成绩 */
    int insert(SecondTestScore score);

    /** 更新复试成绩 */
    int update(SecondTestScore score);

    /** 删除复试成绩 */
    int deleteByExamId(@Param("examId") String examId);
}