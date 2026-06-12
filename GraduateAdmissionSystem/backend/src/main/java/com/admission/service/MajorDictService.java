package com.admission.service;

import com.admission.entity.AdmissionList;
import com.admission.entity.CandidateProfile;
import com.admission.entity.MajorDict;
import com.admission.mapper.AdmissionListMapper;
import com.admission.mapper.CandidateProfileMapper;
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
    private final AdmissionListMapper admissionListMapper;
    private final CandidateProfileMapper candidateProfileMapper;

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

    /** 更新专业（含招生名额校验：不能低于已录取人数） */
    @Transactional
    public int update(MajorDict majorDict) {
        String majorCode = majorDict.getMajorCode();
        int planNei = 0, planWai = 0;
        for (AdmissionList a : admissionListMapper.findAll()) {
            if (majorCode.equals(a.getAdmittedMajor())) {
                CandidateProfile cp = candidateProfileMapper.findByExamId(a.getExamId());
                if (cp != null) {
                    if ("计划内".equals(cp.getCategory())) planNei++;
                    else planWai++;
                }
            }
        }
        if (planNei > majorDict.getPlannedInside()) {
            throw new RuntimeException("计划内已录取" + planNei + "人，不能将招生数改为" + majorDict.getPlannedInside());
        }
        if (planWai > majorDict.getPlannedOutside()) {
            throw new RuntimeException("计划外已录取" + planWai + "人，不能将招生数改为" + majorDict.getPlannedOutside());
        }
        return majorDictMapper.update(majorDict);
    }

    /** 删除专业 */
    @Transactional
    public int delete(String majorCode) {
        return majorDictMapper.deleteByCode(majorCode);
    }
}
