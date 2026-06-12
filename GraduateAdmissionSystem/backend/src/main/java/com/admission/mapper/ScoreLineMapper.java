package com.admission.mapper;

import com.admission.entity.ScoreLine;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分数线 Mapper
 */
@Mapper
public interface ScoreLineMapper {

    /** 查询所有分数线 */
    List<ScoreLine> findAll();

    /** 根据ID查询 */
    ScoreLine findById(@Param("id") Integer id);

    /** 根据年份查询 */
    ScoreLine findByYear(@Param("year") String year);

    /** 新增分数线 */
    int insert(ScoreLine scoreLine);

    /** 更新分数线 */
    int update(ScoreLine scoreLine);

    /** 删除分数线 */
    int deleteById(@Param("id") Integer id);
}
