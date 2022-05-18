package com.team9.project.service;

import com.team9.project.models.Student;
import com.team9.project.payload.request.DataRequest;
import com.team9.project.payload.response.DataResponse;
import com.team9.project.repository.mapper.StudentMapper;
import com.team9.project.util.CommonMethod;
import com.team9.project.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.*;

@Service
public class MyInfoService {
    @Autowired
    private StudentMapper studentMapper;

    public DataResponse myInfoInit(@Valid @RequestBody DataRequest dataRequest) {
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
}
