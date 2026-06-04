package com.example.admission.service;

import com.example.admission.entity.AdmissionList;
import com.example.admission.entity.InitialExamScore;
import com.example.admission.entity.ReexamScore;
import com.example.admission.mapper.AdmissionListMapper;
import com.example.admission.mapper.InitialExamScoreMapper;
import com.example.admission.mapper.ReexamScoreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdmissionService {
    
    @Autowired
    private AdmissionListMapper admissionListMapper;
    
    @Autowired
    private InitialExamScoreMapper initialExamScoreMapper;
    
    @Autowired
    private ReexamScoreMapper reexamScoreMapper;
    
    // 获取所有录取记录
    public List<AdmissionList> getAllAdmissions() {
        return admissionListMapper.findAll();
    }
    
    // 获取已录取的考生
    public List<AdmissionList> getAdmitted() {
        return admissionListMapper.findAdmitted();
    }
    
    // 录取考生
    @Transactional
    public int admit(String examNumber, String department) {
        InitialExamScore initialScore = initialExamScoreMapper.findByExamNumber(examNumber);
        if (initialScore == null) {
            throw new RuntimeException("该考生暂无初试成绩");
        }
        
        ReexamScore reexamScore = reexamScoreMapper.findByExamNumber(examNumber);
        if (reexamScore == null) {
            throw new RuntimeException("该考生暂无复试成绩");
        }
        
        double finalScore = initialScore.getTotalScore() * 0.6 + reexamScore.getTotalScore() * 0.4;
        
        AdmissionList admission = new AdmissionList();
        admission.setExamNumber(examNumber);
        admission.setDepartment(department);
        admission.setInitialTotalScore(initialScore.getTotalScore());
        admission.setReexamTotalScore(reexamScore.getTotalScore());
        admission.setFinalScore(finalScore);
        admission.setIsAdmitted(1);
        
        return admissionListMapper.insert(admission);
    }
    
    // 更新录取信息
    @Transactional
    public int updateAdmission(AdmissionList admission) {
        return admissionListMapper.update(admission);
    }
    
    // 取消录取
    @Transactional
    public int deleteAdmission(String examNumber) {
        return admissionListMapper.delete(examNumber);
    }
    
    // 统计录取人数
    public int countAdmitted() {
        return admissionListMapper.countAdmitted();
    }
    
    // 平均最终成绩
    public Double avgFinalScore() {
        return admissionListMapper.avgFinalScore();
    }
}