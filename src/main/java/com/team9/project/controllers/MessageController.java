package com.team9.project.controllers;

import com.team9.project.models.Course;
import com.team9.project.models.Log;
import com.team9.project.models.Student;
import com.team9.project.payload.request.DataRequest;
import com.team9.project.payload.response.DataResponse;
import com.team9.project.repository.mapper.CourseMapper;
import com.team9.project.repository.mapper.LogMapper;
import com.team9.project.service.LogService;
import com.team9.project.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")
public class MessageController {
    @Autowired
    private LogMapper logMapper;
    @Autowired
    private LogService logService;
    @Autowired
    private CourseMapper courseMapper;

    @PostMapping("/messageInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse messageInit(@Valid @RequestBody DataRequest dataRequest) {
        String message = dataRequest.getString("message");
        Integer command = dataRequest.getInteger("command");
        String caller = dataRequest.getString("caller");
        Integer id = dataRequest.getInteger("id");
        Integer id2 = dataRequest.getInteger("id2");
        String callerStatus1 = dataRequest.getString("callerStatus1");
        String callerStatus2 = dataRequest.getString("callerStatus2");
        String callerStatus3 = dataRequest.getString("callerStatus3");
        String callerStatus4 = dataRequest.getString("callerStatus4");

        Log l;
//        command注释：
//        1：passLog
//        2：rejectLog
//        3：selectClass
//        4：withdrawClass
        switch (command) {
            case 1:
                l = logMapper.getLogById(id).get(0);
                logService.passLog(l);
                break;
            case 2:
                l = logMapper.getLogById(id).get(0);
                logService.rejectLog(l);
                break;
            case 3:
                boolean flag = true;
                Course c = courseMapper.getCourseById(id).get(0);
                for(Student s : c.getStudents()) {
                    if(Objects.equals(s.getId(), id2)) {
                        message="你已经选过这门课了，不能重复选择。";
                        flag = false;
                        break;
                    }
                }
                if(flag) {
                    courseMapper.selectCourse(id, id2);
                    message = "选课成功！";
                }
                break;
            case 4:
                courseMapper.withdrawCourse(id, id2);
                break;
            case 5:
                l = logMapper.getLogById(id).get(0);
                if("待审批".equals(l.getStatus())) {
                    logService.deleteLogById(id);
                    message = "撤回成功！";
                } else {
                    message = "该请假申请已审批，无法撤回。";
                }
            default:
                break;
        }

        Map<String, String> m = new HashMap<>();
        m.put("message", message);
        String backParas = "model=" + caller;
        if (callerStatus1 != null && callerStatus2!=null) {
           backParas += "&" + callerStatus1 + "=" + callerStatus2;
        }
        if (callerStatus3 != null && callerStatus4!=null && !"null".equals(callerStatus4)) {
           backParas += "&" + callerStatus3 + "=" + callerStatus4;
        }
        m.put("backParas", backParas);
        List<Map<String, String>> dataList = new ArrayList<>();
        dataList.add(m);
        return CommonMethod.getReturnData(dataList);
    }
}
