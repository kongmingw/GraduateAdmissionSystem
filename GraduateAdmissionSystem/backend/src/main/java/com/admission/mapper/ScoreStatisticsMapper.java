package com.admission.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ScoreStatisticsMapper {

    /** 统计各科平均分、最高分、最低分 */
    Map<String, Object> scoreOverview();

    /** 统计各科及格率 */
    List<Map<String, Object>> passRate();

    /** 统计初试总分各分数段人数（<200, 200-250, 250-300, >300） */
    List<Map<String, Object>> firstScoreDistribution();

    /** 统计复试总分各分数段人数 */
    List<Map<String, Object>> secondScoreDistribution();
}