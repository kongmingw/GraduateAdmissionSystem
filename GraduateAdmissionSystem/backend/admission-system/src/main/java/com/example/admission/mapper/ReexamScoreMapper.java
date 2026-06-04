package com.example.admission.mapper;

import com.example.admission.entity.ReexamScore;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ReexamScoreMapper {
    
    @Select("SELECT * FROM reexam_score")
    @Results({
        @Result(property = "examNumber", column = "exam_number"),
        @Result(property = "professionalSubjectScore", column = "professional_subject_score"),
        @Result(property = "interviewScore", column = "interview_score"),
        @Result(property = "computerTestScore", column = "computer_test_score"),
        @Result(property = "totalScore", column = "total_score"),
        @Result(property = "createTime", column = "create_time")
    })
    List<ReexamScore> findAll();
    
    @Select("SELECT * FROM reexam_score WHERE exam_number = #{examNumber}")
    @Results({
        @Result(property = "examNumber", column = "exam_number"),
        @Result(property = "professionalSubjectScore", column = "professional_subject_score"),
        @Result(property = "interviewScore", column = "interview_score"),
        @Result(property = "computerTestScore", column = "computer_test_score"),
        @Result(property = "totalScore", column = "total_score"),
        @Result(property = "createTime", column = "create_time")
    })
    ReexamScore findByExamNumber(String examNumber);
    
    @Insert("INSERT INTO reexam_score(exam_number, professional_subject_score, interview_score, computer_test_score) " +
            "VALUES(#{examNumber}, #{professionalSubjectScore}, #{interviewScore}, #{computerTestScore})")
    int insert(ReexamScore score);
    
    @Update("UPDATE reexam_score SET professional_subject_score=#{professionalSubjectScore}, " +
            "interview_score=#{interviewScore}, computer_test_score=#{computerTestScore} " +
            "WHERE exam_number=#{examNumber}")
    int update(ReexamScore score);
}