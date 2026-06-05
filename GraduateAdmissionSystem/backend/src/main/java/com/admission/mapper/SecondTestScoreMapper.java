package com.admission.mapper;

import com.admission.entity.SecondTestScore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SecondTestScoreMapper {

    List<SecondTestScore> findAll();

    SecondTestScore findByExamId(@Param("examId") String examId);

    int insert(SecondTestScore score);

    int update(SecondTestScore score);

    int deleteByExamId(@Param("examId") String examId);
}