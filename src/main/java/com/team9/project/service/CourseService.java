package com.team9.project.service;

import com.team9.project.models.Course;
import com.team9.project.models.Student;
import com.team9.project.payload.request.DataRequest;
import com.team9.project.payload.response.DataResponse;
import com.team9.project.repository.mapper.CourseMapper;
import com.team9.project.repository.mapper.ScoreMapper;
import com.team9.project.repository.mapper.StudentMapper;
import com.team9.project.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;
import java.util.*;

@Service
public class CourseService {
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private ScoreMapper scoreMapper;

    /*
        mode注释：
        1：按info搜索
        2：按studentId搜索
     */
    public List<Map<String, String>> getCourseMapList(String para, int mode) {
        List<Map<String, String>> dataList = new ArrayList<>();
        List<Course> cList;
        if ("".equals(para) || para == null) {
            cList = courseMapper.findAll();
        } else if (mode == 1) {
            cList = courseMapper.getCourseByCourseInfo(para);
        } else {
            cList = getCourseByStudentId(para);
        }

        if (cList == null || cList.size() == 0) {
            return dataList;
        }
        Course c;
        Map<String, String> m;
        String courseNameParas, scoreParas, teacherParas;
        for (Course course : cList) {
            c = course;
            m = new HashMap<>();
            m.put("id", String.valueOf(c.getId()));
            courseNameParas = "model=courseEdit&id=" + c.getId();
            m.put("courseName", c.getCourseName());
            m.put("courseId", c.getCourseId());
            m.put("courseNameParas", courseNameParas);
            m.put("book", c.getBook());
            m.put("courseWare", c.getCourseWare());
            m.put("resource", c.getResource());
            m.put("teacher", c.getTeacher());
            teacherParas = "model=course&courseInfo=" + c.getTeacher();
            m.put("teacherParas", teacherParas);
            m.put("time", c.getTime());
            m.put("credit", c.getCredit());
            scoreParas = "model=score&courseId=" + c.getCourseId();
            m.put("scoreParas", scoreParas);
            dataList.add(m);
        }
        return dataList;
    }

    public DataResponse courseInit(@Valid @RequestBody DataRequest dataRequest) {
        List<Map<String, String>> dataList;
        String studentId = dataRequest.getString("studentId");
        String info = dataRequest.getString("courseInfo");

        if (studentId == null) {
            dataList = getCourseMapList(info, 1);
        } else {
            dataList = getCourseMapList(studentId, 2);
        }
        return CommonMethod.getReturnData(dataList);
    }

    public DataResponse courseQuery(@Valid @RequestBody DataRequest dataRequest) {
        String courseInfo = dataRequest.getString("courseInfo");
        List<Map<String, String>> dataList = getCourseMapList(courseInfo, 1);
        return CommonMethod.getReturnData(dataList);
    }

    public synchronized Integer getNewCourseId() {
        Integer id = courseMapper.getMaxId();
        if (id == null) {
            id = 1;
        } else {
            id = id + 1;
        }
        return id;
    }

    public DataResponse courseEditInit(@Valid @RequestBody DataRequest dataRequest) {
        String studentId = dataRequest.getString("studentId");
        Integer id = dataRequest.getInteger("id");
        Course c = null;
        List<Course> op;
        if (id != null) {
            op = courseMapper.getCourseById(id);
            if (op != null && op.size() != 0) {
                c = op.get(0);
            }
        }
        Map<String, String> form = new HashMap<>();
        if (c != null) {
            List<Student> students = c.getStudents();
            StringBuilder studentIdStr = new StringBuilder();
            for (int j = 0; j < students.size(); j++) {
                studentIdStr.append(students.get(j).getStudentId());
                if (j != students.size() - 1) {
                    studentIdStr.append(",");
                }
            }
            form.put("id", String.valueOf(c.getId()));
            form.put("courseName", c.getCourseName());
            form.put("courseId", c.getCourseId());
            form.put("book", c.getBook());
            form.put("courseWare", c.getCourseWare());
            form.put("resource", c.getResource());
            form.put("teacher", c.getTeacher());
            form.put("time", c.getTime());
            form.put("credit", c.getCredit());
            form.put("students", studentIdStr.toString());
        } else if (studentId != null) {
            form.put("students", studentId);
        }
        return CommonMethod.getReturnData(form);
    }

    public DataResponse courseEditSubmit(@Valid @RequestBody DataRequest dataRequest) {
        boolean flag = false;
        Map<String, String> form = dataRequest.getMap("form");
        Integer id = CommonMethod.getInteger(form, "id");
        String courseName = CommonMethod.getString(form, "courseName");
        String courseId = CommonMethod.getString(form, "courseId");
        String book = CommonMethod.getString(form, "book");
        String courseWare = CommonMethod.getString(form, "courseWare");
        String resource = CommonMethod.getString(form, "resource");
        String teacher = CommonMethod.getString(form, "teacher");
        String time = CommonMethod.getString(form, "time");
        String credit = CommonMethod.getString(form, "credit");
        String studentsIdStr = CommonMethod.getString(form, "students");
        List<Student> students = new ArrayList<>();
        if (!"".equals(studentsIdStr)) {
            String[] studentsId = studentsIdStr.split(",");
            for (String s : studentsId) {
                students.add(studentMapper.getStudentByStudentInfo(s).get(0));
            }
        }
        Course c = null;
        List<Course> op;
        if (id != null) {
            op = courseMapper.getCourseById(id);
            if (op != null && op.size() != 0) {
                c = op.get(0);
            }
        }
        if (c == null) {
            c = new Course();
            id = getNewCourseId();
            c.setId(id);
            flag = true;
        }
        updateStudent(c.getId(), students);
        c.setCourseName(courseName);
        c.setCourseId(courseId);
        c.setBook(book);
        c.setCourseWare(courseWare);
        c.setResource(resource);
        c.setTeacher(teacher);
        c.setTime(time);
        c.setCredit(credit);
        c.setStudents(null);
        if (flag) {
            courseMapper.addCourse(c);
        } else {
            courseMapper.updateCourse(c);
        }

        return CommonMethod.getReturnData(c.getId());
    }

    public DataResponse courseDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");
        Course c = null;
        List<Course> op;
        if (id != null) {
            op = courseMapper.getCourseById(id);
            if (op != null && op.size() != 0) {
                c = op.get(0);
            }
        }
        if (c != null) {
            deleteCourseById(c.getId());
        }
        return CommonMethod.getReturnMessageOK();
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

    public void deleteCourseById(Integer id) {
        courseMapper.deleteAllStudent(id);
        courseMapper.deleteCourseById(id);
        scoreMapper.deleteScoreByCourseId(id);
    }

    public void updateStudent(int cid, List<Student> students) {
        courseMapper.deleteAllStudent(cid);
        for (Student s : students) {
            courseMapper.insertStudent(s.getId(), cid);
        }
    }

    public List<Course> getCourseByInfo(String info) {
        List<Course> sList = courseMapper.getCourseByStudentInfo(info);
        List<Course> cList = courseMapper.getCourseByCourseInfo(info);
        Set<Integer> resultId = new HashSet<>();
        List<Course> result = new ArrayList<>();
        for (Course c : cList) {
            resultId.add(c.getId());
        }
        for (Course c : sList) {
            if (c.getId() != null) {
                resultId.add(c.getId());
            }
        }
        for (Integer id : resultId) {
            result.add(courseMapper.getCourseById(id).get(0));
        }
        return result;
    }
}