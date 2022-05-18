package com.team9.project.service;

import com.team9.project.models.*;
import com.team9.project.payload.request.DataRequest;
import com.team9.project.payload.response.DataResponse;
import com.team9.project.repository.mapper.*;
import com.team9.project.util.CommonMethod;
import com.team9.project.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.*;

@Service
public class StudentService {
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private ScoreMapper scoreMapper;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private RewardService rewardService;
    @Autowired
    private PracticeService practiceService;
    @Autowired
    private ConsumeService consumeService;
    @Autowired
    private LogService logService;

    public List<Map<String, String>> getStudentMapList(String studentInfo) {
        List<Map<String, String>> dataList = new ArrayList<>();
        List<Student> sList;
        if ("".equals(studentInfo) || studentInfo == null) {
            sList = studentMapper.findAll();
        } else {
            sList = studentMapper.getStudentByStudentInfo(studentInfo);
        }

        if (sList == null || sList.size() == 0) {
            return dataList;
        }
        Map<String, String> m;
        String myInfoParas, myCourseParas, courseSelectParas, myScoreParas, myPracticeParas, myLogParas, consumeParas, myActivityParas, myRewardParas, evaluationParas, myIntroduceParas;
        for (Student student : sList) {
            m = new HashMap<>();
            m.put("id", String.valueOf(student.getId()));
            m.put("name", student.getName());
            m.put("studentId", student.getStudentId());
            m.put("sex", student.getSex());

            myInfoParas = "model=myInfo&id=" + student.getId();
            myCourseParas = "model=myCourse&studentId=" + student.getStudentId();
            myScoreParas = "model=myScore&studentId=" + student.getStudentId();
            myPracticeParas = "model=practice&studentId=" + student.getStudentId();
            myLogParas = "model=myLog&studentId=" + student.getStudentId();
            consumeParas = "model=consume&studentId=" + student.getStudentId();
            myActivityParas = "model=activity&studentId=" + student.getStudentId();
            myRewardParas = "model=reward&studentId=" + student.getStudentId();
            myIntroduceParas = "model=introduce&studentId=" + student.getStudentId();
            evaluationParas = "model=evaluation&id=" + student.getId();
            courseSelectParas = "model=courseSelect&studentId=" + student.getStudentId();

            m.put("myInfo", "我的基础信息");
            m.put("myCourse", "我的课程");
            m.put("courseSelect", "进入选课系统");
            m.put("myScore", "我的成绩");
            m.put("myPractice", "我的实践活动信息");
            m.put("myLog", "我的请假信息");
            m.put("consume", "我的消费信息");
            m.put("myActivity", "我的活动信息");
            m.put("myReward", "我的荣誉信息");
            m.put("evaluation", "我的综合评价");
            m.put("myIntroduce", "生成简历");

            m.put("myInfoParas", myInfoParas);
            m.put("myCourseParas", myCourseParas);
            m.put("courseSelectParas", courseSelectParas);
            m.put("myScoreParas", myScoreParas);
            m.put("myPracticeParas", myPracticeParas);
            m.put("myLogParas", myLogParas);
            m.put("consumeParas", consumeParas);
            m.put("myActivityParas", myActivityParas);
            m.put("myRewardParas", myRewardParas);
            m.put("evaluationParas", evaluationParas);
            m.put("myIntroduceParas", myIntroduceParas);

            dataList.add(m);
        }
        return dataList;
    }

    public DataResponse studentInit(@Valid @RequestBody DataRequest dataRequest) {
        String studentInfo = dataRequest.getString("studentInfo");
        List<Map<String, String>> dataList = getStudentMapList(studentInfo);
        return CommonMethod.getReturnData(dataList);
    }

    public DataResponse studentQuery(@Valid @RequestBody DataRequest dataRequest) {
        String studentInfo = dataRequest.getString("studentInfo");
        List<Map<String, String>> dataList = getStudentMapList(studentInfo);
        return CommonMethod.getReturnData(dataList);
    }

    public DataResponse studentEditInit(@Valid @RequestBody DataRequest dataRequest) {
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
        if (s != null) {
            form.put("id", String.valueOf(s.getId()));
            form.put("name", s.getName());
            form.put("studentId", s.getStudentId());
            form.put("citizenId", s.getCitizenId());
            form.put("sex", s.getSex());
            form.put("age", String.valueOf(s.getAge()));
            form.put("dept", s.getDept());
            form.put("birthday", TimeUtil.parseStr(s.getBirthday(), "yyyy-MM-dd HH:mm:ss"));
            form.put("phone", s.getPhone());
            form.put("hometown", s.getHometown());
            form.put("highSchool", s.getHighSchool());
            form.put("politicalStatus", s.getPoliticalStatus());
            form.put("classPosition", s.getClassPosition());
        }
        return CommonMethod.getReturnData(form);
    }

    public synchronized Integer getNewStudentId() {
        Integer id = studentMapper.getMaxId();
        if (id == null) {
            id = 1;
        } else {
            id = id + 1;
        }
        return id;
    }

    public DataResponse studentEditSubmit(@Valid @RequestBody DataRequest dataRequest) {
        boolean flag = false;
        Map<String, String> form = dataRequest.getMap("form");
        Integer id = CommonMethod.getInteger(form, "id");
        String name = CommonMethod.getString(form, "name");
        String studentId = CommonMethod.getString(form, "studentId");
        String citizenId = CommonMethod.getString(form, "citizenId");
        String sex = CommonMethod.getString(form, "sex");
        Integer age = CommonMethod.getInteger(form, "age");
        String dept = CommonMethod.getString(form, "dept");
        Date birthday = CommonMethod.getDate(form, "birthday");
        String phone = CommonMethod.getString(form, "phone");
        String hometown = CommonMethod.getString(form, "hometown");
        String highSchool = CommonMethod.getString(form, "highSchool");
        String politicalStatus = CommonMethod.getString(form, "politicalStatus");
        String classPosition = CommonMethod.getString(form, "classPosition");
        Student s = null;
        List<Student> op;
        if (id != null) {
            op = studentMapper.getStudentById(id);
            if (op != null && op.size() != 0) {
                s = op.get(0);
            }
        }
        if (s == null) {
            s = new Student();
            id = getNewStudentId();
            s.setId(id);
            flag = true;
        }
        s.setName(name);
        s.setStudentId(studentId);
        s.setCitizenId(citizenId);
        s.setSex(sex);
        s.setAge(age);
        s.setDept(dept);
        s.setBirthday(birthday);
        s.setPhone(phone);
        s.setHometown(hometown);
        s.setHighSchool(highSchool);
        s.setPoliticalStatus(politicalStatus);
        s.setClassPosition(classPosition);
        if (flag) {
            studentMapper.addStudent(s);
        } else {
            studentMapper.updateStudent(s);
        }

        return CommonMethod.getReturnData(s.getId());
    }

    public DataResponse studentDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");
        Student s = null;
        List<Student> op;
        if (id != null) {
            op = studentMapper.getStudentById(id);
            if (op != null && op.size() != 0) {
                s = op.get(0);
            }
        }
        if (s != null) {
            List<Activity> activities = activityService.getActivityByStudentInfo(s.getStudentId());
            for (Activity a : activities) {
                activityService.deleteActivityById(a.getId());
            }

            List<Reward> rewards = rewardService.getRewardByStudentInfo(s.getStudentId());
            for (Reward r : rewards) {
                rewardService.deleteReward(r.getId());
            }

            List<Practice> practices = practiceService.getPracticeByStudentInfo(s.getStudentId());
            for (Practice p : practices) {
                practiceService.deletePracticeById(p.getId());
            }

            List<Consume> consumes = consumeService.getConsumeByStudentInfo(s.getStudentId());
            for (Consume c : consumes) {
                consumeService.deleteConsumeById(c.getId());
            }

            List<Log> logs = logService.getLogByInfo(s.getStudentId());
            for (Log l : logs) {
                logService.deleteLogById(l.getId());
            }
            studentMapper.deleteStudentById(s.getId());
            studentMapper.deleteStudentCourseById(s.getId());
            scoreMapper.deleteScoreByStudentId(s.getId());

        }
        return CommonMethod.getReturnMessageOK();
    }

}
