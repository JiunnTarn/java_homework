package com.team9.project.service;

import com.team9.project.models.Reward;
import com.team9.project.models.Student;
import com.team9.project.payload.request.DataRequest;
import com.team9.project.payload.response.DataResponse;
import com.team9.project.repository.mapper.RewardMapper;
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
public class RewardService {
    @Autowired
    private RewardMapper rewardMapper;
    @Autowired
    private StudentMapper studentMapper;

    private String paraKeeper = "";

    private final String[] creditStr = {"", "学院级", "校级", "省三等奖", "省二等奖", "省一等奖", "国家三等奖", "国家二等奖", "国家一等奖", "世界级"};

    public List<Map<String, String>> getRewardMapList(String para, @Nullable String para2, int mode) {
        List<Map<String, String>> dataList = new ArrayList<>();
        List<Reward> rList;
        if ("".equals(para) || para == null) {
            rList = rewardMapper.findAll();
        } else if (mode == 1) {
            rList = getRewardByInfo(para);
        } else if (mode == 2) {
            rList = getRewardByStudentInfo(para);
        } else {
            rList = getRewardByStudentInfoAndRewardInfo(para, para2);
        }

        if (rList == null || rList.size() == 0) {
            return dataList;
        }
        Reward r;
        Map<String, String> m;
        String studentParas;
        for (Reward reward : rList) {
            r = reward;
            Student student = r.getStudent();

            m = new HashMap<>();
            m.put("id", String.valueOf(r.getId()));
            m.put("rewardTime", TimeUtil.parseStr(r.getRewardTime(), "yyyy-MM-dd HH:mm:ss"));
            m.put("rewardName", r.getRewardName());
            m.put("rewardId", r.getRewardId());
            m.put("credit", r.getCredit()!=null ? creditStr[r.getCredit()] : "");
            m.put("student", student.getName());
            studentParas = "model=student&studentInfo=" + student.getStudentId();
            m.put("studentParas", studentParas);
            dataList.add(m);
        }
        return dataList;
    }

    public DataResponse rewardInit(@Valid @RequestBody DataRequest dataRequest) {
        List<Map<String, String>> dataList;
        String studentId = dataRequest.getString("studentId");
        String rewardInfo = dataRequest.getString("rewardInfo");

        if (studentId == null) {
            dataList = getRewardMapList(rewardInfo, null, 1);
        } else {
            dataList = getRewardMapList(studentId, null, 2);
            paraKeeper = studentId;
        }
        return CommonMethod.getReturnData(dataList);
    }

    public DataResponse rewardQuery(@Valid @RequestBody DataRequest dataRequest) {
        String info = dataRequest.getString("info");
        List<Map<String, String>> dataList;
        if ("".equals(paraKeeper)) {
            dataList = getRewardMapList(info, null, 1);
        } else {
            dataList = getRewardMapList(paraKeeper, info, 3);
        }
        return CommonMethod.getReturnData(dataList);
    }

    public DataResponse rewardEditInit(@Valid @RequestBody DataRequest dataRequest) {
        String studentId = paraKeeper;
        if (dataRequest.getString("studentId") != null) {
            studentId = dataRequest.getString("studentId");
        }
        Integer id = dataRequest.getInteger("id");
        Reward r = null;
        List<Reward> op;
        if (id != null) {
            op = rewardMapper.getRewardById(id);
            if (op != null && op.size() != 0) {
                r = op.get(0);
            }
        }
        Map<String, String> form = new HashMap<>();
        if (r != null) {
            Student student = r.getStudent();
            String studentIdStr = student.getStudentId();
            form.put("id", String.valueOf(r.getId()));
            form.put("rewardName", r.getRewardName());
            form.put("rewardId", r.getRewardId());
            form.put("credit", r.getCredit()!=null ? creditStr[r.getCredit()] : "");
            form.put("rewardTime", TimeUtil.parseStr(r.getRewardTime(), "yyyy-MM-dd HH:mm:ss"));
            form.put("student", studentIdStr);
        } else if (studentId != null) {
            form.put("student", studentId);
        }
        return CommonMethod.getReturnData(form);
    }

    public synchronized Integer getNewRewardId() {
        Integer id = rewardMapper.getMaxId();
        if (id == null) {
            id = 1;
        } else {
            id = id + 1;
        }
        return id;
    }

    public DataResponse rewardEditSubmit(@Valid @RequestBody DataRequest dataRequest) throws ParseException {
        boolean flag = false;
        Map<String, String> form = dataRequest.getMap("form");
        Integer id = CommonMethod.getInteger(form, "id");
        String rewardName = CommonMethod.getString(form, "rewardName");
        String rewardId = CommonMethod.getString(form, "rewardId");
        Integer credit = CommonMethod.getInteger(form, "credit");
        String rewardTime = CommonMethod.getString(form, "rewardTime");
        String studentsIdStr = CommonMethod.getString(form, "student");
        Student student = studentMapper.getStudentByStudentInfo(studentsIdStr).get(0);
        Reward r = null;
        List<Reward> op;
        if (id != null) {
            op = rewardMapper.getRewardById(id);
            if (op != null && op.size() != 0) {
                r = op.get(0);
            }
        }
        if (r == null) {
            r = new Reward();
            id = getNewRewardId();
            r.setId(id);
            flag = true;
        }
        updateStudent(r.getId(), student);
        r.setRewardName(rewardName);
        r.setRewardId(rewardId);
        r.setCredit(credit);
        r.setRewardTime(TimeUtil.parseDateTime(rewardTime));
        r.setStudent(null);
        if (flag) {
            rewardMapper.addReward(r);
        } else {
            rewardMapper.updateReward(r);
        }
        return CommonMethod.getReturnData(r.getId());
    }

    public DataResponse rewardDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");
        Reward a = null;
        List<Reward> op;
        if (id != null) {
            op = rewardMapper.getRewardById(id);
            if (op != null && op.size() != 0) {
                a = op.get(0);
            }
        }
        if (a != null) {
            deleteReward(a.getId());
        }
        return CommonMethod.getReturnMessageOK();
    }

    public List<Reward> getRewardByStudentInfo(String studentInfo) {
        List<Reward> list = rewardMapper.getRewardByStudentInfo(studentInfo);
        List<Reward> result = new ArrayList<>();
        for (Reward r : list) {
            if (r.getId() != null) {
                result.add(r);
            }
        }
        return result;
    }

    public void deleteReward(Integer id) {
        rewardMapper.deleteReward(id);
        rewardMapper.deleteAllStudent(id);
    }

    public void updateStudent(int rid, Student s) {
        rewardMapper.deleteAllStudent(rid);
        rewardMapper.insertStudent(s.getId(), rid);
    }

    public List<Reward> getRewardByStudentInfoAndRewardInfo(String studentInfo, String practiceInfo) {
        List<Reward> sList = rewardMapper.getRewardByStudentInfo(studentInfo);
        List<Reward> aList = rewardMapper.getRewardByRewardInfo(practiceInfo);
        List<Reward> result = new ArrayList<>();
        for (Reward r : aList) {
            for (Reward rr : sList) {
                if (rr.getId() != null && Objects.equals(r.getId(), rr.getId())) {
                    result.add(r);
                    break;
                }
            }
        }
        return result;
    }

    public List<Reward> getRewardByInfo(String info) {
        List<Reward> sList = rewardMapper.getRewardByStudentInfo(info);
        List<Reward> rList = rewardMapper.getRewardByRewardInfo(info);
        Set<Integer> resultId = new HashSet<>();
        List<Reward> result = new ArrayList<>();
        for (Reward r : rList) {
            resultId.add(r.getId());
        }
        for (Reward r : sList) {
            if(r.getId()!=null) {
                resultId.add(r.getId());
            }
        }
        for(Integer id : resultId) {
            result.add(rewardMapper.getRewardById(id).get(0));
        }
        return result;
    }
}
