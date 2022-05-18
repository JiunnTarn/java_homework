package com.team9.project.repository.mapper;

import com.team9.project.models.Consume;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ConsumeMapper {
    List<Consume> getConsumeById(int id);
    List<Consume> getConsumeByConsumeInfo(String info);
    List<Consume> getConsumeByStudentInfo(String studentInfo);
    List<Consume> findAll();
    Integer getMaxId();
    void addConsume(Consume consume);
    void deleteConsumeById(Integer id);
    void updateConsume(Consume consume);
    void deleteAllStudent(int cid);
    void insertStudent(int sid, int cid);
}
