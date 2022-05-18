package com.team9.project.repository.mapper;

import com.team9.project.models.Score;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ScoreMapper {
    List<Score> getScoreById(Integer id);
    List<Score> getScoreByStudentInfo(String studentInfo);
    List<Score> getScoreByCourseInfo(String courseInfo);
    List<Score> findAll();
    Integer getMaxId();
    void addScore(@Param("id")Integer id, @Param("sid") Integer sid, @Param("cid") Integer cid, @Param("score") String score);
    void deleteScoreById(Integer id);
    void deleteScoreByStudentId(Integer studentId);
    void deleteScoreByCourseId(Integer courseId);
    void updateScore(@Param("id")Integer id, @Param("sid") Integer sid, @Param("cid") Integer cid, @Param("score") String score);
}
