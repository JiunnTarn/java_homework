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
import java.util.*;

@Service
public class FinderService {
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private CourseService courseService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private LogService logService;
    @Autowired
    private PracticeService practiceService;
    @Autowired
    private RewardService rewardService;
    @Autowired
    private ConsumeService consumeService;

    public DataResponse finderInit() {
        return CommonMethod.getReturnData(new ArrayList<>());
    }

    public DataResponse finderQuery(@Valid @RequestBody DataRequest dataRequest) {
        String info = dataRequest.getString("info");
        List<Map<String, String>> dataList = new ArrayList<>();
        Map<String, String> m;

        List<Student> students = studentMapper.getStudentByStudentInfo(info);
        List<Course> courses = courseService.getCourseByInfo(info);
        List<Activity> activities = activityService.getActivityByInfo(info);
        List<Log> logs = logService.getLogByInfo(info);
        List<Practice> practices = practiceService.getPracticeByInfo(info);
        List<Reward> rewards = rewardService.getRewardByInfo(info);
        List<Consume> consumes = consumeService.getConsumeByInfo(info);

        for (Student student : students) {
            m = new HashMap<>();
            m.put("model", "学生");
            m.put("detail", student.toString());
            m.put("checkParas", "model=student&studentInfo=" + student.getStudentId());
            dataList.add(m);
        }
        for (Course course : courses) {
            m = new HashMap<>();
            m.put("model", "课程");
            m.put("detail", course.toString());
            m.put("checkParas", "model=course&courseInfo=" + course.getCourseId());
            dataList.add(m);
        }
        for (Activity activity : activities) {
            m = new HashMap<>();
            m.put("model", "活动");
            m.put("detail", activity.toString());
            m.put("checkParas", "model=activity&activityInfo=" + activity.getActivityName());
            dataList.add(m);
        }
        for (Log log : logs) {
            m = new HashMap<>();
            m.put("model", "请假");
            m.put("detail", log.toString());
            m.put("checkParas", "model=log&studentInfo=" + log.getStudent().getStudentId());
            dataList.add(m);
        }
        for (Practice practice : practices) {
            m = new HashMap<>();
            m.put("model", "实践");
            m.put("detail", practice.toString());
            m.put("checkParas", "model=practice&practiceInfo=" + practice.getPracticeName());
            dataList.add(m);
        }
        for (Reward reward : rewards) {
            m = new HashMap<>();
            m.put("model", "荣誉");
            m.put("detail", reward.toString());
            m.put("checkParas", "model=reward&rewardInfo=" + reward.getRewardId());
            dataList.add(m);
        }
        for (Consume consume : consumes) {
            m = new HashMap<>();
            m.put("model", "消费");
            m.put("detail", consume.toString());
            m.put("checkParas", "model=consume&consumeInfo=" + consume.getReason());
            dataList.add(m);
        }

        return CommonMethod.getReturnData(dataList);
    }

}
