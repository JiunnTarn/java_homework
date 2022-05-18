package com.team9.project.service;

import com.team9.project.models.Practice;
import com.team9.project.models.Student;
import com.team9.project.payload.request.DataRequest;
import com.team9.project.payload.response.DataResponse;
import com.team9.project.repository.mapper.PracticeMapper;
import com.team9.project.repository.mapper.StudentMapper;
import com.team9.project.util.CommonMethod;
import com.team9.project.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;
import java.text.ParseException;
import java.util.*;

@Service
public class PracticeService {
    @Autowired
    private PracticeMapper practiceMapper;
    @Autowired
    private StudentMapper studentMapper;

    private String paraKeeper = "";

    public List<Map<String, String>> getPracticeMapList(String para, @Nullable String para2, int mode) {
        List<Map<String, String>> dataList = new ArrayList<>();
        List<Practice> pList;
        if ("".equals(para) || para == null) {
            pList = practiceMapper.findAll();
        } else if (mode == 1) {
            pList = getPracticeByInfo(para);
        } else if (mode == 2) {
            pList = getPracticeByStudentInfo(para);
        } else {
            pList = getPracticeByStudentInfoAndPracticeInfo(para, para2);
        }

        if (pList == null || pList.size() == 0) {
            return dataList;
        }
        Practice p;
        Map<String, String> m;
        String categoryParas;
        for (Practice practice : pList) {
            p = practice;
            List<Student> students = p.getStudents();
            StringBuilder studentsStr = new StringBuilder();
            for (int j = 0; j < students.size(); j++) {
                studentsStr.append(students.get(j).getName());
                if (j != students.size() - 1) {
                    studentsStr.append("，");
                }
            }
            m = new HashMap<>();
            m.put("id", String.valueOf(p.getId()));
            m.put("practiceName", p.getPracticeName());
            m.put("category", p.getCategory());
            categoryParas = "model=practice&practiceInfo=" + p.getCategory();
            m.put("categoryParas", categoryParas);
            m.put("startTime", TimeUtil.parseStr(p.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
            m.put("endTime", TimeUtil.parseStr(p.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
            m.put("credit", String.valueOf(p.getCredit()));
            m.put("students", studentsStr.toString());
            dataList.add(m);
        }
        return dataList;
    }

    public DataResponse practiceInit(@Valid @RequestBody DataRequest dataRequest) {
        List<Map<String, String>> dataList;
        String studentId = dataRequest.getString("studentId");
        String practiceInfo = dataRequest.getString("practiceInfo");

        if (studentId == null) {
            dataList = getPracticeMapList(practiceInfo, null, 1);
        } else {
            dataList = getPracticeMapList(studentId, null, 2);
            paraKeeper = studentId;
        }
        return CommonMethod.getReturnData(dataList);
    }

    public DataResponse practiceQuery(@Valid @RequestBody DataRequest dataRequest) {
        String info = dataRequest.getString("info");
        List<Map<String, String>> dataList;
        if ("".equals(paraKeeper)) {
            dataList = getPracticeMapList(info, null, 1);
        } else {
            dataList = getPracticeMapList(paraKeeper, info, 3);
        }
        return CommonMethod.getReturnData(dataList);
    }

    public DataResponse practiceEditInit(@Valid @RequestBody DataRequest dataRequest) {
        String studentId = paraKeeper;
        Integer id = dataRequest.getInteger("id");
        Practice p = null;
        List<Practice> op;
        if (id != null) {
            op = practiceMapper.getPracticeById(id);
            if (op != null && op.size() != 0) {
                p = op.get(0);
            }
        }
        Map<String, String> form = new HashMap<>();
        if (p != null) {
            List<Student> students = p.getStudents();
            StringBuilder studentIdStr = new StringBuilder();
            for (int j = 0; j < students.size(); j++) {
                studentIdStr.append(students.get(j).getStudentId());
                if (j != students.size() - 1) {
                    studentIdStr.append(",");
                }
            }
            form.put("id", String.valueOf(p.getId()));
            form.put("practiceName", p.getPracticeName());
            form.put("category", p.getCategory());
            form.put("startTime", TimeUtil.parseStr(p.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
            form.put("endTime", TimeUtil.parseStr(p.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
            form.put("credit", String.valueOf(p.getCredit()));
            form.put("students", studentIdStr.toString());
        } else if (studentId != null) {
            form.put("students", studentId);
        }
        return CommonMethod.getReturnData(form);
    }

    public synchronized Integer getNewPracticeId() {
        Integer id = practiceMapper.getMaxId();
        if (id == null) {
            id = 1;
        } else {
            id = id + 1;
        }
        return id;
    }

    public DataResponse practiceEditSubmit(@Valid @RequestBody DataRequest dataRequest) throws ParseException {
        boolean flag = false;
        Map<String, String> form = dataRequest.getMap("form");
        Integer id = CommonMethod.getInteger(form, "id");
        String practiceName = CommonMethod.getString(form, "practiceName");
        String category = CommonMethod.getString(form, "category");
        String startTime = CommonMethod.getString(form, "startTime");
        String endTime = CommonMethod.getString(form, "endTime");
        Integer credit = CommonMethod.getInteger(form, "credit");
        String studentsIdStr = CommonMethod.getString(form, "students");
        List<Student> students = new ArrayList<>();
        if (!"".equals(studentsIdStr)) {
            String[] studentsId = studentsIdStr.split(",");
            for (String s : studentsId) {
                students.add(studentMapper.getStudentByStudentInfo(s).get(0));
            }
        }
        Practice p = null;
        List<Practice> op;
        if (id != null) {
            op = practiceMapper.getPracticeById(id);
            if (op != null && op.size() != 0) {
                p = op.get(0);
            }
        }
        if (p == null) {
            p = new Practice();
            id = getNewPracticeId();
            p.setId(id);
            flag = true;
        }
        updateStudent(p.getId(), students);
        p.setPracticeName(practiceName);
        p.setCategory(category);
        p.setStartTime(TimeUtil.parseDateTime(startTime));
        p.setEndTime(TimeUtil.parseDateTime(endTime));
        p.setCredit(credit);
        p.setStudents(null);
        if (flag) {
            practiceMapper.addPractice(p);
        } else {
            practiceMapper.updatePractice(p);
        }

        return CommonMethod.getReturnData(p.getId());
    }

    public DataResponse practiceDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");
        Practice p = null;
        List<Practice> op;
        if (id != null) {
            op = practiceMapper.getPracticeById(id);
            if (op != null && op.size() != 0) {
                p = op.get(0);
            }
        }
        if (p != null) {
            deletePracticeById(p.getId());
        }
        return CommonMethod.getReturnMessageOK();
    }

    public List<Practice> getPracticeByStudentInfo(String studentInfo) {
        List<Practice> list = practiceMapper.getPracticeByStudentInfo(studentInfo);
        List<Practice> result = new ArrayList<>();
        for (Practice p : list) {
            if (p.getId() != null) {
                result.add(p);
            }
        }
        return result;
    }

    public void deletePracticeById(Integer id) {
        practiceMapper.deletePracticeById(id);
        practiceMapper.deleteAllStudent(id);
    }

    public void updateStudent(int pid, List<Student> students) {
        practiceMapper.deleteAllStudent(pid);
        for (Student s : students) {
            practiceMapper.insertStudent(s.getId(), pid);
        }
    }

    public List<Practice> getPracticeByStudentInfoAndPracticeInfo(String studentInfo, String practiceInfo) {
        List<Practice> sList = practiceMapper.getPracticeByStudentInfo(studentInfo);
        List<Practice> aList = practiceMapper.getPracticeByPracticeInfo(practiceInfo);
        List<Practice> result = new ArrayList<>();
        for (Practice p : aList) {
            for (Practice pp : sList) {
                if (pp.getId() != null && Objects.equals(p.getId(), pp.getId())) {
                    result.add(p);
                    break;
                }
            }
        }
        return result;
    }

    public List<Practice> getPracticeByInfo(String info) {
        List<Practice> sList = practiceMapper.getPracticeByStudentInfo(info);
        List<Practice> pList = practiceMapper.getPracticeByPracticeInfo(info);
        Set<Integer> resultId = new HashSet<>();
        List<Practice> result = new ArrayList<>();
        for (Practice p : pList) {
            resultId.add(p.getId());
        }
        for (Practice p : sList) {
            if(p.getId()!=null) {
                resultId.add(p.getId());
            }
        }
        for(Integer id : resultId) {
            result.add(practiceMapper.getPracticeById(id).get(0));
        }
        return result;
    }
}
