package com.team9.project.service;

import com.team9.project.models.Activity;
import com.team9.project.models.Student;
import com.team9.project.payload.request.DataRequest;
import com.team9.project.payload.response.DataResponse;
import com.team9.project.repository.mapper.ActivityMapper;
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
public class ActivityService {
    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private StudentMapper studentMapper;

    private String paraKeeper = "";

    public List<Map<String, String>> getActivityMapList(String para, @Nullable String para2, int mode) {
        List<Map<String, String>> dataList = new ArrayList<>();
        List<Activity> aList;
        if ("".equals(para) || para == null) {
            aList = activityMapper.findAll();
        } else if (mode == 1) {
            aList = getActivityByInfo(para);
        } else if (mode == 2) {
            aList = getActivityByStudentInfo(para);
        } else {
            aList = getActivityByStudentInfoAndActivityInfo(para, para2);
        }

        if (aList == null || aList.size() == 0) {
            return dataList;
        }
        Activity a;
        Map<String, String> m;
        String categoryParas, initiatorParas;
        for (Activity activity : aList) {
            a = activity;
            List<Student> students = a.getStudents();
            StringBuilder studentsStr = new StringBuilder();
            for (int j = 0; j < students.size(); j++) {
                studentsStr.append(students.get(j).getName());
                if (j != students.size() - 1) {
                    studentsStr.append("，");
                }
            }
            m = new HashMap<>();
            m.put("id", String.valueOf(a.getId()));
            m.put("activityName", a.getActivityName());
            m.put("category", a.getCategory());
            categoryParas = "model=activity&activityInfo=" + a.getCategory();
            m.put("categoryParas", categoryParas);
            m.put("initiator", a.getInitiator());
            initiatorParas = "model=activity&activityInfo=" + a.getInitiator();
            m.put("initiatorParas", initiatorParas);
            m.put("startTime", TimeUtil.parseStr(a.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
            m.put("endTime", TimeUtil.parseStr(a.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
            m.put("students", studentsStr.toString());
            dataList.add(m);
        }
        return dataList;
    }

    public DataResponse activityInit(@Valid @RequestBody DataRequest dataRequest) {
        List<Map<String, String>> dataList;
        String studentId = dataRequest.getString("studentId");
        String activityInfo = dataRequest.getString("activityInfo");

        if (studentId == null) {
            dataList = getActivityMapList(activityInfo, null, 1);
        } else {
            dataList = getActivityMapList(studentId, null, 2);
            paraKeeper = studentId;
        }
        return CommonMethod.getReturnData(dataList);
    }

    public DataResponse activityQuery(@Valid @RequestBody DataRequest dataRequest) {
        String info = dataRequest.getString("info");
        List<Map<String, String>> dataList;
        if ("".equals(paraKeeper)) {
            dataList = getActivityMapList(info, null, 1);
        } else {
            dataList = getActivityMapList(paraKeeper, info, 3);
        }

        return CommonMethod.getReturnData(dataList);
    }

    public DataResponse activityEditInit(@Valid @RequestBody DataRequest dataRequest) {
        String studentId = paraKeeper;
        Integer id = dataRequest.getInteger("id");
        Activity a = null;
        List<Activity> op;
        if (id != null) {
            op = activityMapper.getActivityById(id);
            if (op != null && op.size() != 0) {
                a = op.get(0);
            }
        }
        Map<String, String> form = new HashMap<>();
        if (a != null) {
            List<Student> students = a.getStudents();
            StringBuilder studentIdStr = new StringBuilder();
            for (int j = 0; j < students.size(); j++) {
                studentIdStr.append(students.get(j).getStudentId());
                if (j != students.size() - 1) {
                    studentIdStr.append(",");
                }
            }
            form.put("id", String.valueOf(a.getId()));
            form.put("activityName", a.getActivityName());
            form.put("category", a.getCategory());
            form.put("initiator", a.getInitiator());
            form.put("startTime", TimeUtil.parseStr(a.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
            form.put("endTime", TimeUtil.parseStr(a.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
            form.put("students", studentIdStr.toString());
        } else if (studentId != null) {
            form.put("students", studentId);
        }
        return CommonMethod.getReturnData(form);
    }

    public synchronized Integer getNewActivityId() {
        Integer id = activityMapper.getMaxId();
        if (id == null) {
            id = 1;
        } else {
            id = id + 1;
        }
        return id;
    }

    public DataResponse activityEditSubmit(@Valid @RequestBody DataRequest dataRequest) throws ParseException {
        boolean flag = false;
        Map<String, String> form = dataRequest.getMap("form");
        Integer id = CommonMethod.getInteger(form, "id");
        String activityName = CommonMethod.getString(form, "activityName");
        String category = CommonMethod.getString(form, "category");
        String initiator = CommonMethod.getString(form, "initiator");
        String startTime = CommonMethod.getString(form, "startTime");
        String endTime = CommonMethod.getString(form, "endTime");
        String studentsIdStr = CommonMethod.getString(form, "students");
        List<Student> students = new ArrayList<>();
        if (!"".equals(studentsIdStr)) {
            String[] studentsId = studentsIdStr.split(",");
            for (String s : studentsId) {
                students.add(studentMapper.getStudentByStudentInfo(s).get(0));
            }
        }
        Activity a = null;
        List<Activity> op;
        if (id != null) {
            op = activityMapper.getActivityById(id);
            if (op != null && op.size() != 0) {
                a = op.get(0);
            }
        }
        if (a == null) {
            a = new Activity();
            id = getNewActivityId();
            a.setId(id);
            flag = true;
        }
        updateStudent(a.getId(), students);
        a.setActivityName(activityName);
        a.setCategory(category);
        a.setInitiator(initiator);
        a.setStartTime(TimeUtil.parseDateTime(startTime));
        a.setEndTime(TimeUtil.parseDateTime(endTime));
        a.setStudents(null);
        if (flag) {
            activityMapper.addActivity(a);
        } else {
            activityMapper.updateActivity(a);
        }

        return CommonMethod.getReturnData(a.getId());
    }

    public DataResponse activityDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");
        Activity a = null;
        List<Activity> op;
        if (id != null) {
            op = activityMapper.getActivityById(id);
            if (op != null && op.size() != 0) {
                a = op.get(0);
            }
        }
        if (a != null) {
            deleteActivityById(a.getId());
        }
        return CommonMethod.getReturnMessageOK();
    }

    public List<Activity> getActivityByStudentInfo(String studentInfo) {
        List<Activity> list = activityMapper.getActivityByStudentInfo(studentInfo);
        List<Activity> result = new ArrayList<>();
        for (Activity a : list) {
            if (a.getId() != null) {
                result.add(a);
            }
        }
        return result;
    }

    public void deleteActivityById(Integer id) {
        activityMapper.deleteActivityById(id);
        activityMapper.deleteAllStudent(id);
    }

    public void updateStudent(int aid, List<Student> students) {
        activityMapper.deleteAllStudent(aid);
        for (Student s : students) {
            activityMapper.insertStudent(s.getId(), aid);
        }
    }

    public List<Activity> getActivityByStudentInfoAndActivityInfo(String studentInfo, String activityInfo) {
        List<Activity> sList = activityMapper.getActivityByStudentInfo(studentInfo);
        List<Activity> aList = activityMapper.getActivityByActivityInfo(activityInfo);
        List<Activity> result = new ArrayList<>();
        for (Activity a : aList) {
            for (Activity aa : sList) {
                if (aa.getId() != null && Objects.equals(a.getId(), aa.getId())) {
                    result.add(a);
                    break;
                }
            }
        }
        return result;
    }

    public List<Activity> getActivityByInfo(String info) {
        List<Activity> sList = activityMapper.getActivityByStudentInfo(info);
        List<Activity> aList = activityMapper.getActivityByActivityInfo(info);
        Set<Integer> resultId = new HashSet<>();
        List<Activity> result = new ArrayList<>();
        for (Activity a : aList) {
            resultId.add(a.getId());
        }
        for (Activity a : sList) {
            if(a.getId()!=null) {
                resultId.add(a.getId());
            }
        }
        for(Integer id : resultId) {
            result.add(activityMapper.getActivityById(id).get(0));
        }
        return result;
    }

}
