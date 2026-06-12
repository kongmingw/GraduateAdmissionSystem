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

    public List<MajorDict> findAll() {
        return majorDictMapper.findAll();
    }

    public MajorDict findByCode(String majorCode) {
        return majorDictMapper.findByCode(majorCode);
    }

    @Transactional
    public int add(MajorDict majorDict) {
        return majorDictMapper.insert(majorDict);
    }

    @Transactional
    public int update(MajorDict majorDict) {
        return majorDictMapper.update(majorDict);
    }

    @Transactional
    public int delete(String majorCode) {
        return majorDictMapper.deleteByCode(majorCode);
    }
}
