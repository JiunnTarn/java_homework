package com.team9.project.repository.mapper;

import com.team9.project.models.Activity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ActivityMapper {
    List<Activity> getActivityById(int id);
    List<Activity> getActivityByActivityInfo(String info);
    List<Activity> getActivityByStudentInfo(String studentInfo);
    List<Activity> findAll();
    Integer getMaxId();
    void addActivity(Activity activity);
    void deleteActivityById(Integer id);
    void updateActivity(Activity activity);
    void deleteAllStudent(int aid);
    void insertStudent(int sid, int aid);
}
