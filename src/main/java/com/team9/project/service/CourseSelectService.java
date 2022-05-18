package com.team9.project.service;

import com.team9.project.models.Course;
import com.team9.project.payload.request.DataRequest;
import com.team9.project.payload.response.DataResponse;
import com.team9.project.repository.mapper.CourseMapper;
import com.team9.project.repository.mapper.StudentMapper;
import com.team9.project.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.*;

@Service
public class CourseSelectService {
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private StudentMapper studentMapper;

    private String studentId = "";
    /*
        mode注释：
        1：按info搜索
        2：按studentId搜索
     */
    public List<Map<String, String>> getCourseMapList(String para, @Nullable String para2, int mode) {
        List<Map<String, String>> dataList = new ArrayList<>();
        List<Course> cList;
        if ("".equals(para) || para == null) {
            cList = courseMapper.findAll();
        } else if (mode == 1) {
            cList = courseMapper.getCourseByCourseInfo(para);
        } else if (mode == 2) {
            cList = getCourseByStudentId(para);
        } else {
            cList = getCourseByStudentInfoAndCourseInfo(para, para2);
        }

        if (cList == null || cList.size() == 0) {
            return dataList;
        }
        Course c;
        Map<String, String> m;
        String courseNameParas, teacherParas, selectParas;
        for (Course course : cList) {
            c = course;
            m = new HashMap<>();
            m.put("id", String.valueOf(c.getId()));
            courseNameParas = "model=myCourse&courseInfo=" + c.getCourseId();
            m.put("courseName", c.getCourseName());
            m.put("courseNameParas", courseNameParas);
            m.put("courseId", c.getCourseId());
            m.put("book", c.getBook());
            m.put("courseWare", c.getCourseWare());
            m.put("resource", c.getResource());
            m.put("teacher", c.getTeacher());
            teacherParas = "model=myCourse&courseInfo=" + c.getTeacher();
            m.put("teacherParas", teacherParas);
            m.put("time", c.getTime());
            m.put("credit", c.getCredit());
            m.put("studentNum", String.valueOf(c.getStudents().size()));
            selectParas = "model=message&message=未知错误&command=3&caller=courseSelect&id=" + c.getId() + "&id2=" + studentMapper.getStudentByStudentInfo(studentId).get(0).getId() + "&callerStatus1=studentId&callerStatus2=" + studentId + "&callerStatus3=courseInfo&callerStatus4=" + para;
            m.put("selectParas", selectParas);
            dataList.add(m);
        }
        return dataList;
    }

    public DataResponse courseSelectInit(@Valid @RequestBody DataRequest dataRequest) {
        List<Map<String, String>> dataList;
        studentId = dataRequest.getString("studentId");
        String info = dataRequest.getString("courseInfo");

        if (info == null) {
            dataList = getCourseMapList("", null, 1);
        } else {
            dataList = getCourseMapList(info, null, 1);
        }
        return CommonMethod.getReturnData(dataList);
    }

    public DataResponse courseSelectQuery(@Valid @RequestBody DataRequest dataRequest) {
        String courseInfo = dataRequest.getString("courseInfo");
        List<Map<String, String>> dataList = getCourseMapList(courseInfo, null, 1);

        return CommonMethod.getReturnData(dataList);
    }

    public List<Course> getCourseByStudentId(String studentId) {
        List<Course> list = courseMapper.getCourseByStudentInfo(studentId);
        List<Course> result = new ArrayList<>();
        for (Course c : list) {
            if (c.getId() != null) {
                result.add(c);
            }
        }
        return result;
    }

    public List<Course> getCourseByStudentInfoAndCourseInfo(String studentInfo, String courseInfo) {
        List<Course> sList = courseMapper.getCourseByStudentInfo(studentInfo);
        List<Course> cList = courseMapper.getCourseByCourseInfo(courseInfo);
        List<Course> result = new ArrayList<>();
        for (Course c : cList) {
            for (Course cc : sList) {
                if (cc.getId() != null && Objects.equals(c.getId(), cc.getId())) {
                    result.add(c);
                    break;
                }
            }
        }
        return result;
    }
}