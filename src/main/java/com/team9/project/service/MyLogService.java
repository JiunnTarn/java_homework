package com.team9.project.service;

import com.team9.project.models.Log;
import com.team9.project.models.Student;
import com.team9.project.payload.request.DataRequest;
import com.team9.project.payload.response.DataResponse;
import com.team9.project.repository.mapper.LogMapper;
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
public class MyLogService {
    @Autowired
    private LogMapper logMapper;
    @Autowired
    private StudentMapper studentMapper;

    private String studentId;

    public List<Map<String, String>> getLogMapList(String para, @Nullable String para2, int mode) {
        List<Map<String, String>> dataList = new ArrayList<>();
        List<Log> lList;
        if ("".equals(para) || para == null) {
            lList = logMapper.findAll();
        } else if (mode == 1) {
            lList = getLogByInfo(para);
        } else if (mode == 2) {
            lList = getLogByStudentInfo(para);
        } else {
            lList = getLogByStudentInfoAndLogInfo(para, para2);
        }

        if (lList == null || lList.size() == 0) {
            return dataList;
        }
        Log l;
        Map<String, String> m;
        String withdrawParas, studentParas;
        for (Log log : lList) {
            l = log;
            Student student = l.getStudent();
            String studentsStr = "";
            if (student != null) {
                studentsStr = student.getName();
            }
            m = new HashMap<>();
            m.put("id", String.valueOf(l.getId()));
            m.put("student", studentsStr);
            studentParas = "model=student&para=" + l.getStudent().getStudentId();
            m.put("studentParas", studentParas);
            m.put("destination", l.getDestination());
            m.put("applyTime", TimeUtil.parseStr(l.getApplyTime(), "yyyy-MM-dd HH:mm:ss"));
            m.put("leaveTime", TimeUtil.parseStr(l.getLeaveTime(), "yyyy-MM-dd HH:mm:ss"));
            m.put("returnTime", TimeUtil.parseStr(l.getReturnTime(), "yyyy-MM-dd HH:mm:ss"));
            m.put("reason", l.getReason());
            m.put("status", l.getStatus());
            withdrawParas = "model=message&message=未知错误&command=5&caller=myLog&id=" + l.getId() +"&callerStatus1=studentId&callerStatus2=" + studentId + "&callerStatus3=logInfo&callerStatus4=" + para2;
            m.put("withdrawParas", withdrawParas);
            dataList.add(m);
        }
        return dataList;
    }

    public DataResponse myLogInit(@Valid @RequestBody DataRequest dataRequest) {
        List<Map<String, String>> dataList;
        studentId = dataRequest.getString("studentId");
        String info = dataRequest.getString("logInfo");

        if (info == null) {
            dataList = getLogMapList(studentId, null, 2);
        } else {
            dataList = getLogMapList(studentId, info, 3);
        }
        return CommonMethod.getReturnData(dataList);
    }

    public DataResponse myLogQuery(@Valid @RequestBody DataRequest dataRequest) {
        String info = dataRequest.getString("info");
        List<Map<String, String>> dataList = getLogMapList(studentId, info, 3);
        return CommonMethod.getReturnData(dataList);
    }

    public DataResponse myLogEditInit(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");
        Log l = null;
        List<Log> op;
        if (id != null) {
            op = logMapper.getLogById(id);
            if (op != null && op.size() != 0) {
                l = op.get(0);
            }
        }
        Map<String, String> form = new HashMap<>();
        if (l != null) {
            String studentIdStr = l.getStudent().getStudentId();
            form.put("id", String.valueOf(l.getId()));
            form.put("destination", l.getDestination());
            form.put("applyTime", TimeUtil.parseStr(l.getApplyTime(), "yyyy-MM-dd HH:mm:ss"));
            form.put("leaveTime", TimeUtil.parseStr(l.getLeaveTime(), "yyyy-MM-dd HH:mm:ss"));
            form.put("returnTime", TimeUtil.parseStr(l.getReturnTime(), "yyyy-MM-dd HH:mm:ss"));
            form.put("reason", l.getReason());
            form.put("student", studentIdStr);
        } else if (studentId != null) {
            form.put("student", studentId);
            form.put("applyTime", TimeUtil.parseStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
        }
        return CommonMethod.getReturnData(form);
    }

    public DataResponse myLogEditSubmit(@Valid @RequestBody DataRequest dataRequest) throws ParseException {
        boolean flag = false;
        Map<String, String> form = dataRequest.getMap("form");
        Integer id = CommonMethod.getInteger(form, "id");
        String studentIdStr = CommonMethod.getString(form, "student");
        String destination = CommonMethod.getString(form, "destination");
        Date applyTime = TimeUtil.parseDateTime(CommonMethod.getString(form, "applyTime"));
        Date leaveTime = TimeUtil.parseDateTime(CommonMethod.getString(form, "leaveTime"));
        Date returnTime = TimeUtil.parseDateTime(CommonMethod.getString(form, "returnTime"));
        String reason = CommonMethod.getString(form, "reason");
        Student student = studentMapper.getStudentByStudentInfo(studentIdStr).get(0);

        Log l = null;
        List<Log> op;
        if (id != null) {
            op = logMapper.getLogById(id);
            if (op != null && op.size() != 0) {
                l = op.get(0);
            }
        }
        if (l == null) {
            l = new Log();
            id = getNewLogId();
            l.setId(id);
            flag = true;
        }
        updateStudent(l.getId(), student);
        l.setDestination(destination);
        l.setApplyTime(applyTime);
        l.setLeaveTime(leaveTime);
        l.setReturnTime(returnTime);
        l.setReason(reason);
        l.setStatus("待审批");
        l.setStudent(null);
        if (flag) {
            logMapper.addLog(l);
        } else {
            logMapper.updateLog(l);
        }
        return CommonMethod.getReturnData(l.getId());
    }

    public synchronized Integer getNewLogId() {
        Integer id = logMapper.getMaxId();
        if (id == null) {
            id = 1;
        } else {
            id = id + 1;
        }
        return id;
    }

    public List<Log> getLogByStudentInfo(String studentInfo) {
        List<Log> list = logMapper.getLogByStudentInfo(studentInfo);
        List<Log> result = new ArrayList<>();
        for (Log l : list) {
            if (l.getId() != null) {
                result.add(l);
            }
        }
        return result;
    }

    public List<Log> getLogByStudentInfoAndLogInfo(String studentInfo, String practiceInfo) {
        List<Log> sList = logMapper.getLogByStudentInfo(studentInfo);
        List<Log> lList = logMapper.getLogByLogInfo(practiceInfo);
        List<Log> result = new ArrayList<>();
        for (Log l : lList) {
            for (Log ll : sList) {
                if (ll.getId() != null && Objects.equals(l.getId(), ll.getId())) {
                    result.add(l);
                    break;
                }
            }
        }
        return result;
    }

    public void updateStudent(int lid, Student student) {
        logMapper.deleteAllStudent(lid);
        logMapper.insertStudent(student.getId(), lid);
    }
    public List<Log> getLogByInfo(String info) {
        List<Log> sList = logMapper.getLogByStudentInfo(info);
        List<Log> lList = logMapper.getLogByLogInfo(info);
        Set<Integer> resultId = new HashSet<>();
        List<Log> result = new ArrayList<>();
        for (Log r : lList) {
            resultId.add(r.getId());
        }
        for (Log r : sList) {
            if(r.getId()!=null) {
                resultId.add(r.getId());
            }
        }
        for(Integer id : resultId) {
            result.add(logMapper.getLogById(id).get(0));
        }
        return result;
    }
}
