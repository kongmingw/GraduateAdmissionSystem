package com.example.admission.mapper;

import com.example.admission.entity.StudentProfile;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StudentProfileMapper {

    @Select("SELECT * FROM student_profile")
    @Results({
        @Result(property = "examNumber", column = "exam_number"),
        @Result(property = "politicalStatus", column = "political_status"),
        @Result(property = "isFreshGraduate", column = "is_fresh_graduate"),
        @Result(property = "majorCode", column = "major_code"),
        @Result(property = "createTime", column = "create_time"),
        @Result(property = "updateTime", column = "update_time")
    })
    List<StudentProfile> findAll();

    @Select("SELECT * FROM student_profile WHERE exam_number = #{examNumber}")
    @Results({
        @Result(property = "examNumber", column = "exam_number"),
        @Result(property = "politicalStatus", column = "political_status"),
        @Result(property = "isFreshGraduate", column = "is_fresh_graduate"),
        @Result(property = "majorCode", column = "major_code"),
        @Result(property = "createTime", column = "create_time"),
        @Result(property = "updateTime", column = "update_time")
    })
    StudentProfile findByExamNumber(String examNumber);

    @Insert("INSERT INTO student_profile(exam_number, name, gender, age, political_status, " +
            "is_fresh_graduate, education, source, major_code, category) " +
            "VALUES(#{examNumber}, #{name}, #{gender}, #{age}, #{politicalStatus}, " +
            "#{isFreshGraduate}, #{education}, #{source}, #{majorCode}, #{category})")
    int insert(StudentProfile student);

    @Update("UPDATE student_profile SET name=#{name}, gender=#{gender}, age=#{age}, " +
            "political_status=#{politicalStatus}, is_fresh_graduate=#{isFreshGraduate}, " +
            "education=#{education}, source=#{source}, major_code=#{majorCode}, " +
            "category=#{category} WHERE exam_number=#{examNumber}")
    int update(StudentProfile student);

    @Delete("DELETE FROM student_profile WHERE exam_number = #{examNumber}")
    int delete(String examNumber);
}