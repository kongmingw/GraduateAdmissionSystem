package com.example.admission.mapper;

import com.example.admission.entity.InitialExamScore;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface InitialExamScoreMapper {
    
    @Select("SELECT * FROM initial_exam_score")
    @Results({
        @Result(property = "examNumber", column = "exam_number"),
        @Result(property = "politicsScore", column = "politics_score"),
        @Result(property = "foreignLanguageScore", column = "foreign_language_score"),
        @Result(property = "professionalBasicScore", column = "professional_basic_score"),
        @Result(property = "totalScore", column = "total_score"),
        @Result(property = "averageScore", column = "average_score"),
        @Result(property = "createTime", column = "create_time")
    })
    List<InitialExamScore> findAll();
    
    @Select("SELECT * FROM initial_exam_score WHERE exam_number = #{examNumber}")
    @Results({
        @Result(property = "examNumber", column = "exam_number"),
        @Result(property = "politicsScore", column = "politics_score"),
        @Result(property = "foreignLanguageScore", column = "foreign_language_score"),
        @Result(property = "professionalBasicScore", column = "professional_basic_score"),
        @Result(property = "totalScore", column = "total_score"),
        @Result(property = "averageScore", column = "average_score"),
        @Result(property = "createTime", column = "create_time")
    })
    InitialExamScore findByExamNumber(String examNumber);
    
    @Insert("INSERT INTO initial_exam_score(exam_number, politics_score, foreign_language_score, professional_basic_score) " +
            "VALUES(#{examNumber}, #{politicsScore}, #{foreignLanguageScore}, #{professionalBasicScore})")
    int insert(InitialExamScore score);
    
    //  加 update 方法
    @Update("UPDATE initial_exam_score SET politics_score=#{politicsScore}, " +
            "foreign_language_score=#{foreignLanguageScore}, professional_basic_score=#{professionalBasicScore} " +
            "WHERE exam_number=#{examNumber}")
    int update(InitialExamScore score);
}