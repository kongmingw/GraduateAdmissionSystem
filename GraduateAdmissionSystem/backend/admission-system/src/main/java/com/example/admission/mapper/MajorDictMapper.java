package com.example.admission.mapper;

import com.example.admission.entity.MajorDict;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface MajorDictMapper {
    
    @Select("SELECT * FROM major_dict")
    @Results({
        @Result(property = "majorCode", column = "major_code"),
        @Result(property = "majorName", column = "major_name"),
        @Result(property = "plannedEnrollment", column = "planned_enrollment"),
        @Result(property = "extraEnrollment", column = "extra_enrollment"),
        @Result(property = "createTime", column = "create_time"),
        @Result(property = "updateTime", column = "update_time")
    })
    List<MajorDict> findAll();
}