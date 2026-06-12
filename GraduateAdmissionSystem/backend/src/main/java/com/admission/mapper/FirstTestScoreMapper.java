package com.admission.mapper;

import com.admission.entity.FirstTestScore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 初试成绩 Mapper
 */
@Mapper
public interface FirstTestScoreMapper {

    /** 查询所有初试成绩 */
    List<FirstTestScore> findAll();

    /** 根据考号查询 */
    FirstTestScore findByExamId(@Param("examId") String examId);

    /** 新增初试成绩 */
    int insert(FirstTestScore score);

    /** 更新初试成绩 */
    int update(FirstTestScore score);

    /** 删除初试成绩 */
    int deleteByExamId(@Param("examId") String examId);
}