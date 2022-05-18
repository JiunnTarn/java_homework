package com.team9.project.service;

import com.team9.project.models.Score;
import com.team9.project.payload.request.DataRequest;
import com.team9.project.payload.response.DataResponse;
import com.team9.project.repository.mapper.ScoreMapper;
import com.team9.project.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.*;

@Service
public class MyScoreService {
    @Autowired
    private ScoreMapper scoreMapper;

    private String studentId = "";

    /*
        mode注释：
        1：按学生信息搜索
        2：按课程信息搜索
        3：按两者搜索
     */
    public List<Map<String, String>> getScoreMapList(String para, @Nullable String para2, int mode) {
        List<Map<String, String>> dataList = new ArrayList<>();
        List<Score> sList;
        if ("".equals(para) || para == null) {
            sList = scoreMapper.findAll();
        } else if (mode == 1) {
            sList = getScoreByStudentInfo(para);
        } else if (mode == 2) {
            sList = getScoreByCourseInfo(para);
        } else {
            sList = getScoreByStudentInfoAndCourseInfo(para, para2);
        }

        if (sList == null || sList.size() == 0) {
            return dataList;
        }
        Map<String, String> m;
        String courseNameParas;
        double averageGpa = 0.0;
        for (Score score : sList) {
            m = new HashMap<>();
            m.put("id", String.valueOf(score.getId()));
            m.put("courseId", score.getCourse().getCourseId());
            courseNameParas = "model=course&courseInfo=" + score.getCourse().getCourseId();
            m.put("courseName", score.getCourse().getCourseName());
            m.put("courseNameParas", courseNameParas);
            m.put("studentId", score.getStudent().getStudentId());
            m.put("studentName", score.getStudent().getName());
            m.put("score", String.valueOf(score.getScore()));
            averageGpa += (score.getScore() - 50.0) / 10.0;
            m.put("gpa", String.valueOf((score.getScore() - 50.0) / 10.0));
            dataList.add(m);
        }
        averageGpa = averageGpa / sList.size();
        if (mode == 1) {
            m = new HashMap<>();
            m.put("gpa", "平均绩点：" + String.format("%.1f", averageGpa));
            dataList.add(m);
        }
        return dataList;
    }

    public DataResponse myScoreInit(@Valid @RequestBody DataRequest dataRequest) {
        List<Map<String, String>> dataList;
        studentId = dataRequest.getString("studentId");

        dataList = getScoreMapList(studentId, null, 1);
        return CommonMethod.getReturnData(dataList);
    }

    public DataResponse myScoreQuery(@Valid @RequestBody DataRequest dataRequest) {
        String courseInfo = dataRequest.getString("courseInfo");
        List<Map<String, String>> dataList;
        if (courseInfo != null && !"".equals(courseInfo)) {
            dataList = getScoreMapList(studentId, courseInfo, 3);
        } else {
            dataList = getScoreMapList(studentId, null, 1);
        }
        return CommonMethod.getReturnData(dataList);
    }

    public List<Score> getScoreByStudentInfo(String studentInfo) {
        List<Score> list = scoreMapper.getScoreByStudentInfo(studentInfo);
        List<Score> result = new ArrayList<>();
        for (Score s : list) {
            if (s.getId() != null) {
                result.add(s);
            }
        }
        return result;
    }

    public List<Score> getScoreByCourseInfo(String courseInfo) {
        List<Score> list = scoreMapper.getScoreByCourseInfo(courseInfo);
        List<Score> result = new ArrayList<>();
        for (Score s : list) {
            if (s.getId() != null) {
                result.add(s);
            }
        }
        return result;
    }

    public List<Score> getScoreByStudentInfoAndCourseInfo(String studentInfo, String courseInfo) {
        List<Score> sList = scoreMapper.getScoreByStudentInfo(studentInfo);
        List<Score> cList = scoreMapper.getScoreByCourseInfo(courseInfo);
        List<Score> result = new ArrayList<>();
        for (Score s : cList) {
            for (Score ss : sList) {
                if (ss.getId() != null && Objects.equals(s.getId(), ss.getId())) {
                    result.add(s);
                    break;
                }
            }
        }
        return result;
    }
}