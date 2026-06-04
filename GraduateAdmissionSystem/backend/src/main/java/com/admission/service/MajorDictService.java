package com.admission.service;

import com.admission.entity.MajorDict;
import java.util.List;

/**
 * 专业字典业务接口
 */
public interface MajorDictService {

    /** 查询所有专业 */
    List<MajorDict> findAll();

    /** 根据专业代码查询 */
    MajorDict findByCode(String majorCode);

    /** 新增专业 */
    int add(MajorDict majorDict);

    /** 更新专业 */
    int update(MajorDict majorDict);

    /** 删除专业 */
    int delete(String majorCode);
}