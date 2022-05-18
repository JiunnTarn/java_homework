package com.team9.project.repository.mapper;

import com.team9.project.models.Log;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogMapper {
    List<Log> getLogById(Integer id);
    List<Log> getLogByLogInfo(String logInfo);
    List<Log> getLogByStudentInfo(String studentInfo);
    List<Log> findAll();
    Integer getMaxId();
    void addLog(Log log);
    void deleteLogById(Integer lid);
    void updateLog(Log log);
    void deleteAllStudent(int lid);
    Integer insertStudent(int sid, int lid);
}
