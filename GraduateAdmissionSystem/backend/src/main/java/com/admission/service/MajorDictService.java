package com.admission.service;

import com.admission.entity.MajorDict;
import com.admission.mapper.MajorDictMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 专业字典业务
 */
@Service
@RequiredArgsConstructor
public class MajorDictService {

    private final MajorDictMapper majorDictMapper;

    /** 查询所有专业 */
    public List<MajorDict> findAll() {
        return majorDictMapper.findAll();
    }

    /** 根据专业代码查询 */
    public MajorDict findByCode(String majorCode) {
        return majorDictMapper.findByCode(majorCode);
    }

    /** 新增专业 */
    @Transactional
    public int add(MajorDict majorDict) {
        return majorDictMapper.insert(majorDict);
    }

    /** 更新专业 */
    @Transactional
    public int update(MajorDict majorDict) {
        return majorDictMapper.update(majorDict);
    }

    /** 删除专业 */
    @Transactional
    public int delete(String majorCode) {
        return majorDictMapper.deleteByCode(majorCode);
    }
}
