package com.team9.project.service;

import com.team9.project.models.*;
import com.team9.project.payload.request.DataRequest;
import com.team9.project.payload.response.DataResponse;
import com.team9.project.repository.mapper.StudentMapper;
import com.team9.project.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EvaluationService {
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private PracticeService practiceService;
    @Autowired
    private RewardService rewardService;
    @Autowired
    private ConsumeService consumeService;

    public DataResponse evaluationInit(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");
        Student s = null;
        List<Student> op;
        if (id != null) {
            op = studentMapper.getStudentById(id);
            if (op != null && op.size() != 0) {
                s = op.get(0);
            }
        }
        Map<String, String> form = new HashMap<>();
        if (s == null) {
            form.put("tag", "找不到该学生");
            return CommonMethod.getReturnData(form);
        }
        double gpa = 0;
        List<Score> scores = scoreService.getScoreByStudentInfo(s.getStudentId());
        for (Score ss : scores) {
            gpa += (ss.getScore() - 50.0) / 10.0;
        }
        gpa = gpa / scores.size();
        gpa *= 10;

        double consume = 0;
        List<Consume> consumes = consumeService.getConsumeByStudentInfo(s.getStudentId());
        for (Consume cc : consumes) {
            if ("支出".equals(cc.getType())) {
                consume += cc.getAmount();
            }
        }

        double practiceCredit = 0;
        List<Practice> practices = practiceService.getPracticeByStudentInfo(s.getStudentId());
        for (Practice p : practices) {
            practiceCredit += p.getCredit();
        }
        practiceCredit *= 2;

        StringBuilder tag = new StringBuilder();

        Double rewardCredit = 0.0;
        List<Reward> rewards = rewardService.getRewardByStudentInfo(s.getStudentId());
        for (Reward r : rewards) {
            if (r.getRewardName().contains("运动") || r.getRewardName().contains("篮球") || r.getRewardName().contains("足球") || r.getRewardName().contains("体育")) {
                tag.append("  运动健将");
            }
            if (r.getRewardName().contains("音乐") || r.getRewardName().contains("跳舞") || r.getRewardName().contains("舞蹈") || r.getRewardName().contains("唱歌") || r.getRewardName().contains("绘画")) {
                tag.append("  艺术大佬");
            }
            if (r.getRewardName().contains("程序") || r.getRewardName().contains("编程")) {
                tag.append("  编程大牛");
            }
            if(r.getCredit()!=null) {
                rewardCredit += r.getCredit();
            }

        }
        rewardCredit *= 3;

        double score = gpa + practiceCredit + rewardCredit;
        String desc = "平均绩点 * 10 + 实践拓培分之和 * 2 + 荣誉加权后分数 * 3";

        List<Student> allStudents = studentMapper.findAll();
        int scoreRank = 0;
        int consumeRank = 0;
        for (Student sss : allStudents) {
            List<Score> ssss = scoreService.getScoreByStudentInfo(sss.getStudentId());
            List<Consume> cc = consumeService.getConsumeByStudentInfo(sss.getStudentId());
            double ggpa = 0;
            double cconsume = 0;
            for (Score ss : ssss) {
                ggpa += ss.getScore();
            }
            for (Consume c : cc) {
                if ("支出".equals(c.getType())) {
                    cconsume += c.getAmount();
                }
            }
            ggpa = ggpa / ssss.size();
            if (ggpa > gpa) scoreRank++;
            if (cconsume > consume) consumeRank++;
        }

        if (scoreRank < allStudents.size() * 0.03) {
            tag.append("  学霸");
        }
        if (consumeRank < allStudents.size() * 0.1) {
            tag.append("  壕");
        }

        form.put("tag", "".equals(tag.toString()) ? "暂时还没有获得称号哦" : tag.toString());
        form.put("score", String.valueOf(score));
        form.put("desc", desc);
        return CommonMethod.getReturnData(form);
    }
}
