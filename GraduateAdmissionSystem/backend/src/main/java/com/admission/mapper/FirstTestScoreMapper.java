package com.admission.mapper;

import com.admission.entity.FirstTestScore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FirstTestScoreMapper {

    List<FirstTestScore> findAll();

    FirstTestScore findByExamId(@Param("examId") String examId);

    int insert(FirstTestScore score);

    int update(FirstTestScore score);

    int deleteByExamId(@Param("examId") String examId);
}