package com.admission.service.impl;

import com.admission.entity.MajorDict;
import com.admission.mapper.MajorDictMapper;
import com.admission.service.MajorDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 专业字典业务实现类
 */
@Service
public class MajorDictServiceImpl implements MajorDictService {

    @Autowired
    private MajorDictMapper majorDictMapper;

    @Override
    public List<MajorDict> findAll() {
        return majorDictMapper.findAll();
    }

    @Override
    public MajorDict findByCode(String majorCode) {
        return majorDictMapper.findByCode(majorCode);
    }

    @Override
    @Transactional  // 开启事务管理（加分项）
    public int add(MajorDict majorDict) {
        return majorDictMapper.insert(majorDict);
    }

    @Override
    @Transactional
    public int update(MajorDict majorDict) {
        return majorDictMapper.update(majorDict);
    }

    @Override
    @Transactional
    public int delete(String majorCode) {
        return majorDictMapper.deleteByCode(majorCode);
    }
}