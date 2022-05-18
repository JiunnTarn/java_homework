package com.team9.project.service;

import com.team9.project.models.Log;
import com.team9.project.models.Student;
import com.team9.project.payload.request.DataRequest;
import com.team9.project.payload.response.DataResponse;
import java.text.ParseException;
import java.util.*;
import com.team9.project.repository.mapper.LogMapper;
import com.team9.project.repository.mapper.StudentMapper;
import com.team9.project.util.CommonMethod;
import com.team9.project.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

@Service
public class LogService {
    @Autowired
    private LogMapper logMapper;
    @Autowired
    private StudentMapper studentMapper;

    public List<Map<String, String>> getLogMapList(String info) {
        List<Map<String, String>> dataList = new ArrayList<>();
        List<Log> lList;
        if ("".equals(info) || info == null) {
            lList = logMapper.findAll();
        } else {
            lList = getLogByInfo(info);
        }
        if (lList == null || lList.size() == 0) {
            return dataList;
        }
        Log l;
        Map<String, String> m;
        String passParas, rejectParas, studentParas;
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
            studentParas = "model=student&info=" + l.getStudent().getStudentId();
            m.put("studentParas", studentParas);
            m.put("destination", l.getDestination());
            m.put("applyTime", TimeUtil.parseStr(l.getApplyTime(), "yyyy-MM-dd HH:mm:ss"));
            m.put("leaveTime", TimeUtil.parseStr(l.getLeaveTime(), "yyyy-MM-dd HH:mm:ss"));
            m.put("returnTime", TimeUtil.parseStr(l.getReturnTime(), "yyyy-MM-dd HH:mm:ss"));
            m.put("reason", l.getReason());
            m.put("status", l.getStatus());
            passParas = "model=message&message=已通过&command=1&caller=log&id=" + l.getId();
            if (info != null) {
                passParas += "&callerStatus1=info&callerStatus2=" + info;
            }
            m.put("passParas", passParas);
            rejectParas = "model=message&message=已拒绝&command=2&caller=log&id=" + l.getId();
            if (info != null) {
                rejectParas += "&callerStatus1=info&callerStatus2=" + info;
            }
            m.put("rejectParas", rejectParas);
            dataList.add(m);
        }
        return dataList;
    }

    public DataResponse logInit(@Valid @RequestBody DataRequest dataRequest) {
        String studentInfo = dataRequest.getString("studentInfo");
        List<Map<String, String>> dataList = getLogMapList(studentInfo);
        return CommonMethod.getReturnData(dataList);
    }

    public DataResponse logQuery(@Valid @RequestBody DataRequest dataRequest) {
        String info = dataRequest.getString("info");
        List<Map<String, String>> dataList = getLogMapList(info);
        return CommonMethod.getReturnData(dataList);
    }

    public DataResponse logEditInit(@Valid @RequestBody DataRequest dataRequest) {
        String studentId = dataRequest.getString("studentId");
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
        }
        return CommonMethod.getReturnData(form);
    }

    public DataResponse logEditSubmit(@Valid @RequestBody DataRequest dataRequest) throws ParseException {
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

    public DataResponse logDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");
        if (id != null) {
            deleteLogById(id);
        }
        return CommonMethod.getReturnMessageOK();
    }

    public void deleteLogById(Integer id) {
        this.logMapper.deleteLogById(id);
        this.logMapper.deleteAllStudent(id);
    }

    public void passLog(Log log) {
        log.setStatus("已通过");
        logMapper.updateLog(log);
    }

    public void rejectLog(Log log) {
        log.setStatus("已拒绝");
        logMapper.updateLog(log);
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
