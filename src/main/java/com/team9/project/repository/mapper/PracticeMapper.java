package com.team9.project.repository.mapper;


import com.team9.project.models.Practice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PracticeMapper {
    List<Practice> getPracticeById(int id);
    List<Practice> getPracticeByPracticeInfo(String info);
    List<Practice> getPracticeByStudentInfo(String studentInfo);
    List<Practice> findAll();
    Integer getMaxId();
    void addPractice(Practice practice);
    void deletePracticeById(Integer id);
    void updatePractice(Practice practice);
    void deleteAllStudent(int pid);
    int insertStudent(int sid, int pid);
}
