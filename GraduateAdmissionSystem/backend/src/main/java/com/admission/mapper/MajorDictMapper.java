package com.admission.mapper;

import com.admission.entity.MajorDict;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 专业字典 Mapper 接口
 */
@Mapper
public interface MajorDictMapper {

    /** 查询所有专业 */
    List<MajorDict> findAll();

    /** 根据专业代码查询 */
    MajorDict findByCode(@Param("majorCode") String majorCode);

    /** 新增专业 */
    int insert(MajorDict majorDict);

    /** 更新专业 */
    int update(MajorDict majorDict);

    /** 删除专业 */
    int deleteByCode(@Param("majorCode") String majorCode);
}