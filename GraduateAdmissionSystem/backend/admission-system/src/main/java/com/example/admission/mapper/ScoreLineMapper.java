package com.example.admission.mapper;

import com.example.admission.entity.ScoreLine;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ScoreLineMapper {
    
    @Select("SELECT * FROM score_line")
    @Results({
        @Result(property = "majorCode", column = "major_code"),
        @Result(property = "politicsLine", column = "politics_line"),
        @Result(property = "foreignLanguageLine", column = "foreign_language_line"),
        @Result(property = "professionalBasicLine", column = "professional_basic_line"),
        @Result(property = "totalScoreLine", column = "total_score_line"),
        @Result(property = "createTime", column = "create_time")
    })
    List<ScoreLine> findAll();
    
    @Select("SELECT * FROM score_line WHERE major_code = #{majorCode}")
    @Results({
        @Result(property = "majorCode", column = "major_code"),
        @Result(property = "politicsLine", column = "politics_line"),
        @Result(property = "foreignLanguageLine", column = "foreign_language_line"),
        @Result(property = "professionalBasicLine", column = "professional_basic_line"),
        @Result(property = "totalScoreLine", column = "total_score_line"),
        @Result(property = "createTime", column = "create_time")
    })
    List<ScoreLine> findByMajorCode(String majorCode);
    
    @Insert("INSERT INTO score_line(major_code, politics_line, foreign_language_line, " +
            "professional_basic_line, total_score_line, year) " +
            "VALUES(#{majorCode}, #{politicsLine}, #{foreignLanguageLine}, " +
            "#{professionalBasicLine}, #{totalScoreLine}, #{year})")
    int insert(ScoreLine scoreLine);
    
    @Update("UPDATE score_line SET politics_line=#{politicsLine}, " +
            "foreign_language_line=#{foreignLanguageLine}, professional_basic_line=#{professionalBasicLine}, " +
            "total_score_line=#{totalScoreLine} WHERE id=#{id}")
    int update(ScoreLine scoreLine);
    
    @Delete("DELETE FROM score_line WHERE id = #{id}")
    int delete(Long id);
}