package com.admission.service;

import com.admission.entity.*;
import com.admission.mapper.AdmissionListMapper;
import com.admission.mapper.CandidateProfileMapper;
import com.admission.mapper.FirstTestScoreMapper;
import com.admission.mapper.MajorDictMapper;
import com.admission.mapper.ScoreLineMapper;
import com.admission.mapper.SecondTestScoreMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.*;

/**
 * 录取管理业务
 */
@Service
@RequiredArgsConstructor
public class AdmissionListService {

    private final AdmissionListMapper admissionListMapper;
    private final FirstTestScoreMapper firstTestScoreMapper;
    private final SecondTestScoreMapper secondTestScoreMapper;
    private final ScoreLineMapper scoreLineMapper;
    private final CandidateProfileMapper candidateProfileMapper;
    private final MajorDictMapper majorDictMapper;

    /** 查询所有录取考生 */
    public List<AdmissionList> findAll() {
        return admissionListMapper.findAll();
    }

    /** 根据考号查询录取状态 */
    public AdmissionList findByExamId(String examId) {
        return admissionListMapper.findByExamId(examId);
    }

    /** 录取考生（自动计算综合总分、检查分数线、名额限制） */
    @Transactional
    public String admit(AdmissionList request) {
        String examId = request.getExamId();

        // 检查是否已被录取
        if (admissionListMapper.findByExamId(examId) != null) {
            throw new RuntimeException("该考生已在录取名单中");
        }

        // 获取初试成绩
        FirstTestScore firstScore = firstTestScoreMapper.findByExamId(examId);
        if (firstScore == null) {
            throw new RuntimeException("该考生无初试成绩，无法录取");
        }

        // 获取复试成绩
        SecondTestScore secondScore = secondTestScoreMapper.findByExamId(examId);
        if (secondScore == null) {
            throw new RuntimeException("该考生无复试成绩，无法录取");
        }

        // 计算综合总分
        double totalScore = firstScore.getTotalFirst() + secondScore.getTotalSecond();

        // 检查是否达到录取总分线
        ScoreLine line = scoreLineMapper.findByYear(String.valueOf(Year.now().getValue()));
        if (line != null && line.getAdmissionTotalLine() != null) {
            if (totalScore < line.getAdmissionTotalLine()) {
                throw new RuntimeException("综合总分(" + totalScore + ")未达到录取总分线(" + line.getAdmissionTotalLine() + ")，无法录取");
            }
        }

        // 检查招生名额
        CandidateProfile candidate = candidateProfileMapper.findByExamId(examId);
        String admittedMajor = request.getAdmittedMajor();
        if (candidate != null && admittedMajor != null) {
            MajorDict major = majorDictMapper.findByCode(admittedMajor);
            if (major != null) {
                int currentCount = 0, currentPlanNei = 0, currentPlanWai = 0;
                for (AdmissionList a : admissionListMapper.findAll()) {
                    if (admittedMajor.equals(a.getAdmittedMajor())) {
                        currentCount++;
                        CandidateProfile cp = candidateProfileMapper.findByExamId(a.getExamId());
                        if (cp != null) {
                            if ("计划内".equals(cp.getCategory())) currentPlanNei++;
                            else currentPlanWai++;
                        }
                    }
                }
                int totalPlan = major.getPlannedInside() + major.getPlannedOutside();
                if (currentCount >= totalPlan) {
                    throw new RuntimeException("该专业招生名额已满（计划" + totalPlan + "人，已录取" + currentCount + "人）");
                }
                if ("计划内".equals(candidate.getCategory()) && currentPlanNei >= major.getPlannedInside()) {
                    throw new RuntimeException("该专业计划内名额已满（计划" + major.getPlannedInside() + "人，已录取" + currentPlanNei + "人）");
                }
                if ("计划外".equals(candidate.getCategory()) && currentPlanWai >= major.getPlannedOutside()) {
                    throw new RuntimeException("该专业计划外名额已满（计划" + major.getPlannedOutside() + "人，已录取" + currentPlanWai + "人）");
                }
            }
        }

        request.setFinalFirstScore(firstScore.getTotalFirst());
        request.setFinalSecondScore(secondScore.getTotalSecond());
        request.setTotalScore(totalScore);
        admissionListMapper.insert(request);

        String lineInfo = (line != null && line.getAdmissionTotalLine() != null)
                ? "（录取线：" + line.getAdmissionTotalLine() + "）" : "";
        return "录取成功，综合总分：" + totalScore + lineInfo;
    }

    /** 取消录取 */
    @Transactional
    public void cancel(String examId) {
        if (admissionListMapper.findByExamId(examId) == null) {
            throw new RuntimeException("该考生不在录取名单中");
        }
        admissionListMapper.deleteByExamId(examId);
    }

    /** 获取待录取候选列表（有初试+有复试+未被录取，按录取线分组） */
    public Map<String, Object> getCandidates() {
        String currentYear = String.valueOf(Year.now().getValue());
        ScoreLine line = scoreLineMapper.findByYear(currentYear);
        double admissionLine = (line != null && line.getAdmissionTotalLine() != null) ? line.getAdmissionTotalLine() : 450;

        Set<String> admittedIds = new HashSet<>();
        admissionListMapper.findAll().forEach(a -> admittedIds.add(a.getExamId()));

        Map<String, Double> firstMap = new HashMap<>();
        firstTestScoreMapper.findAll().forEach(f -> firstMap.put(f.getExamId(), f.getTotalFirst()));

        Map<String, Double> secondMap = new HashMap<>();
        secondTestScoreMapper.findAll().forEach(s -> secondMap.put(s.getExamId(), s.getTotalSecond()));

        List<Map<String, Object>> qualified = new ArrayList<>();
        List<Map<String, Object>> unqualified = new ArrayList<>();

        candidateProfileMapper.findAll().forEach(c -> {
            String examId = c.getExamId();
            if (admittedIds.contains(examId)) return;
            Double totalFirst = firstMap.get(examId);
            Double totalSecond = secondMap.get(examId);
            if (totalFirst == null || totalSecond == null) return;

            double totalScore = totalFirst + totalSecond;
            Map<String, Object> entry = new HashMap<>();
            entry.put("examId", examId);
            entry.put("name", c.getName());
            entry.put("targetMajor", c.getTargetMajor());
            entry.put("finalFirstScore", totalFirst);
            entry.put("finalSecondScore", totalSecond);
            entry.put("totalScore", totalScore);

            if (totalScore >= admissionLine) {
                qualified.add(entry);
            } else {
                entry.put("gap", String.format("%.1f", admissionLine - totalScore));
                unqualified.add(entry);
            }
        });

        Map<String, Object> result = new HashMap<>();
        result.put("admissionLine", admissionLine);
        result.put("qualified", qualified);
        result.put("unqualified", unqualified);
        return result;
    }
}
