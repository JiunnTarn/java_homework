package com.team9.project.service;

import com.team9.project.models.Consume;
import com.team9.project.models.Student;
import com.team9.project.payload.request.DataRequest;
import com.team9.project.payload.response.DataResponse;
import com.team9.project.repository.mapper.ConsumeMapper;
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
public class ConsumeService {
    @Autowired
    private ConsumeMapper consumeMapper;
    @Autowired
    private StudentMapper studentMapper;

    private String paraKeeper = "";

    public List<Map<String, String>> getConsumeMapList(String para, @Nullable String para2, int mode) {
        List<Map<String, String>> dataList = new ArrayList<>();
        List<Consume> cList;
        if ("".equals(para) || para == null) {
            cList = consumeMapper.findAll();
        } else if (mode == 1) {
            cList = getConsumeByInfo(para);
        } else if (mode == 2) {
            cList = getConsumeByStudentInfo(para);
        } else {
            cList = getConsumeByStudentInfoAndConsumeInfo(para, para2);
        }

        if (cList == null || cList.size() == 0) {
            return dataList;
        }
        Consume c;
        Map<String, String> m;
        String typeParas, studentParas;
        for (Consume consume : cList) {
            c = consume;
            String studentStr = c.getStudent().getName();
            m = new HashMap<>();
            m.put("id", String.valueOf(c.getId()));
            m.put("student", studentStr);
            studentParas = "model=student&studentInfo=" + c.getStudent().getStudentId();
            m.put("studentParas", studentParas);
            m.put("type", c.getType());
            typeParas = "model=consume&consumeInfo=" + c.getType();
            m.put("typeParas", typeParas);
            m.put("amount", String.valueOf(c.getAmount()));
            m.put("time", TimeUtil.parseStr(c.getTime(), "yyyy-MM-dd HH:mm:ss"));
            m.put("reason", c.getReason());
            dataList.add(m);
        }
        return dataList;
    }

    public DataResponse consumeInit(@Valid @RequestBody DataRequest dataRequest) {
        List<Map<String, String>> dataList;
        String studentId = dataRequest.getString("studentId");
        String consumeInfo = dataRequest.getString("consumeInfo");

        if (studentId == null) {
            dataList = getConsumeMapList(consumeInfo, null, 1);
        } else {
            dataList = getConsumeMapList(studentId, null, 2);
            paraKeeper = studentId;
        }
        return CommonMethod.getReturnData(dataList);
    }

    public DataResponse consumeQuery(@Valid @RequestBody DataRequest dataRequest) {
        String info = dataRequest.getString("info");
        List<Map<String, String>> dataList;
        if ("".equals(paraKeeper)) {
            dataList = getConsumeMapList(info, null, 1);
        } else {
            dataList = getConsumeMapList(paraKeeper, info, 3);
        }

        return CommonMethod.getReturnData(dataList);
    }

    public DataResponse consumeEditInit(@Valid @RequestBody DataRequest dataRequest) {
        String studentId = paraKeeper;
        Integer id = dataRequest.getInteger("id");
        Consume c = null;
        List<Consume> op;
        if (id != null) {
            op = consumeMapper.getConsumeById(id);
            if (op != null && op.size() != 0) {
                c = op.get(0);
            }
        }
        Map<String, String> form = new HashMap<>();
        if (c != null) {
            String studentIdStr = c.getStudent().getStudentId();
            form.put("id", String.valueOf(c.getId()));
            form.put("student", studentIdStr);
            form.put("type", c.getType());
            form.put("amount", String.valueOf(c.getAmount()));
            form.put("time", TimeUtil.parseStr(c.getTime(), "yyyy-MM-dd HH:mm:ss"));
            form.put("reason", c.getReason());
        } else if (studentId != null) {
            form.put("student", studentId);
            form.put("time", TimeUtil.parseStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
            form.put("type", "支出");
        }
        return CommonMethod.getReturnData(form);
    }

    public synchronized Integer getNewConsumeId() {
        Integer id = consumeMapper.getMaxId();
        if (id == null) {
            id = 1;
        } else {
            id = id + 1;
        }
        return id;
    }

    public DataResponse consumeEditSubmit(@Valid @RequestBody DataRequest dataRequest) throws ParseException {
        boolean flag = false;
        Map<String, String> form = dataRequest.getMap("form");
        Integer id = CommonMethod.getInteger(form, "id");
        String type = CommonMethod.getString(form, "type");
        String reason = CommonMethod.getString(form, "reason");
        Integer amount = CommonMethod.getInteger(form, "amount");
        String time = CommonMethod.getString(form, "time");
        String studentIdStr = CommonMethod.getString(form, "student");
        Student student = new Student();
        if (studentIdStr != null && !"".equals(studentIdStr)) {
            student = studentMapper.getStudentByStudentInfo(studentIdStr).get(0);
        }
        Consume c = null;
        List<Consume> op;
        if (id != null) {
            op = consumeMapper.getConsumeById(id);
            if (op != null && op.size() != 0) {
                c = op.get(0);
            }
        }
        if (c == null) {
            c = new Consume();
            id = getNewConsumeId();
            c.setId(id);
            flag = true;
        }
        updateStudent(c.getId(), student);
        c.setType(type);
        c.setAmount(amount);
        c.setReason(reason);
        c.setTime(TimeUtil.parseDateTime(time));
        c.setStudent(null);
        if (flag) {
            consumeMapper.addConsume(c);
        } else {
            consumeMapper.updateConsume(c);
        }

        return CommonMethod.getReturnData(c.getId());
    }

    public DataResponse consumeDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");
        Consume c = null;
        List<Consume> op;
        if (id != null) {
            op = consumeMapper.getConsumeById(id);
            if (op != null && op.size() != 0) {
                c = op.get(0);
            }
        }
        if (c != null) {
            deleteConsumeById(c.getId());
        }
        return CommonMethod.getReturnMessageOK();
    }

    public List<Consume> getConsumeByStudentInfo(String studentInfo) {
        List<Consume> list = consumeMapper.getConsumeByStudentInfo(studentInfo);
        List<Consume> result = new ArrayList<>();
        for (Consume c : list) {
            if (c.getId() != null) {
                result.add(c);
            }
        }
        return result;
    }

    public void deleteConsumeById(Integer id) {
        consumeMapper.deleteConsumeById(id);
        consumeMapper.deleteAllStudent(id);
    }

    public void updateStudent(int aid, Student student) {
        consumeMapper.deleteAllStudent(aid);
        consumeMapper.insertStudent(student.getId(), aid);
    }

    public List<Consume> getConsumeByStudentInfoAndConsumeInfo(String studentInfo, String consumeInfo) {
        List<Consume> sList = consumeMapper.getConsumeByStudentInfo(studentInfo);
        List<Consume> aList = consumeMapper.getConsumeByConsumeInfo(consumeInfo);
        List<Consume> result = new ArrayList<>();
        for (Consume c : aList) {
            for (Consume cc : sList) {
                if (cc.getId() != null && Objects.equals(c.getId(), cc.getId())) {
                    result.add(c);
                    break;
                }
            }
        }
        return result;
    }

    public List<Consume> getConsumeByInfo(String info) {
        List<Consume> sList = consumeMapper.getConsumeByStudentInfo(info);
        List<Consume> cList = consumeMapper.getConsumeByConsumeInfo(info);
        Set<Integer> resultId = new HashSet<>();
        List<Consume> result = new ArrayList<>();
        for (Consume c : cList) {
            resultId.add(c.getId());
        }
        for (Consume c : sList) {
            if (c.getId() != null) {
                resultId.add(c.getId());
            }
        }
        for (Integer id : resultId) {
            result.add(consumeMapper.getConsumeById(id).get(0));
        }
        return result;
    }

}
