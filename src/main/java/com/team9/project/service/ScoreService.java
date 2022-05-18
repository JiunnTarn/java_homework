package com.team9.project.service;

import com.team9.project.models.Course;
import com.team9.project.models.Score;
import com.team9.project.models.Student;
import com.team9.project.payload.request.DataRequest;
import com.team9.project.payload.response.DataResponse;
import com.team9.project.repository.mapper.CourseMapper;
import com.team9.project.repository.mapper.ScoreMapper;
import com.team9.project.repository.mapper.StudentMapper;
import com.team9.project.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.*;

@Service
public class ScoreService {
    @Autowired
    private ScoreMapper scoreMapper;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private CourseMapper courseMapper;

    private String paraKeeper = "";

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
        String studentNameParas, courseNameParas;
        for (Score score : sList) {
            m = new HashMap<>();
            m.put("id", String.valueOf(score.getId()));
            m.put("courseId", score.getCourse().getCourseId());
            courseNameParas = "model=course&courseInfo=" + score.getCourse().getCourseId();
            m.put("courseName", score.getCourse().getCourseName());
            m.put("courseNameParas", courseNameParas);
            m.put("studentId", score.getStudent().getStudentId());
            studentNameParas = "model=student&studentInfo=" + score.getStudent().getStudentId();
            m.put("studentName", score.getStudent().getName());
            m.put("studentNameParas", studentNameParas);
            m.put("score", String.valueOf(score.getScore()));
            m.put("gpa", String.valueOf((score.getScore() - 50.0) / 10.0));
            dataList.add(m);
        }
        return dataList;
    }

    public DataResponse scoreInit(@Valid @RequestBody DataRequest dataRequest) {
        List<Map<String, String>> dataList;
        String studentId = dataRequest.getString("studentId");
        String courseId = dataRequest.getString("courseId");

        if (studentId != null) {
            dataList = getScoreMapList(studentId, null, 1);
        } else {
            dataList = getScoreMapList(courseId, null, 2);
            paraKeeper = courseId;
        }
        return CommonMethod.getReturnData(dataList);
    }

    public DataResponse scoreQuery(@Valid @RequestBody DataRequest dataRequest) {
        String studentInfo = dataRequest.getString("studentInfo");
        List<Map<String, String>> dataList;
        if (!"".equals(paraKeeper)) {
            dataList = getScoreMapList(studentInfo, paraKeeper, 3);
        } else {
            dataList = getScoreMapList(studentInfo, null, 1);
        }
        return CommonMethod.getReturnData(dataList);
    }

    public synchronized Integer getNewScoreId() {
        Integer id = scoreMapper.getMaxId();
        if (id == null) {
            id = 1;
        } else {
            id = id + 1;
        }
        return id;
    }

    public DataResponse scoreEditInit(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");
        Score s = null;
        Map<String, String> form = new HashMap<>();
        List<Score> op;
        if (id != null) {
            op = scoreMapper.getScoreById(id);
            if (op != null && op.size() != 0) {
                s = op.get(0);
            }
        }
        {
            if (!"".equals(paraKeeper)) {
                Course course = courseMapper.getCourseByCourseInfo(paraKeeper).get(0);
                form.put("courseId", course.getCourseId());
                form.put("courseName", course.getCourseName());
                form.put("studentName", "无需输入");
            }
        }
        if (s != null) {
            form.put("id", String.valueOf(s.getId()));
            form.put("courseId", s.getCourse().getCourseId());
            form.put("courseName", s.getCourse().getCourseName());
            form.put("studentId", s.getStudent().getStudentId());
            form.put("studentName", s.getStudent().getName());
            form.put("score", String.valueOf(s.getScore()));
        }
        return CommonMethod.getReturnData(form);
    }

    public DataResponse scoreEditSubmit(@Valid @RequestBody DataRequest dataRequest) {
        boolean flag = false;
        Map<String, String> form = dataRequest.getMap("form");
        Integer id = CommonMethod.getInteger(form, "id");
        String courseId = CommonMethod.getString(form, "courseId");
        String studentId = CommonMethod.getString(form, "studentId");
        String score = CommonMethod.getString(form, "score");
        Course course = courseMapper.getCourseByCourseInfo(courseId).get(0);
        Student student = studentMapper.getStudentByStudentInfo(studentId).get(0);

        Score s = null;
        List<Score> op;
        if (id != null) {
            op = scoreMapper.getScoreById(id);
            if (op != null && op.size() != 0) {
                s = op.get(0);
            }
        }
        if (s == null) {
            id = getNewScoreId();
            flag = true;
        }
        if (flag) {
            scoreMapper.addScore(id, student.getId(), course.getId(), score);
        } else {
            scoreMapper.updateScore(id, student.getId(), course.getId(), score);
        }

        assert s != null;
        return CommonMethod.getReturnData(id);
    }


    public DataResponse scoreDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");
        Score s = null;
        List<Score> op;
        if (id != null) {
            op = scoreMapper.getScoreById(id);
            if (op != null && op.size() != 0) {
                s = op.get(0);
            }
        }
        if (s != null) {
            scoreMapper.deleteScoreById(s.getId());
        }
        return CommonMethod.getReturnMessageOK();
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