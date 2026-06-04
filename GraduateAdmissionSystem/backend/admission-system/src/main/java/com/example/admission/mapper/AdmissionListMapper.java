package com.example.admission.mapper;

import com.example.admission.entity.AdmissionList;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface AdmissionListMapper {
    
    @Select("SELECT * FROM admission_list")
    @Results({
        @Result(property = "examNumber", column = "exam_number"),
        @Result(property = "initialTotalScore", column = "initial_total_score"),
        @Result(property = "reexamTotalScore", column = "reexam_total_score"),
        @Result(property = "finalScore", column = "final_score"),
        @Result(property = "isAdmitted", column = "is_admitted"),
        @Result(property = "createTime", column = "create_time")
    })
    List<AdmissionList> findAll();
    
    @Select("SELECT * FROM admission_list WHERE exam_number = #{examNumber}")
    @Results({
        @Result(property = "examNumber", column = "exam_number"),
        @Result(property = "initialTotalScore", column = "initial_total_score"),
        @Result(property = "reexamTotalScore", column = "reexam_total_score"),
        @Result(property = "finalScore", column = "final_score"),
        @Result(property = "isAdmitted", column = "is_admitted"),
        @Result(property = "createTime", column = "create_time")
    })
    AdmissionList findByExamNumber(String examNumber);
    
    @Select("SELECT * FROM admission_list WHERE is_admitted = 1")
    @Results({
        @Result(property = "examNumber", column = "exam_number"),
        @Result(property = "initialTotalScore", column = "initial_total_score"),
        @Result(property = "reexamTotalScore", column = "reexam_total_score"),
        @Result(property = "finalScore", column = "final_score"),
        @Result(property = "isAdmitted", column = "is_admitted"),
        @Result(property = "createTime", column = "create_time")
    })
    List<AdmissionList> findAdmitted();
    
    @Insert("INSERT INTO admission_list(exam_number, department, initial_total_score, " +
            "reexam_total_score, final_score, is_admitted) " +
            "VALUES(#{examNumber}, #{department}, #{initialTotalScore}, " +
            "#{reexamTotalScore}, #{finalScore}, #{isAdmitted})")
    int insert(AdmissionList admission);
    
    @Update("UPDATE admission_list SET department=#{department}, " +
            "initial_total_score=#{initialTotalScore}, reexam_total_score=#{reexamTotalScore}, " +
            "final_score=#{finalScore}, is_admitted=#{isAdmitted} " +
            "WHERE exam_number=#{examNumber}")
    int update(AdmissionList admission);
    
    // 统计分析查询
    @Select("SELECT COUNT(*) FROM admission_list WHERE is_admitted = 1")
    int countAdmitted();
    
    @Select("SELECT AVG(final_score) FROM admission_list WHERE is_admitted = 1")
    Double avgFinalScore();
}